package graphql.linalyzer.cli.config;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.rules.AllDefinitionsUsedRule;
import graphql.linalyzer.rules.ConsistentLinebreakRule;
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
        ruleCreatorsMap.put("allDefinitionsUsed", ruleConfiguration -> new AllDefinitionsUsedRule(
                "allDefinitionsUsedID",
                ruleConfiguration.getSeverity()
        ));

        ruleCreatorsMap.put("consistentLinebreak", RuleMappers::newLinebreakRule);
    }

    private static ConsistentLinebreakRule newLinebreakRule(RuleConfiguration ruleConfiguration) {
        String styleValue = ruleConfiguration.getArguments().get("style");
        if (styleValue == null) {
            throw new InvalidConfigException("missing 'style' property for consistentLinebreak rule");
        }
        String style = styleValue.toLowerCase();

        ConsistentLinebreakRule.LinebreakType type;
        switch (style) {
            case "windows":
                type = ConsistentLinebreakRule.LinebreakType.WINDOWS;
                break;
            case "linux":
                type = ConsistentLinebreakRule.LinebreakType.LINUX;
                break;
            default:
                throw new InvalidConfigException("invalid 'style' property for consistentLinebreak rule");

        }
        return new ConsistentLinebreakRule(
                "allDefinitionsUsedID",
                ruleConfiguration.getSeverity(),
                type
        );
    }

    static LinterRule getRule(RuleConfiguration ruleConfiguration) {
        final LinterRuleCreator linterRuleCreator = ruleCreatorsMap.get(ruleConfiguration.getName());

        if (linterRuleCreator == null) {
            throw new IllegalArgumentException("No rule found with name " + ruleConfiguration.getName());
        }

        return linterRuleCreator.create(ruleConfiguration);
    }
}
