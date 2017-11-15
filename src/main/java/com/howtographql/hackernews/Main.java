package com.howtographql.hackernews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.lang.model.element.Modifier;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class Main {
	
	public static String getScalarField(String line){
		String name = "";
		for(int i = line.lastIndexOf(':'); i < line.length(); ++i){
			if(line.charAt(i) != ' ' && line.charAt(i) != ':') name += line.charAt(i);
		}
		
		return name;
	}
	
	public static String getField(String line){
		String name = "";
		for(int i = 0; i < line.length(); ++i){
			if(i == line.lastIndexOf(':')) break;
			else if(line.charAt(i) != ' ') name += line.charAt(i);
		}
		return name;
	}
	
	public static String getName(String line, String match){
		String name = "";
		
		int	index = line.toLowerCase().indexOf(match);
		index = index + match.length();
		
		for(int i = index; i < line.length(); ++i){
			// District {  --- first space
			if(line.charAt(i) == ' ' && name.isEmpty()) continue;
			// District {  --- last sapce or  District{  --- last {
			else if(line.charAt(i) == ' ' || line.charAt(i) == '{') break;
			else if(line.charAt(i) != ' ')name += line.charAt(i);
		}

		return name;
	}
	
	public static ArrayList<String> getInterfaces(String line){
		ArrayList<String> nameInterfaces = new ArrayList<>();
		String interfaz = "";
		String implement = "implements";
		int index = line.toLowerCase().indexOf(implement);
		index = index + implement.length();

		for(int i = index; i < line.length(); ++i){
			// implements Infrastructure{  --- first space
			if(line.charAt(i) == ' ' && nameInterfaces.isEmpty()) continue;
			// District {  --- last sapce or  District{  --- last {
			else if(line.charAt(i) == '{'){ nameInterfaces.add(interfaz); break;}
			else if(line.charAt(i) == ','){ nameInterfaces.add(interfaz); interfaz = "";}
			else if(line.charAt(i) != ' ') interfaz += line.charAt(i);
		}
		return nameInterfaces;
	}
	
	public static boolean isSimpleType(String finalScalar){
		if(finalScalar.equals("Int") || finalScalar.equals("String") || finalScalar.equals("ID") || finalScalar.equals("Boolean") || finalScalar.equals("Float")) return true;
		else return false;
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
				.addCode("$T graph = new $T (\"TFG_Example1\", \"jdbc:virtuoso://localhost:1111\", \"dba\", \"dba\");\n", VirtGraph , VirtGraph)
				.addCode("$T sparql = $T.create(\"Select ?valor FROM <http://localhost:8890/Example4> WHERE {\"\n", Query, QueryFactory)
				.addCode("+ \"OPTIONAL { <\"+ this.getIdTurtle() +\"> <\"+  value + \"> ?valor}.\"\n")
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
	
	public static MethodSpec getIdTurtle(){
		MethodSpec method = MethodSpec.methodBuilder("getIdTurtle")
				.addCode("return idTurtle;\n")
				.addModifiers(Modifier.PRIVATE)
				.returns(String.class)
				.build();
		return method;
	}
	
	public static MethodSpec constructor(){
		MethodSpec method = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC)
			    .addParameter(String.class, "idTurtle")
			    .addStatement("this.$N = $N", "idTurtle", "idTurtle")
			    .build();
		return method;
	}
	
	
	public static ClassName getClassName(String name){
		ClassName ClassName = null;
		if(name.equals("Integer")) ClassName = ClassName.get("java.lang", "Integer");
		else if(name.equals("String")) ClassName = ClassName.get("java.lang", "String");
		else if(name.equals("Boolean")) ClassName = ClassName.get("java.lang", "Boolean");
		else if(name.equals("Float")) ClassName = ClassName.get("java.lang", "Float");
		return ClassName;
	}
	
	public static String getFinalScalar(String finalScalar){
		if(finalScalar.contains("!")) finalScalar = finalScalar.replace("!", ""); 
		if(finalScalar.contains("[") && finalScalar.contains("]")){
			finalScalar = finalScalar.replace("]", "");
			finalScalar = finalScalar.replace("[", "");

		}
		return finalScalar;
	}
	public static void buildType(String nameType, String nameInterface,  ArrayList<String> interfaces,  ArrayList<String> nameFields,  ArrayList<String> scalarFields) throws IOException{
		//Type District
		if(nameType != "" && !nameType.equals("Query")){
			int i = 0;
			ArrayList<MethodSpec> methods = new ArrayList<>();
			
			// AAA : [BBB!]
			// AAA -> NameField
			// BBB -> ScalarField/finalScalar
			for(String nameField : nameFields){
				
				String finalScalar = scalarFields.get(i);
				boolean lista = finalScalar.contains("[") && finalScalar.contains("]");
				//[String!] -> String
				finalScalar = getFinalScalar(finalScalar);
				
				boolean isSimple = isSimpleType(finalScalar);
				if(isSimple){
					// AAA : String, ID, Boolean, Int, Float
					//Int -> Integer, ID -> Integer
					if(finalScalar.equals("Int") || finalScalar.equals("ID")) finalScalar = "Integer";
					ClassName className = getClassName(finalScalar);
					ClassName arrayList = ClassName.get("java.util", "ArrayList");
					TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
					
					String output = nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
					MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("get" + output)
						    .addModifiers(Modifier.PUBLIC);
					
					// AAA : Int
				    if(!lista){
				    	methodBuild.returns(className);
				    	if(finalScalar.equals("String")) methodBuild.addStatement("return modifyScalarValue(connectVirtuoso(\"http://www.example.com/$L\").get(0))", nameField);
				    	else if(finalScalar.equals("Integer")) methodBuild.addStatement("return $L.parse$L(modifyScalarValue(connectVirtuoso(\"http://www.example.com/$L\").get(0)))", finalScalar, "Int", nameField);
				    	else methodBuild.addStatement("return $L.parse$L(modifyScalarValue(connectVirtuoso(\"http://www.example.com/$L\").get(0)))", finalScalar, finalScalar, nameField);
				    }else{
				    // AAA : [Int]
				    	methodBuild.returns(listOfClassName);
						methodBuild.addStatement("ArrayList<String> $L = connectVirtuoso(\"http://www.example.com/$L\")", nameField, nameField);
						methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", finalScalar, nameField+ "s");
						
						if(finalScalar.equals("String")) methodBuild.addStatement("for(String id:$L) $L.add(id)", nameField, nameField + "s");
				    	else if(finalScalar.equals("Integer")) methodBuild.addStatement("for(String id:$L) $L.add($L.parse$L(id))", nameField, nameField + "s", finalScalar, "Int");
				    	else methodBuild.addStatement("for(String id:$L) $L.add($L.parse$L(id))", nameField, nameField + "s", finalScalar, finalScalar);
						
						methodBuild.addStatement("return $L", nameField + "s");
				    }
					MethodSpec method = methodBuild.build();
					
					methods.add(method);
				}else{
					//Other types (GeographicalCoordinate...)
					//AAA : GeographicalCoordinate, District
					
					//Falta diferenciar entre interfaz o no
					ClassName className = ClassName.get("com.howtographql.hackernews", finalScalar);
					ClassName arrayList = ClassName.get("java.util", "ArrayList");
					TypeName listOfClassName = ParameterizedTypeName.get(arrayList, className);
					
					String output = nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
					MethodSpec.Builder methodBuild = MethodSpec.methodBuilder("get" + output)
						    .addModifiers(Modifier.PUBLIC);
					if(!lista){
						// AAA : District
						methodBuild.returns(className);
						methodBuild.addStatement("return new $L(connectVirtuoso(\"http://www.example.com/$L\").get(0))", finalScalar, nameField);
					}else{
						// AAA : [District]
						methodBuild.returns(listOfClassName);
						methodBuild.addStatement("ArrayList<String> $L = connectVirtuoso(\"http://www.example.com/$L\")", nameField, nameField);
						methodBuild.addStatement("ArrayList<$L> $L = new ArrayList<>()", finalScalar, nameField+ "s");
						methodBuild.addStatement("for(String id:$L) $L.add(new $L(id))", nameField, nameField + "s", finalScalar);
						methodBuild.addStatement("return $L", nameField + "s");
					}
					MethodSpec method = methodBuild.build();
					methods.add(method);
				}
					   			
				++i;
			}
			
			methods.add(createModifyScalarValue());
			methods.add(connectVirtuoso());
			methods.add(getIdTurtle());
			methods.add(constructor());
			
			TypeSpec.Builder builder = TypeSpec.classBuilder(nameType + "example")
					.addModifiers(Modifier.PUBLIC)
					.addField(String.class, "idTurtle", Modifier.PRIVATE);
			
			for(MethodSpec m : methods){
				builder.addMethod(m);
			}
			TypeSpec typeSpec = builder.build();
			
			JavaFile javaFile = JavaFile.builder("com.howtographql.hackernews", typeSpec)
				    .build();

			javaFile.writeTo(new File(Paths.get("./src/main/java").toAbsolutePath().normalize().toString()));	    

		}
		//Interface
		else if(nameInterface != ""){
			
		}
	}

	public static void main(String[] args) throws IOException {
		
		FileInputStream fis = new FileInputStream("C:\\Users\\rober_000\\workspace\\hackernews-graphql-java\\src\\main\\resources\\ejemplo2.graphqls");
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		
		ArrayList<String> interfaces = new ArrayList<>();
		ArrayList<String> nameFields = new ArrayList<>();
		ArrayList<String> scalarFields = new ArrayList<>();
		String nameType = "";
		String nameInterface = "";
		
		boolean empieza = false;
		while ((line = br.readLine()) != null) {
			line = line.replace("\t", " ");
			
			//Can assume that one type/interface will start because the { can only be there
			if(line.contains("{")) empieza = true;
			
			if(empieza){
				
				
				//Is a type
				if(line.toLowerCase().contains("type")  && line.contains("{")){
					nameType = getName(line, "type");
					System.out.println("name " + nameType);
					//Implements some interface
					if(line.toLowerCase().contains("implements")){
						interfaces = getInterfaces(line);
						for(String n : interfaces) System.out.print("implements " + n);
						System.out.println();
						
					}
				}
				//Is a interface
				else if(line.toLowerCase().contains("interface")  && line.contains("{")){
					nameInterface = getName(line, "interface");
					System.out.println("interface " + nameInterface);
				}
				//Is a field
				else if(line.toLowerCase().contains(":")){
					nameFields.add(getField(line));
					scalarFields.add(getScalarField(line));
				}
				//End of type/interface
				else if(line.contains("}")){
					empieza = false;
					buildType(nameType, nameInterface, interfaces, nameFields, scalarFields);
					interfaces = new ArrayList<>();
					nameFields = new ArrayList<>();
					scalarFields = new ArrayList<>();
					nameType = "";
					nameInterface = "";
				}
			}
			
		}
	 
		br.close();
		// TODO Auto-generated method stub
		MethodSpec main = MethodSpec.methodBuilder("main")
			    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
			    .returns(void.class)
			    .addParameter(String[].class, "args")
			    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
			    .build();

			TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
			    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
			    .addMethod(main)
			    .build();

			JavaFile javaFile = JavaFile.builder("com.howtographql.hackernews", helloWorld)
			    .build();

			javaFile.writeTo(new File(Paths.get("./src/main/java").toAbsolutePath().normalize().toString()));
			
			System.out.println(new File(Paths.get(".\\src\\main\\java").toAbsolutePath().normalize().toString()).toString());
			

	}

}
