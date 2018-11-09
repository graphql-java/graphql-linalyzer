package graphql.linalyzer.rules;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;

import java.util.regex.Pattern;

public class NamingRule implements LinterRule {

    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([a-z]+[A-Z]+\\\\w+)+");

    @Override
    public LinterRuleResult check(SchemaDefinition schemaDefinition) {
        return null;
    }
}
