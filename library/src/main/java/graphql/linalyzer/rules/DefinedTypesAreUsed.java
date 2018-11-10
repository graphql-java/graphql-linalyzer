package graphql.linalyzer.rules;

import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;

import java.util.List;

public class DefinedTypesAreUsed implements LinterRule {
    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        return null;
    }
}
