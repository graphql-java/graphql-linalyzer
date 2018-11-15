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
        def sdl = """
            type Query {
            \tfoo:Bar
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(document)
        NoTabsAllowedRule rule = new NoTabsAllowedRule("ruleId", Severity.ERROR)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 1
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(3, 14), "No tab allowed")

    }

}
