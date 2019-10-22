package com.lab.graphqljava;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;


@Component
public class GraphQLProvider {


    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;
    private GraphQLSchema graphQLSchema;
    
    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        this.graphQLSchema = buildSchema(sdl);        
        
        System.out.println("++++Additional Types - Intially++++++");
        graphQLSchema.getAdditionalTypes().forEach(type -> {
        	System.out.println(type.getName());
        });
        
       
        //Adding a new type call 'Greeting'
        /*
        
        GraphQLFieldDefinition field = GraphQLFieldDefinition.newFieldDefinition()
                .name("name")
                .type(Scalars.GraphQLString)
                .build();
        
        String typeName = "Greeting";
        GraphQLObjectType.Builder builder = newObject()
                .name(typeName)
                .fields(List.of(field))
                .description("Just To Test");
                
         */
        
        graphQLSchema =  graphQLSchema.transform(_builder -> {
        					_builder.additionalType(AddressDefinition.getSchema());
        				});      
        
        
        
        System.out.println("+++++++ADDITIONAL TYPES -- After adding++++++++++");
        graphQLSchema.getAdditionalTypes().forEach(type -> {
        	System.out.println(type.getName());
        });
        
        //Now againg delete that additional type
        /*
        graphQLSchema =  graphQLSchema.transform(_builder -> {
			_builder.clearAdditionalTypes();
		});
        
        System.out.println("+++++++ADDITIONAL TYPES -- After clearing++++++++++");
        graphQLSchema.getAdditionalTypes().forEach(type -> {
        	System.out.println(type.getName());
        });
        
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
		*/
        	   
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();       
    }
    
    

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }
    

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("books", graphQLDataFetchers.getBooksDataFetcher()))
                
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

}
