package graphql.linalyzer.cli;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinitionElement;
import graphql.linalyzer.Severity;
import graphql.linalyzer.rules.NamingRule;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class RuleCreator {
    public List<LinterRule> createRules(List<Rule> rules) {
        return rules.stream().map(rule -> {

            if (rule.getName().equals("camelCase")) {

                return new NamingRule(
                        "ruleId",
                        Arrays.asList(SchemaDefinitionElement.FIELD),
                        NamingRule.CAMEL_CASE, Severity.valueOf(rule.getSeverity().toUpperCase())
                );

            }

            return null;

        }).filter(Objects::nonNull).collect(toList());
    }
}
