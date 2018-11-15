package graphql.linalyzer.cli.result;

import graphql.linalyzer.LinterRuleResult;

public final class ResultTransformer {
    private ResultTransformer() {
    }

    public static RuleResult transformLinterRuleResult(
            String ruleName, LinterRuleResult linterRuleResult
    ) {
        return RuleResult.builder()
                .setRuleName(ruleName)
                .setSeverity(linterRuleResult.getSeverity().toString())
                .setMessage(linterRuleResult.getMessage())
                .setColumn(linterRuleResult.getSourceLocation().getColumn())
                .setLine(linterRuleResult.getSourceLocation().getLine())
                .build();
    }
}
