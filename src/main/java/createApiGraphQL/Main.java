package createApiGraphQL;
/*
 *  $Id: VirtuosoSPARQLExample2.java,v 1.3 2008/04/10 07:26:30 source Exp $
 *
 *  This file is part of the OpenLink Software Virtuoso Open-Source (VOS)
 *  project.
 *
 *  Copyright (C) 1998-2008 OpenLink Software
 *
 *  This project is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation; only version 2 of the License, dated June 1991.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 *
 */

//package virtuoso.jena.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import javax.lang.model.element.Modifier;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import virtuoso.jena.driver.*;

public class Main {
	
	static String dbName, user, password, url_hostlist;
	
	static private String fileDestination = Paths.get("./src/main/java").toAbsolutePath().normalize().toString();
	static private String destinationPathApiGraphQL = Paths.get("./src/main/resources/esquema.graphqls").toAbsolutePath().normalize().toString();

	static void getObjects(ArrayList<Object> objects, ArrayList<Field> fields, HashMap<String, ArrayList<String>> interfaces,  VirtGraph graph){
		

		Query sparql = QueryFactory.create("SELECT ?sujeto (group_concat(?subClass ; separator= \" \") as ?subClasses) FROM <"+ dbName +">  WHERE { " 
				+ "?sujeto <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/Object> ."
				+ "OPTIONAL {?sujeto <http://www.w3.org/2000/01/rdf-schema#subClassOf> ?subClass .}"
				+ "}"
				+ "group by ?sujeto");


	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();

	    while(res.hasNext()){
	    	 ArrayList<Field> fieldsOfObject = new ArrayList<>();
	    	 QuerySolution qs = res.next();
	    	 for(Field f : fields){
	    		 if(f.getDomain().equals(qs.get("?sujeto").toString())) fieldsOfObject.add(f);
	    	 }
	    	 
	    	 ArrayList<String> subClasses = null;
	    	 
	    	 if(qs.get("?subClasses").toString().length() == 0)subClasses = new ArrayList<>();
	    	 else{
	    		 subClasses = new ArrayList<String>(Arrays.asList(qs.get("?subClasses").toString().split(" ")));
	    		 for(String subClass : subClasses){
	    			 if(interfaces.containsKey(subClass))interfaces.get(subClass).add(qs.get("?sujeto").toString());
	    			 else{
	    				 interfaces.put(subClass, new ArrayList<String>());
	    				 interfaces.get(subClass).add(qs.get("?sujeto").toString());
	    			 }
	    		 }
	    	 }
	    	 objects.add(new Object(qs.get("?sujeto").toString() , subClasses, fieldsOfObject ));
	    }
	}
	
	
	static ArrayList<Modificador> sortModifiers(String startNode, ArrayList<String> otherNodes, VirtGraph graph){
		ArrayList<Modificador> orderedModifiers = new ArrayList<>();
		
		for(int i = 0; i < otherNodes.size(); ++i){
			Query sparql = QueryFactory.create("SELECT ?rightNode ?rightNodeType  FROM <"+ dbName +"> WHERE { "
					+ "<" + startNode + "> <http://www.essi.upc.edu/~jvarga/gql/combinedWith> ?rightNode."
					+ "?rightNode <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?rightNodeType."
					+ "}");
			VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
		    ResultSet res = vqe.execSelect();


		    while(res.hasNext()){
		    	 QuerySolution qs = res.next();
		    	 if(qs.get("?rightNode") != null){
		    		 if(qs.get("?rightNodeType").toString().equals("http://www.essi.upc.edu/~jvarga/gql/List")) orderedModifiers.add(new List(qs.get("?rightNode").toString(), new ArrayList<Modificador>()));
		    		 else if(qs.get("?rightNodeType").toString().equals("http://www.essi.upc.edu/~jvarga/gql/NonNull")) orderedModifiers.add(new NonNull(qs.get("?rightNode").toString(), new ArrayList<Modificador>()));
		    		 startNode = qs.get("?rightNode").toString();
		    	 }
		    }
		}
		
		return orderedModifiers;
	}
	
	static ArrayList<String> getCombinedModifiers(String subject , VirtGraph graph){
		ArrayList<String> combinedModifiers = new ArrayList<>();
		Query sparql = QueryFactory.create("SELECT  (group_concat(?combinedWith; separator= \" \") as ?combinedModifiers) FROM <"+ dbName +"> WHERE { "
				+ "<" + subject + "> <http://www.essi.upc.edu/~jvarga/gql/combinedWith>+ ?combinedWith."
				+ "}"
				);

	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();
	    while(res.hasNext()){
	    	 QuerySolution qs = res.next();
	    	 if(qs.get("?combinedModifiers").toString().length() == 0)combinedModifiers = new ArrayList<>();
	    	 else combinedModifiers = new ArrayList<String>(Arrays.asList(qs.get("?combinedModifiers").toString().split(" ")));
	    }

		return combinedModifiers;
	}
	
	static void getFields(ArrayList<Field> createdField, VirtGraph graph){
		Query sparql = QueryFactory.create("SELECT ?sujetoScalarField ?sujetoObjectField ?domain ?range ?modifierType ?modifier (group_concat(?combinedWith; separator= \" \") as ?combinedModifiers) FROM <"+ dbName +"> WHERE { "
				+ "	{"
				+ "	OPTIONAL {"
				+ "		?sujetoScalarField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ."
				+ "		?sujetoScalarField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/ScalarField> ."
				+ "		?sujetoScalarField <http://www.w3.org/2000/01/rdf-schema#domain> ?domain ."
				+ "		?sujetoScalarField <http://www.w3.org/2000/01/rdf-schema#range> ?range ."
				+ "			OPTIONAL{"
				+ "				?sujetoScalarField <http://www.essi.upc.edu/~jvarga/gql/hasModifier> ?modifier."
				+ "				?modifier <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?modifierType."
				+ "			}"
				+ "		}"
				+ "	}"
				+ "UNION"
				+ "	{"
				+ "	OPTIONAL {"
				+ "		?sujetoObjectField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/1999/02/22-rdf-syntax-ns#Property> ."
				+ "		?sujetoObjectField <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.essi.upc.edu/~jvarga/gql/ObjectField> ."
				+ "		?sujetoObjectField <http://www.w3.org/2000/01/rdf-schema#domain> ?domain ."
				+ "		?sujetoObjectField <http://www.w3.org/2000/01/rdf-schema#range> ?range ."
				+ "			OPTIONAL{"
				+ "				?sujetoObjectField <http://www.essi.upc.edu/~jvarga/gql/hasModifier> ?modifier."
				+ "				?modifier <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?modifierType."
				+ "			}"
				+ "		}"
				+ "	}"
				+ "}"
				+ "group by ?sujetoScalarField ?sujetoObjectField ?domain ?range ?modifierType ?modifier"
				);


	    VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
	    ResultSet res = vqe.execSelect();
	    





	    while(res.hasNext()){
	    	 QuerySolution qs = res.next();

	    	 String modifierType = null;
	    	 if(qs.get("?modifierType") != null) modifierType = qs.get("?modifierType").toString();

	    	 if(qs.get("?sujetoObjectField") != null){
	    		 if(modifierType != null){
	    			 ArrayList<Modificador> combinedModifiersOrdered = new ArrayList<>();
	    			 ArrayList<String> combinedModifiers = getCombinedModifiers(qs.get("?modifier").toString() , graph);
	    		 	 if(combinedModifiers.size() != 0) combinedModifiersOrdered = sortModifiers(qs.get("?modifier").toString(), combinedModifiers, graph);

	    		 	 //##ex:stopName  gql:hasModifier ex:nn1
	    		 	 //ex:nn1 a gql:NonNull . ---> modifier a modifierType
	    		 	 //ex:nn1 gql:combinedWith ex:l1 . --> combinedModifiersOrdered
	    			 if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/List"))createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new List(qs.get("?modifier").toString(), combinedModifiersOrdered)));
	    			 else if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/NonNull"))createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new NonNull(qs.get("?modifier").toString(), combinedModifiersOrdered)));

	    		 }else{
	    			 createdField.add(new ObjectField(qs.get("?sujetoObjectField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), null ));
	    		 }
	    	 }
	    	 else if(qs.get("?sujetoScalarField") != null){
	    		 if(modifierType != null){
	    			 ArrayList<Modificador> combinedModifiersOrdered = new ArrayList<>();
	    			 
	    			 ArrayList<String> combinedModifiers = getCombinedModifiers(qs.get("?modifier").toString() , graph);
	    			 if(combinedModifiers.size() != 0)combinedModifiersOrdered = sortModifiers(qs.get("?modifier").toString(), combinedModifiers, graph);
	    	
	    		 	 
	    			 if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/List"))createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new List(qs.get("?modifier").toString(), combinedModifiersOrdered)));
	    			 else if(modifierType.equals("http://www.essi.upc.edu/~jvarga/gql/NonNull"))createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), new NonNull(qs.get("?modifier").toString(), combinedModifiersOrdered)));

	    		 }else{
	    			 createdField.add(new ScalarField(qs.get("?sujetoScalarField").toString(),qs.get("?domain").toString(), qs.get("?range").toString(), null ));
	    		 }
	    	 }
	    }
	}
	
	static String constructRange(String range, Modificador mod){
		String combination = range;
		int contadorClaudators = 0;
		if(mod != null){
			if(mod.getClass().equals(List.class)){ combination = combination + "]"; ++contadorClaudators;}
			else if(mod.getClass().equals(NonNull.class)) combination = combination + "!";
			if(mod.getCombinedWith() != null && mod.getCombinedWith().size() > 0){		
				for(Modificador combined : mod.getCombinedWith()){		
					if(combined.getClass().equals(List.class)){ combination = combination + "]"; ++contadorClaudators;}
					else if(combined.getClass().equals(NonNull.class)) combination = combination + "!";
				}
			}
			while(contadorClaudators > 0){
				combination =  "[" + combination;
				--contadorClaudators;
			}
		}
		return combination;
	}
	
	static void writeFields(Object o , FileWriter fw) throws IOException{
		for(Field f : o.getFields()){
    		Integer index = f.getName().lastIndexOf("/");
			String shortName = f.getName().substring(index + 1);
			
			index = f.getRange().lastIndexOf("/");
			String shortRange = f.getRange().substring(index + 1);
			//construct Range [String!]
			String range = constructRange(shortRange, f.getModifier());
			fw.write("\t" + shortName + " : " + range + "\r\n");
		}
		
	}
	
	public static MethodSpec createModifyScalarValue(){
		
		MethodSpec method = MethodSpec.methodBuilder("modifyScalarValue")
				.addParameter(String.class, "value")
				.addCode("int index = value.toString().indexOf(\"^\");\n")
				.addCode("String resultat =  value.toString().substring(0, index);\n")
				.addCode("return resultat;\n")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.build();
		return method;
	}
	
	public static MethodSpec connectVirtuoso(){
		ClassName string = ClassName.get("java.lang", "String");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		TypeName listOfStrings = ParameterizedTypeName.get(arrayList, string);
		
		
		MethodSpec method = MethodSpec.methodBuilder("connectVirtuoso")
				.addParameter(String.class, "value")
				.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph, url_hostlist, user, password)
				.addCode("$T sparql = $T.create(\"Select ?valor FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
				.addCode("+ \" <\"+ this.idTurtle +\"> <\"+  value + \"> ?valor.\"\n")
				.addCode("+ \"}\");\n \n")
				.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
				.addCode("$T res = vqe.execSelect();\n", ResultSet)
				.addCode("ArrayList<String> valor = new ArrayList<>();\n\n")
				.addCode("while(res.hasNext()){\n")
				.addCode("\t $T qs = res.next();\n", QuerySolution)
				.addCode("\t valor.add(qs.get(\"?valor\").toString());\n")
				.addCode("}\n\n")
				.addCode("graph.close();\n")
				.addCode("return valor;\n")
			    .returns(listOfStrings)
			    .addModifiers(Modifier.PUBLIC)
			    .build();
		return method;
	}
	
	public static MethodSpec connectVirtuosoWithId(){
		ClassName string = ClassName.get("java.lang", "String");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		TypeName listOfStrings = ParameterizedTypeName.get(arrayList, string);
		
		
		MethodSpec method = MethodSpec.methodBuilder("connectVirtuoso")
				.addParameter(String.class, "value")
				.addParameter(String.class, "id")
				.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph, url_hostlist, user, password)
				.addCode("$T sparql = $T.create(\"Select ?valor FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
				.addCode("+ \" <\"+ id +\"> <\"+  value + \"> ?valor.\"\n")
				.addCode("+ \"}\");\n \n")
				.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
				.addCode("$T res = vqe.execSelect();\n", ResultSet)
				.addCode("ArrayList<String> valor = new ArrayList<>();\n\n")
				.addCode("while(res.hasNext()){\n")
				.addCode("\t $T qs = res.next();\n", QuerySolution)
				.addCode("\t valor.add(qs.get(\"?valor\").toString());\n")
				.addCode("}\n\n")
				.addCode("graph.close();\n")
				.addCode("return valor;\n")
			    .returns(listOfStrings)
			    .addModifiers(Modifier.PUBLIC)
			    .build();
		return method;
	}
	
	public static ClassName getClassName(String name){
		ClassName ClassName = null;
		if(name.equals("Int")) ClassName = ClassName.get("java.lang", "Integer");
		else if(name.equals("String")) ClassName = ClassName.get("java.lang", "String");
		else if(name.equals("Boolean")) ClassName = ClassName.get("java.lang", "Boolean");
		else if(name.equals("Float")) ClassName = ClassName.get("java.lang", "Float");
		else if(name.equals("ID")) ClassName = ClassName.get("java.lang", "Integer");
		else ClassName = ClassName.get("serverGraphQL", name);
		return ClassName;
	}

	static void createType(Object o, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		
		ArrayList<MethodSpec> methods = new ArrayList<>();
		Integer index = o.getName().lastIndexOf("/");
		String nameObject = o.getName().substring(index + 1);
		
		TypeSpec.Builder builderType = TypeSpec.classBuilder(nameObject)
		.addModifiers(Modifier.PUBLIC);
		
		if(o.isInterface()) builderType.addField(String.class, "idTurtle", Modifier.PROTECTED);
		else if(o.getSubClassOf().isEmpty() && !o.isInterface()) builderType.addField(String.class, "idTurtle", Modifier.PRIVATE);
		
		//Constructor
		MethodSpec.Builder methodConstructor = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC)
			    .addParameter(String.class, "idTurtle");
		
		if(!o.getSubClassOf().isEmpty() && !o.isInterface())methodConstructor.addStatement("super($L)", "idTurtle");
		else methodConstructor.addStatement("this.$L = $L", "idTurtle", "idTurtle");
		
		methods.add(methodConstructor.build());

		
		// InfrastrustureType : String on interface
		if(o.isInterface()){
			index = o.getName().lastIndexOf("/");
			String nameInterface= o.getName().substring(index + 1);
			
			
			MethodSpec method = MethodSpec.methodBuilder( nameInterface + "Type")
				    .addModifiers(Modifier.PUBLIC)
				    .returns(ClassName.get("java.lang", "String"))
				    .addStatement("return $S", nameObject)
				    .build();
			methods.add(method);
		}
		// InfrastrustureType : String on subClasses
		for(String subClass : o.getSubClassOf()){
			index = subClass.lastIndexOf("/");
			String nameSubClass= subClass.substring(index + 1);
			
			builderType.superclass(ClassName.get("serverGraphQL", nameSubClass));
			
			MethodSpec method = MethodSpec.methodBuilder( nameSubClass + "Type")
				    .addModifiers(Modifier.PUBLIC)
				    .returns(ClassName.get("java.lang", "String"))
				    .addStatement("return $S", nameObject)
				    .build();
			methods.add(method);
		}
		
		
		for(Field field : o.getFields()){
			index = field.getName().lastIndexOf("/");
			String nameField = field.getName().substring(index + 1);
			
			index = field.getRange().lastIndexOf("/");
			String rangeField = field.getRange().substring(index + 1);
			
			ClassName className = getClassName(rangeField);
			ClassName arrayList = ClassName.get("java.util", "ArrayList");
			TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
			TypeName nestedListOfClassName = ParameterizedTypeName.get(arrayList, listOfClassName);
			
			MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(nameField)
				    .addModifiers(Modifier.PUBLIC);
			
			//See if its [], [[]] or nothing
			int contadorList = 0;
			if(field.getModifier() != null){
				if(field.getModifier().getClass().equals(List.class)) ++contadorList;
				if(field.getModifier().getCombinedWith() != null){
					for(Modificador m : field.getModifier().getCombinedWith()){
						if(m.getClass().equals(List.class)) ++contadorList;
					}
				}
			}
			
			boolean list = false;
			boolean nestedList = false;
			if(contadorList == 2) nestedList = true;
			else if(contadorList == 1) list = true;
			

			if(field.getClass().equals(ScalarField.class)){
				if(rangeField.equals("Int") || rangeField.equals("ID")) rangeField = "Integer";
				if(!list && !nestedList){
					// AAA : Int
					methodBuild.returns(className);
					methodBuild.addStatement("$T<String> result = connectVirtuoso(\"$L\") ", arrayList, field.getName());
	    			methodBuild.addStatement("if(result.size() == 0) return null");
	    			if(rangeField.equals("Integer")) methodBuild.addStatement("else return $L.parse$L(modifyScalarValue(result.get(0)))", rangeField, "Int");
	    			else if(rangeField.equals("String")) methodBuild.addStatement("else return modifyScalarValue(result.get(0))");
	    			else methodBuild.addStatement("else return $L.parse$L(modifyScalarValue(result.get(0)))", rangeField, rangeField);
				}else if(list){
					//AAA : [Int]	
					methodBuild.returns(listOfClassName);
					methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
					methodBuild.addStatement("$T<$L> $L = new ArrayList<>()", arrayList, rangeField, nameField+ "s");
					
					if(rangeField.equals("String")) methodBuild.addStatement("for(String value:$L) $L.add(modifyScalarValue(value))", nameField, nameField + "s");
			    	else if(rangeField.equals("Integer")) methodBuild.addStatement("for(String value:$L) $L.add($L.parse$L(modifyScalarValue(value)))", nameField, nameField + "s", rangeField, "Int");
			    	else methodBuild.addStatement("for(String value:$L) $L.add($L.parse$L(modifyScalarValue(value)))", nameField, nameField + "s", rangeField, rangeField);
					
					methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
					methodBuild.addStatement("else return $L", nameField + "s");
				}else if(nestedList){
					//AAA : [[Int]]
					methodBuild.returns(nestedListOfClassName);
					methodBuild.addStatement("ArrayList<ArrayList<$L>> result = new ArrayList<>()", rangeField);
					methodBuild.addStatement("$T<String> valuesOfLists = new ArrayList<>()", arrayList);
					methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
					methodBuild.addCode("for(String value:$L) { \n", nameField);
					methodBuild.addCode("\t ArrayList<String> lists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", value); \n");
					methodBuild.addCode("\t for(String list:lists){ \n");
					methodBuild.addCode("\t \t valuesOfLists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", list); \n");
					
					methodBuild.addStatement("\t \t $T<$L> correctValuesOfLists = new ArrayList<>()", arrayList, rangeField);
					if(rangeField.equals("String")) methodBuild.addStatement("\t \t for(String valueOfList :valuesOfLists) $L.add(modifyScalarValue(valueOfList))", "correctValuesOfLists");
			    	else if(rangeField.equals("Integer")) methodBuild.addStatement("\t \t for(String valueOfList :valuesOfLists) $L.add($L.parse$L(modifyScalarValue(valueOfList)))", "correctValuesOfLists", rangeField, "Int");
			    	else methodBuild.addStatement("\t \t for(String valueOfList : valuesOfLists) $L.add($L.parse$L(modifyScalarValue(valueOfList)))", "correctValuesOfLists", rangeField, rangeField);
					
					methodBuild.addStatement("\t \t result.add(correctValuesOfLists)");
					methodBuild.addCode("\t } \n");
					methodBuild.addCode("} \n");
					
					
					methodBuild.addStatement("if(result.size() == 0) return null");
					methodBuild.addStatement("else return result");
					

				}
			}else if(field.getClass().equals(ObjectField.class)){
				if(!interfaces.containsKey(field.getRange())){
					if(!list && !nestedList){
						// AAA : District
						methodBuild.returns(className);
						methodBuild.addStatement("$T<String> result = connectVirtuoso(\"$L\")", arrayList, field.getName());
		    			methodBuild.addStatement("if(result.size() == 0) return null");
			    		methodBuild.addStatement("else return new $L(result.get(0))", rangeField);
					}else if(list){
						// AAA : [District]
						methodBuild.returns(listOfClassName);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
						methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", rangeField, nameField+ "s");
						methodBuild.addStatement("for(String id:$L) $L.add(new $L(id))", nameField, nameField + "s", rangeField);
						methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
						methodBuild.addStatement("else return $L", nameField + "s");
					}else if(nestedList){
						// AAA : [[District]]
						methodBuild.returns(nestedListOfClassName);
						
						methodBuild.addStatement("ArrayList<ArrayList<$L>> result = new ArrayList<>()", rangeField);
						methodBuild.addStatement("$T<String> valuesOfLists = new ArrayList<>()", arrayList);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
						methodBuild.addCode("for(String value:$L) { \n", nameField);
						methodBuild.addCode("\t ArrayList<String> lists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", value); \n");
						methodBuild.addCode("\t for(String list:lists){ \n");
						methodBuild.addCode("\t \t valuesOfLists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", list); \n");
						
						methodBuild.addStatement("\t \t $T<$L> correctValuesOfLists = new ArrayList<>()", arrayList, rangeField);
						methodBuild.addStatement("\t \t for(String valueOfList :valuesOfLists) $L.add(new $L(valueOfList))", "correctValuesOfLists", rangeField);

						methodBuild.addStatement("\t \t result.add(correctValuesOfLists)");
						methodBuild.addCode("\t } \n");
						methodBuild.addCode("} \n");
						
						
						methodBuild.addStatement("if(result.size() == 0) return null");
						methodBuild.addStatement("else return result");
						
					}
				}else{
					if(!list && !nestedList){
						// AAA : Infrastructure
						methodBuild.returns(className);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")",  arrayList, nameField, field.getName());
						methodBuild.addCode("$L $L = null;\n", rangeField, "result");
						methodBuild.addCode("for(String id: $L){\n", nameField);
						
						for(String possibleOption : interfaces.get(field.getRange())){
							index = possibleOption.lastIndexOf("/");
							String shortNamePossibleOption = possibleOption.substring(index + 1);
							methodBuild.addCode("\t if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).get(0).equals(\"$L\"))  $L = new $L(id); \n",possibleOption , "result",shortNamePossibleOption );
						}
						methodBuild.addCode("}\n");
						
						methodBuild.addStatement("if($L == null) return null", "result");
						methodBuild.addStatement("else return $L", "result");
						
						
					}else if(list){
						// AA : [Infrastructure]
						methodBuild.returns(listOfClassName);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")",  arrayList, nameField, field.getName());
						methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", rangeField, nameField+ "s");
						methodBuild.addCode("for(String id: $L){\n", nameField);
						for(String possibleOption : interfaces.get(field.getRange())){
							index = possibleOption.lastIndexOf("/");
							String shortNamePossibleOption = possibleOption.substring(index + 1);
							methodBuild.addCode("\t if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", id).get(0).equals(\"$L\")) $L.add(new $L(id)); \n",possibleOption , nameField+ "s", shortNamePossibleOption );
						}
						methodBuild.addCode("}\n");
						methodBuild.addStatement("if($L.size() == 0) return null", nameField + "s");
						methodBuild.addStatement("else return $L", nameField+ "s");
						
					}else if(nestedList){
						// AAA : [[Infrastructure]]
						methodBuild.returns(nestedListOfClassName);
						methodBuild.addStatement("ArrayList<ArrayList<$L>> result = new ArrayList<>()", rangeField);
						methodBuild.addStatement("$T<String> valuesOfLists = new ArrayList<>()", arrayList);
						methodBuild.addStatement("$T<String> $L = connectVirtuoso(\"$L\")", arrayList, nameField, field.getName());
						methodBuild.addCode("for(String value:$L) { \n", nameField);
						methodBuild.addCode("\t ArrayList<String> lists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", value); \n");
						methodBuild.addCode("\t for(String list:lists){ \n");
						methodBuild.addCode("\t \t valuesOfLists = connectVirtuoso(\"http://www.essi.upc.edu/~jvarga/gql/hasElement\", list); \n");
						
						methodBuild.addStatement("\t \t $T<$L> correctValuesOfLists = new ArrayList<>()", arrayList, rangeField);
						methodBuild.addCode("\t \t for(String valueOfList :valuesOfLists){ \n");
						
						for(String possibleOption : interfaces.get(field.getRange())){
							index = possibleOption.lastIndexOf("/");
							String shortNamePossibleOption = possibleOption.substring(index + 1);
							methodBuild.addCode("\t \t \t if(connectVirtuoso(\"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\", valueOfList).get(0).equals(\"$L\")) $L.add(new $L(valueOfList)); \n",possibleOption , "correctValuesOfLists", shortNamePossibleOption );
						}
						
						methodBuild.addCode("\t \t }\n");
						methodBuild.addStatement("\t \t result.add(correctValuesOfLists)");
						methodBuild.addCode("\t } \n");
						methodBuild.addCode("} \n");
						
						
						methodBuild.addStatement("if(result.size() == 0) return null");
						methodBuild.addStatement("else return result");
					}
				}
			}
			
			MethodSpec method = methodBuild.build();
			methods.add(method);
			
		}
		
		for(MethodSpec m : methods) builderType.addMethod(m);
		builderType.addMethod(connectVirtuosoWithId());
		builderType.addMethod(connectVirtuoso());
		builderType.addMethod(createModifyScalarValue());
		
		TypeSpec typeSpec = builderType.build();
		JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();

		javaFile.writeTo(new File(fileDestination));	
	}
	
	public static MethodSpec constructorRepository(String nameObject){
		Integer index = nameObject.lastIndexOf("/");
		String shortNameObject = nameObject.substring(index + 1);
		
		
		ClassName ArrayList = ClassName.get("java.util", "ArrayList");
		ClassName Query = ClassName.get("org.apache.jena.query", "Query");
		ClassName QueryFactory = ClassName.get("org.apache.jena.query", "QueryFactory");
		ClassName QuerySolution = ClassName.get("org.apache.jena.query", "QuerySolution");
		ClassName ResultSet = ClassName.get("org.apache.jena.query", "ResultSet");

		ClassName VirtGraph = ClassName.get("virtuoso.jena.driver", "VirtGraph");
		ClassName VirtuosoQueryExecution = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecution");
		ClassName VirtuosoQueryExecutionFactory = ClassName.get("virtuoso.jena.driver", "VirtuosoQueryExecutionFactory");
		
		
		
		MethodSpec method = MethodSpec.constructorBuilder()
				.addCode("$L = new $T<>();\n", shortNameObject + "s", ArrayList)
				.addCode("$T graph = new $T (\"$L\", \"$L\", \"$L\");\n", VirtGraph , VirtGraph,  url_hostlist, user, password)
				.addCode("$T sparql = $T.create(\"Select ?subject FROM <$L> WHERE {\"\n", Query, QueryFactory, dbName)
				.addCode("+ \" ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <$L>.\"\n", nameObject)
				.addCode("+ \"}\");\n \n")
				.addCode("$T vqe = $T.create (sparql, graph);\n",VirtuosoQueryExecution, VirtuosoQueryExecutionFactory)
				.addCode("$T res = vqe.execSelect();\n", ResultSet)
				.addCode("while(res.hasNext()){\n")
				.addCode("\t $T qs = res.next();\n", QuerySolution)
				.addCode("\t String subject = qs.get(\"?subject\").toString();\n")
				.addCode("\t $L.add(new $L(subject));\n", shortNameObject + "s", shortNameObject)
				.addCode("}\n\n")
				.addCode("graph.close();\n")
			    .addModifiers(Modifier.PUBLIC)
			    .build();
		return method;

	}
	
	public static MethodSpec allInstancesRepository(String nameObject){
		
		ClassName clase = ClassName.get("serverGraphQL", nameObject);
		ClassName arrayList = ClassName.get("java.util", "List");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, clase);
		
		MethodSpec method = MethodSpec.methodBuilder("getAll" + nameObject + "s")
				.addStatement("return $L", nameObject + "s")
			    .addModifiers(Modifier.PUBLIC)
			    .returns(listOfClass)
			    .build();
		return method;

	}
	
	public static MethodSpec oneInstanceRepository(String nameObject){
		
		ClassName clase = ClassName.get("serverGraphQL", nameObject);
		
		MethodSpec method = MethodSpec.methodBuilder("get" + nameObject)
				.addParameter(String.class, "id")
				.addStatement("return new $L(id)", nameObject)
			    .addModifiers(Modifier.PUBLIC)
			    .returns(clase)
			    .build();
		return method;

	}
	static void createRepository(Object o) throws IOException{
		
		Integer index = o.getName().lastIndexOf("/");
		String nameObject = o.getName().substring(index + 1);
		
		ClassName className = getClassName(nameObject);
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
		TypeName nestedListOfClassName = ParameterizedTypeName.get(arrayList, listOfClassName);
		
		
		TypeSpec.Builder builder = TypeSpec.classBuilder(nameObject + "Repository")
			    .addModifiers(Modifier.PUBLIC)
				.addField(listOfClassName, nameObject + "s", Modifier.PRIVATE, Modifier.FINAL);
		
		builder.addMethod(constructorRepository(o.getName()));
		builder.addMethod(allInstancesRepository(nameObject));
		builder.addMethod(oneInstanceRepository(nameObject));
		
		TypeSpec typeSpec = builder.build();
		
		JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();

		javaFile.writeTo(new File(fileDestination));	
	}
	
	public static MethodSpec queryList(String nameObject){
		ClassName clase = ClassName.get("serverGraphQL", nameObject);
		ClassName arrayList = ClassName.get("java.util", "List");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, clase);
		
		MethodSpec method = MethodSpec.methodBuilder("all" + nameObject + "s")
				.addCode("return $LRepositoryInstance.getAll$Ls();\n", nameObject, nameObject)
				.addModifiers(Modifier.PUBLIC)
				.returns(listOfClass)
				.build();
		return method;
	}
	
	public static MethodSpec queryOne(String nameObject){
		ClassName clase = ClassName.get("serverGraphQL", nameObject);

		MethodSpec method = MethodSpec.methodBuilder("get" + nameObject )
				.addCode("return $LRepositoryInstance.get$L(id);\n", nameObject, nameObject)
				.addModifiers(Modifier.PUBLIC)
				.returns(clase)
				.addParameter(String.class, "id")
				.build();
		return method;
	}
	
	
	public static void buildQuery(ArrayList<String> repositories) throws IOException{
		ClassName claseImplements = ClassName.get("com.coxautodev.graphql.tools", "GraphQLQueryResolver");
		ClassName arrayList = ClassName.get("java.util", "ArrayList");
		ClassName claseExtends = ClassName.get("graphql.servlet" , "SimpleGraphQLServlet");
		ClassName schemaParser = ClassName.get("com.coxautodev.graphql.tools" , "SchemaParser");
		ClassName GraphQLSchema = ClassName.get("graphql.schema" , "GraphQLSchema");
		ClassName webServlet = ClassName.get("javax.servlet.annotation" , "WebServlet");
		
		TypeName listOfClass = ParameterizedTypeName.get(arrayList, claseImplements);
		
		TypeSpec.Builder builderQuery = TypeSpec.classBuilder("Query")
			    .addModifiers(Modifier.PUBLIC)
			    .addSuperinterface(claseImplements);
		
		TypeSpec.Builder builderGraphQLEndPoint = TypeSpec.classBuilder("GraphQLEndPoint")
				.addModifiers(Modifier.PUBLIC)
				.superclass(claseExtends)
			    .addAnnotation(AnnotationSpec.builder(webServlet)
	                    .addMember("value", "$L", "urlPatterns = \"/graphql\"")
	                    .build());
		
		MethodSpec.Builder constructorQueryBuilder = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC);
		
		MethodSpec.Builder constructorGraphQLEndPoint = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC)
			    .addCode("super($T.newParser()\n", schemaParser)
				.addCode(".file($S)\n", "esquema.graphqls")
				.addCode(".resolvers(new $L(", "Query");
		
		boolean first = true;
		for(String repo : repositories){
			builderQuery.addMethod(queryList(repo));
			builderQuery.addMethod(queryOne(repo));
			
			String repository = repo + "Repository";
			ClassName clase = ClassName.get("serverGraphQL", repository);
			String repoInstance = repository + "Instance";
			builderQuery.addField(clase, repoInstance , Modifier.PRIVATE, Modifier.FINAL); 
			constructorQueryBuilder.addParameter(clase, repoInstance);
			constructorQueryBuilder.addStatement("this.$N = $N", repoInstance , repoInstance );
			
			if(first) {constructorGraphQLEndPoint.addCode("new $L()", repository); first = false;}
			else constructorGraphQLEndPoint.addCode(", new $L()", repository);
		}
		
		constructorGraphQLEndPoint.addCode("))\n");
		constructorGraphQLEndPoint.addCode(".build()\n");
		constructorGraphQLEndPoint.addCode(".makeExecutableSchema());");
		
		
		builderQuery.addMethod(constructorQueryBuilder.build());
		
		builderGraphQLEndPoint.addMethod(constructorGraphQLEndPoint.build());
		
		//Query
		TypeSpec typeSpec = builderQuery.build();
		JavaFile javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();
		javaFile.writeTo(new File(fileDestination));	
		
		//GraphQLEndPoint
		typeSpec = builderGraphQLEndPoint.build();
		javaFile = JavaFile.builder("serverGraphQL", typeSpec)
			    .build();
		javaFile.writeTo(new File(fileDestination));	
		

	}

	
	static void createServer(ArrayList<Object> createdObjects, HashMap<String, ArrayList<String>> interfaces) throws IOException{
		ArrayList<String> repositories = new ArrayList<>();
		for(Object o : createdObjects){
			createType(o,interfaces);
			if(!o.isInterface()){
				createRepository(o);
				Integer index = o.getName().lastIndexOf("/");
				String nameObjectRepository = o.getName().substring(index + 1);
				repositories.add(nameObjectRepository);
			}
		}
		buildQuery(repositories);
	}
	/**
	 * Executes a SPARQL query against a virtuoso url and prints results.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
	    Properties prop = new Properties();
	    
	    File file = new File("src/main/resources/config.properties");
	    InputStream input = new FileInputStream(file);
	    
	    try {
	        // load a properties file
	        prop.load(input);

	        // get the property value and print it out
	        user = prop.getProperty("user");
	        password = prop.getProperty("password");
	        

	        url_hostlist = prop.getProperty("url_hostlist");
	        
	        dbName = prop.getProperty("dbName");

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (input != null) {
	            try {
	                input.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		
		VirtGraph graph = new VirtGraph (url_hostlist, user, password);

		graph.clear ();

		ArrayList<Object> createdObjects = new ArrayList<>();
		HashMap<String, ArrayList<String>> interfaces = new HashMap<>();
		ArrayList<Field> createdField= new ArrayList<>();
		
		//get Objects
		
		getFields(createdField, graph);
		getObjects(createdObjects, createdField, interfaces, graph);

		graph.close();

		
		File newTextFile = new File(destinationPathApiGraphQL);

        FileWriter fw = new FileWriter(newTextFile);
        
        for(Object o : createdObjects){
        	boolean interfaz = false;
        	if(interfaces.containsKey(o.getName())){ interfaz = true; o.setInterface(true);}
    		Integer index = o.getName().lastIndexOf("/");
			String shortName = o.getName().substring(index + 1);
        	if(interfaz){
    			fw.write("interface " + shortName + " {" + "\r\n"); //First line
    			fw.write("	" + shortName + "Type: String!" + "\r\n"); //type 
        	}else{
        		fw.write("type " + shortName + " ");
        		if(o.getSubClassOf().size() == 0)  fw.write("{" + "\r\n");
        		else{
	        			int i = 0;
	        			for(String subClassOf : o.getSubClassOf()){
	        				index = subClassOf.lastIndexOf("/");
	        				String shortNameSubClass = subClassOf.substring(index + 1);
	        				if(i == 0) fw.write("implements " + shortNameSubClass);
	        				else fw.write(", " + shortNameSubClass);
	        				++i;
	        			}
	        			fw.write("{" + "\r\n");
        			
    					//write Field of interface in the type
        				for(String subClassOf : o.getSubClassOf()){
        					for(Object searchParent : createdObjects){
        						if(searchParent.getName().equals(subClassOf)){
        							//add Fields of parents to object
        							//o.getFields().addAll(searchParent.getFields());
        							writeFields(searchParent, fw);
        	        				index = subClassOf.lastIndexOf("/");
        	        				String shortNameSubClass = subClassOf.substring(index + 1);
        	        				fw.write("	" + shortNameSubClass + "Type: String!" + "\r\n"); //type 
        						}
        				}
        			}
        		}
        	}
        	writeFields(o, fw);
        	fw.write("}" + "\r\n" + "\r\n"); //End type/ interface
        }
        


        
        //Queries
        fw.write("type Query {" + "\r\n");
        for(int i = 0; i < createdObjects.size(); ++i){
        	if(!createdObjects.get(i).isInterface()){
        		Integer index = createdObjects.get(i).getName().lastIndexOf("/");
				String shortNameSubClass = createdObjects.get(i).getName().substring(index + 1);
        		fw.write("	" + "all"+ shortNameSubClass + "s: [" + shortNameSubClass +"]" + "\r\n");
        		fw.write("	" + "get"+ shortNameSubClass + "(id: String!): " + shortNameSubClass +"" + "\r\n");
        		
        	}
        }
        fw.write("}" + "\r\n");
        
        fw.write("schema {" + "\r\n");
        fw.write("	query: Query" + "\r\n");
        fw.write("}" + "\r\n" + "\r\n");
        fw.close();
        createServer(createdObjects, interfaces);
	}
	
	
	

	


}
