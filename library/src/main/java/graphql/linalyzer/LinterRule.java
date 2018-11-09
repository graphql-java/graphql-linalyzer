package graphql.linalyzer;

public interface LinterRule {

    LinterRuleResult check(SchemaDefinition schemaDefinition);

}
