package graphql.linalyzer.rules

import graphql.language.SourceLocation
import graphql.linalyzer.LinterRuleResult
import graphql.linalyzer.SchemaDefinition
import graphql.linalyzer.Severity
import graphql.parser.Parser
import spock.lang.Specification

class NoTabsAllowedRuleTest extends Specification {


    def "no tabs allowed"() {
        given:
        def sdl = "\ttype Query {\n" +
                "  Name: String\t\n" +
                "\tId: String\n" +
                "}"
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        NoTabsAllowedRule rule = new NoTabsAllowedRule("ruleId", Severity.ERROR)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 3
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(1, 1), "No tab allowed")
        result[1] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(2, 15), "No tab allowed")
        result[2] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(3, 1), "No tab allowed")

    }

}
