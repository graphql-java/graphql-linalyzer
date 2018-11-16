package graphql.linalyzer.rules;

import graphql.language.SourceLocation;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.Severity;

import java.util.ArrayList;
import java.util.List;

public class MaxLineLengthRule implements LinterRule {

    private final String ruleId;
    private final Severity severity;
    private int maxLineLength;

    public MaxLineLengthRule(String ruleId,
                             Severity severity,
                             int maxLineLength) {
        this.maxLineLength = maxLineLength;
        this.ruleId = ruleId;
        this.severity = severity;
    }


    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        String[] lines = schemaDefinition.getRawString().split("[\\r\\n]+");
        List<LinterRuleResult> result = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() <= maxLineLength) {
                continue;
            }
            result.add(createResult(i + 1));
        }
        return result;

    }

    private LinterRuleResult createResult(int lineNumber) {
        return new LinterRuleResult(severity, ruleId, new SourceLocation(lineNumber, 1), "Line " + lineNumber + " to long.");
    }


}
