package graphql.linalyzer;

import graphql.language.Document;
import graphql.parser.Parser;

import java.util.List;
import java.util.stream.Collectors;

public class Linalyzer {

    private final List<LinterRule> rules;

    public Linalyzer(List<LinterRule> rules) {
        this.rules = rules;
    }

    public List<LinterRuleResult> lint(String document) {
        return lint(document, new Parser().parseDocument(document));
    }

    public List<LinterRuleResult> lint(String raw, Document document) {
        SchemaDefinition schemaDefinition = new SchemaDefinition(raw, document);
        return rules.stream().flatMap(linterRule -> linterRule.check(schemaDefinition).stream()).collect(Collectors.toList());
    }
}
