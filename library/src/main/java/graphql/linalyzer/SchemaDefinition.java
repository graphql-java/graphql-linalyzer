package graphql.linalyzer;

import graphql.language.Document;
import graphql.language.IgnoredChar;
import graphql.language.NamedNode;
import graphql.language.Node;
import graphql.language.NodeTraverser;

import java.util.List;
import java.util.Map;

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

    public Map<IgnoredChar, Node> getAllIgnoredChars() {
        NodeTraverser nodeTraverser = new NodeTraverser();
        AllIgnoredCharsVisitor allIgnoredCharsVisitor = new AllIgnoredCharsVisitor();
        nodeTraverser.preOrder(allIgnoredCharsVisitor, document);
        return allIgnoredCharsVisitor.getResult();

    }

}
