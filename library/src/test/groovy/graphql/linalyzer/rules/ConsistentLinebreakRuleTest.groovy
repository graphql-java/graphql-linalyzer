package graphql.linalyzer.rules

import graphql.language.SourceLocation
import graphql.linalyzer.LinterRuleResult
import graphql.linalyzer.SchemaDefinition
import graphql.linalyzer.Severity
import graphql.parser.Parser
import spock.lang.Specification

class ConsistentLinebreakRuleTest extends Specification {

    def "linux linebreak expected"() {
        given:
        def sdl = "type Query {\n\r\n Field: String\r\n }"
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        ConsistentLinebreakRule rule = new ConsistentLinebreakRule("ruleId", Severity.ERROR, ConsistentLinebreakRule.LinebreakType.LINUX)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 2
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(2, 1), "Not allowed linebreak at 2. Expected LINUX style.")
        result[1] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(3, 15), "Not allowed linebreak at 3. Expected LINUX style.")

    }

    def "windows linebreak expected"() {
        given:
        def sdl = "type Query {\n\n Field: String\r\n }"
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        ConsistentLinebreakRule rule = new ConsistentLinebreakRule("ruleId", Severity.ERROR, ConsistentLinebreakRule.LinebreakType.WINDOWS)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 2
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(1, 13), "Not allowed linebreak at 1. Expected WINDOWS style.")
        result[1] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(2, 1), "Not allowed linebreak at 2. Expected WINDOWS style.")

    }
}
