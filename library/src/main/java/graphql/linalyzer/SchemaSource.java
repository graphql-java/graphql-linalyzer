package graphql.linalyzer;

import graphql.schema.idl.TypeDefinitionRegistry;

public interface SchemaSource {

    TypeDefinitionRegistry getSchema();
}
