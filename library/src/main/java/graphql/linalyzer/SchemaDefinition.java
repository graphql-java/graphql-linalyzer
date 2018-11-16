package graphql.linalyzer;

import graphql.language.Document;
import graphql.language.IgnoredChar;
import graphql.language.NamedNode;
import graphql.language.Node;
import graphql.language.NodeTraverser;

import java.util.List;
import java.util.Map;

public class SchemaDefinition {

    private String rawString;
    private Document document;


    public SchemaDefinition(String rawString, Document document) {
        this.rawString = rawString;
        this.document = document;
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

    public String getRawString() {
        return rawString;
    }
}
