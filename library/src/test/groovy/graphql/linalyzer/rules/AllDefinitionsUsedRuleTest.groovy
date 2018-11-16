package graphql.linalyzer.rules

import graphql.language.SourceLocation
import graphql.linalyzer.LinterRuleResult
import graphql.linalyzer.SchemaDefinition
import graphql.linalyzer.Severity
import graphql.parser.Parser
import spock.lang.Specification

class AllDefinitionsUsedRuleTest extends Specification {

    def "unused object definition"() {
        given:
        def sdl = """
            type Query {
                used: Used
            }
            type NotUsed {
                id: ID
            }
            type Used {
                id: ID
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        AllDefinitionsUsedRule rule = new AllDefinitionsUsedRule("ruleId", Severity.ERROR)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 1
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(5, 13), "Unused definition NotUsed.")
    }

    def "unused object with explicit schema definition"() {
        given:
        def sdl = """
            schema {
                query: TopLevel
            }
            type TopLevel {
                used: Used
            }
            type NotUsed {
                id: ID
            }
            type Used {
                id: ID
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        AllDefinitionsUsedRule rule = new AllDefinitionsUsedRule("ruleId", Severity.ERROR)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 1
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(8, 13), "Unused definition NotUsed.")
    }

    def "unused object and interface and enum"() {
        given:
        def sdl = """
            type Query {
                used: Used
            }
            type NotUsed {
                id: ID
            }
            type Used {
                id: ID
            }
            interface NotUsedInterface {
                id: ID
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        AllDefinitionsUsedRule rule = new AllDefinitionsUsedRule("ruleId", Severity.ERROR)
        when:
        def result = rule.check(schemaDefinition)

        then:
        result.size() == 2
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(5, 13), "Unused definition NotUsed.")
        result[1] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(11, 13), "Unused definition NotUsedInterface.")
    }

}
