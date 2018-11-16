package graphql.linalyzer.rules;

import graphql.language.IgnoredChar;
import graphql.language.Node;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.Severity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NoTabsAllowedRule implements LinterRule {

    private String ruleId;
    private final Severity severity;


    public NoTabsAllowedRule(String ruleId,
                             Severity severity) {
        this.ruleId = ruleId;
        this.severity = severity;
    }

    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        Map<IgnoredChar, Node> allNodes = schemaDefinition.getAllIgnoredChars();
        return allNodes
                .entrySet()
                .stream()
                .filter(this::isTabChar)
                .map(entry -> createRuleResult(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private boolean isTabChar(Map.Entry<IgnoredChar, Node> entry) {
        return entry.getKey().getKind() == IgnoredChar.IgnoredCharKind.TAB;
    }

    private LinterRuleResult createRuleResult(IgnoredChar ignoredChar, Node node) {
        return new LinterRuleResult(severity, ruleId, node.getSourceLocation(), "No" +
                " tab allowed");
    }
}
