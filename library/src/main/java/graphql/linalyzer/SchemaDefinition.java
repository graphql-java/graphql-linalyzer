package graphql.linalyzer;

import graphql.language.Document;
import graphql.language.IgnoredChar;
import graphql.language.NamedNode;
import graphql.language.Node;
import graphql.language.NodeTraverser;
import graphql.language.ObjectTypeDefinition;
import graphql.language.OperationTypeDefinition;
import graphql.language.TypeDefinition;
import graphql.language.TypeName;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SchemaDefinition {

    private final TypeDefinitionRegistry typeRegistry;
    private String rawString;
    private Document document;


    public SchemaDefinition(String rawString, Document document) {
        this.rawString = rawString;
        this.document = document;
        this.typeRegistry = new SchemaParser().buildRegistry(document);
    }

    public List<NamedNode> getAllNodes(List<SchemaDefinitionElement> schemaDefinitionElements) {
        NodeTraverser nodeTraverser = new NodeTraverser();
        CollectElementsVisitor collectElementsVisitor = new CollectElementsVisitor(schemaDefinitionElements);
        nodeTraverser.preOrder(collectElementsVisitor, document);
        return collectElementsVisitor.getResult();
    }

    public Map<IgnoredChar, Node> getAllIgnoredChars() {
        NodeTraverser nodeTraverser = new NodeTraverser();
        AllIgnoredCharsVisitor allIgnoredCharsVisitor = new AllIgnoredCharsVisitor();
        nodeTraverser.preOrder(allIgnoredCharsVisitor, document);
        return allIgnoredCharsVisitor.getResult();
    }


    public ObjectTypeDefinition getQueryDefinition() {
        if (!typeRegistry.schemaDefinition().isPresent()) {
            return (ObjectTypeDefinition) typeRegistry.getType("Query").get();
        }
        graphql.language.SchemaDefinition schemaDefinition = typeRegistry.schemaDefinition().get();
        List<OperationTypeDefinition> operationTypes = schemaDefinition.getOperationTypeDefinitions();
        OperationTypeDefinition queryOp = operationTypes.stream().filter(op -> "query".equals(op.getName())).findFirst().get();
        TypeName typeName = (TypeName) queryOp.getType();
        return (ObjectTypeDefinition) typeRegistry.getType(typeName.getName()).get();
    }

    public Optional<ObjectTypeDefinition> getMutationDefinition() {
        if (!typeRegistry.schemaDefinition().isPresent()) {
            Optional<TypeDefinition> mutationTypeDef = typeRegistry.getType("Mutation");
            return mutationTypeDef.map(ObjectTypeDefinition.class::cast);
        }
        graphql.language.SchemaDefinition schemaDefinition = typeRegistry.schemaDefinition().get();
        List<OperationTypeDefinition> operationTypes = schemaDefinition.getOperationTypeDefinitions();
        Optional<OperationTypeDefinition> mutationOp = operationTypes.stream().filter(op -> "mutation".equals(op.getName())).findFirst();
        if (!mutationOp.isPresent()) {
            return Optional.empty();
        }
        TypeName typeName = (TypeName) mutationOp.get().getType();
        return typeRegistry.getType(typeName.getName()).map(ObjectTypeDefinition.class::cast);
    }

    public Optional<ObjectTypeDefinition> getSubscriptionDefinition() {
        if (!typeRegistry.schemaDefinition().isPresent()) {
            Optional<TypeDefinition> subscriptionTypeDef = typeRegistry.getType("Subscription");
            return subscriptionTypeDef.map(ObjectTypeDefinition.class::cast);
        }
        graphql.language.SchemaDefinition schemaDefinition = typeRegistry.schemaDefinition().get();
        List<OperationTypeDefinition> operationTypes = schemaDefinition.getOperationTypeDefinitions();
        Optional<OperationTypeDefinition> subscriptionOp = operationTypes.stream().filter(op -> "subscription".equals(op.getName())).findFirst();
        if (!subscriptionOp.isPresent()) {
            return Optional.empty();
        }
        TypeName typeName = (TypeName) subscriptionOp.get().getType();
        return typeRegistry.getType(typeName.getName()).map(ObjectTypeDefinition.class::cast);
    }

    public String getRawString() {
        return rawString;
    }
}
