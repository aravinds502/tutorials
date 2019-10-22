package com.lab.graphqljava;

import graphql.schema.GraphQLObjectType;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.Scalars.GraphQLString;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLBigInteger;

public class AddressDefinition {
	
	public static GraphQLObjectType getSchema() {
		GraphQLObjectType address = newObject()
		        .name("Address")
		        .field(newFieldDefinition()
		                .name("flatno")
		                .type(GraphQLInt))
		        .field(newFieldDefinition()
		                .name("street")
		                .type(GraphQLString))
		        .field(newFieldDefinition()
		                .name("city")
		                .type(GraphQLString))
		        .field(newFieldDefinition()
		                .name("zipcode")
		                .type(GraphQLBigInteger))
		        .build();
		
		return address;
	}

}
