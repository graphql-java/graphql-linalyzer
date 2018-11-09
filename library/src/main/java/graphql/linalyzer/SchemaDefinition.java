package graphql.linalyzer;

import graphql.language.ObjectTypeDefinition;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.Map;

public class SchemaDefinition {

    public SchemaDefinition(TypeDefinitionRegistry typeDefinitionRegistry) {
        this.typeDefinitionRegistry = typeDefinitionRegistry;
    }

    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public Map<String, ObjectTypeDefinition> getObjectTypeDefinitions() {
//       return typeDefinitionRegistry.types().entrySet().stream().filter(stringTypeDefinitionEntry -> {
//           return stringTypeDefinitionEntry.getValue() instanceof ObjectTypeDefinition;
//       }).collect(Collectors.toMap());
        return null;
    }

}
