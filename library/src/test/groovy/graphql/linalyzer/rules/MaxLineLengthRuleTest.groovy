package graphql.linalyzer.rules

import graphql.language.SourceLocation
import graphql.linalyzer.LinterRuleResult
import graphql.linalyzer.SchemaDefinition
import graphql.linalyzer.Severity
import graphql.parser.Parser
import spock.lang.Specification

class MaxLineLengthRuleTest extends Specification {


    def "max line length"() {
        given:
        def sdl = """
            type Query {
                Field: String # to long
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        MaxLineLengthRule rule = new MaxLineLengthRule("ruleId", Severity.ERROR, 30)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 1
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(3, 1), "Line 3 to long.")

    }

}
