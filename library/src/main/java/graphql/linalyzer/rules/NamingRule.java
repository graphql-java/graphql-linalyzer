package graphql.linalyzer.rules;

import graphql.language.NamedNode;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.Severity;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NamingRule implements LinterRule {

    private String ruleId;
    private final List<SchemaDefinitionElement> elementsToCheck;
    private final Pattern pattern;
    private final Severity severity;

    public static final Pattern CAMEL_CASE = Pattern.compile("[a-z][a-zA-Z0-9]*");
    public static final Pattern START_UPPER_CASE = Pattern.compile("[A-Z][_0-9A-Za-z]*");

    public NamingRule(String ruleId,
                      List<SchemaDefinitionElement> elementsToCheck,
                      Pattern pattern,
                      Severity severity) {
        this.ruleId = ruleId;
        this.elementsToCheck = elementsToCheck;
        this.pattern = pattern;
        this.severity = severity;
    }

    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        List<NamedNode> allNodes = schemaDefinition.getAllNodes(elementsToCheck);
        return allNodes
                .stream()
                .filter(namedNode -> (!pattern.matcher(namedNode.getName()).matches()))
                .map(this::createRuleResult)
                .collect(Collectors.toList());

    }

    private LinterRuleResult createRuleResult(NamedNode namedNode) {
        return new LinterRuleResult(severity, ruleId, namedNode.getSourceLocation(), "Not allowed name " + namedNode.getName());
    }
}
