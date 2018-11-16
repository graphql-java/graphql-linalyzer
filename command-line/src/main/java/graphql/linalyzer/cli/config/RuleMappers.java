package graphql.linalyzer.cli.config;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.rules.NamingRule;
import graphql.linalyzer.rules.NoTabsAllowedRule;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

final class RuleMappers {
    private RuleMappers() {
    }

    private static final Map<String, LinterRuleCreator> ruleCreatorsMap = new HashMap<>();

    static {
        ruleCreatorsMap.put("camelCase", ruleConfiguration -> new NamingRule(
                "camelCaseRuleID",
                singletonList(SchemaDefinitionElement.FIELD),
                NamingRule.CAMEL_CASE, ruleConfiguration.getSeverity()
        ));

        ruleCreatorsMap.put("noTabs", ruleConfiguration -> new NoTabsAllowedRule(
                "noTabsRuleID",
                ruleConfiguration.getSeverity()
        ));
    }

    static LinterRule getRule(RuleConfiguration ruleConfiguration) {
        final LinterRuleCreator linterRuleCreator = ruleCreatorsMap.get(ruleConfiguration.getName());

        if (linterRuleCreator == null) {
            throw new IllegalArgumentException("No rule found with name " + ruleConfiguration.getName());
        }

        return linterRuleCreator.create(ruleConfiguration);
    }
}
