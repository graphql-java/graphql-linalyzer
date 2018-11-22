package graphql.linalyzer.cli.config;

import graphql.linalyzer.LinterRule;

public interface LinterRuleCreator {

    LinterRule create(RuleConfiguration ruleConfiguration);
}
