package graphql.linalyzer;

import graphql.language.Document;

import java.util.List;
import java.util.stream.Collectors;

public class Linalyzer {

    private final List<LinterRule> rules;

    public Linalyzer(List<LinterRule> rules) {
        this.rules = rules;
    }

    public List<LinterRuleResult> lint(Document document) {
        SchemaDefinition schemaDefinition = new SchemaDefinition(document);
        return rules.stream().flatMap(linterRule -> linterRule.check(schemaDefinition).stream()).collect(Collectors.toList());
    }
}
