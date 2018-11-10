package graphql.linalyzer;

import graphql.language.Document;
import graphql.language.NamedNode;
import graphql.language.NodeTraverser;

import java.util.List;

public class SchemaDefinition {

//    private final TypeDefinitionRegistry typeDefinitionRegistry;

    public SchemaDefinition(Document document) {
//        SchemaParser schemaParser = new SchemaParser();
//        this.typeDefinitionRegistry = schemaParser.buildRegistry(document);
        this.document = document;
    }

    private Document document;

    public List<NamedNode> getAllNodes(List<SchemaDefinitionElement> schemaDefinitionElements) {
        NodeTraverser nodeTraverser = new NodeTraverser();
        CollectElementsVisitor collectElementsVisitor = new CollectElementsVisitor(schemaDefinitionElements);
        nodeTraverser.preOrder(collectElementsVisitor, document);
        return collectElementsVisitor.getResult();
    }

}
