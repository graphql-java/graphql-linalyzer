package graphql.linalyzer.cli.config;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.Severity;
import graphql.linalyzer.rules.NamingRule;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public final class ConfigTransformer {
    private ConfigTransformer() {
    }

    public static Map<String, LinterRule> transformRuleConfigurations(List<RuleConfiguration> ruleConfigurations) {
        return ruleConfigurations.stream()
                .map(ruleConfiguration -> {
                    final String ruleName = ruleConfiguration.getName();

                    LinterRule linterRule = null;

                    if (ruleName.equals("camelCase")) {
                        linterRule = new NamingRule(
                                "ruleId",
                                Arrays.asList(SchemaDefinitionElement.FIELD),
                                NamingRule.CAMEL_CASE, Severity.valueOf(ruleConfiguration.getSeverity().toUpperCase())
                        );
                    }

                    return new AbstractMap.SimpleEntry<>(ruleName, linterRule);

                })
                .filter(entry -> entry.getValue() != null)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
