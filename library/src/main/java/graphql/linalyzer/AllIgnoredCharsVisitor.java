package graphql.linalyzer;

import graphql.language.IgnoredChar;
import graphql.language.Node;
import graphql.language.NodeVisitorStub;
import graphql.util.TraversalControl;
import graphql.util.TraverserContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AllIgnoredCharsVisitor extends NodeVisitorStub {

    private final Map<IgnoredChar, Node> result = new LinkedHashMap<>();

    public AllIgnoredCharsVisitor() {
    }

    @Override
    protected TraversalControl visitNode(Node node, TraverserContext<Node> context) {
        Stream.concat(node.getIgnoredChars().getLeft().stream(), node.getIgnoredChars().getRight().stream())
                .forEach(ignoredChar -> {
                    result.putIfAbsent(ignoredChar, node);
                });
        return super.visitNode(node, context);
    }

    public Map<IgnoredChar, Node> getResult() {
        return result;
    }
}
