package graphql.linalyzer;

import java.util.List;

public interface LinterRule {

    List<LinterRuleResult> check(SchemaDefinition schemaDefinition);

}
