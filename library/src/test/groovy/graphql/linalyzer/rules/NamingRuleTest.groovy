package graphql.linalyzer.rules


import graphql.language.SourceLocation
import graphql.linalyzer.LinterRuleResult
import graphql.linalyzer.SchemaDefinition
import graphql.linalyzer.SchemaDefinitionElement
import graphql.linalyzer.Severity
import graphql.parser.Parser
import spock.lang.Specification

class NamingRuleTest extends Specification {


    def "checks for camel case fields"() {
        given:
        def sdl = """
            type Query {
                Field: String
                id: ID
                allowed: String
            }
        """
        def document = new Parser().parseDocument(sdl)
        SchemaDefinition schemaDefinition = new SchemaDefinition(sdl, document)
        NamingRule namingRuleTest = new NamingRule("ruleId", [SchemaDefinitionElement.FIELD], NamingRule.CAMEL_CASE, Severity.ERROR)
        when:
        def result = namingRuleTest.check(schemaDefinition)

        then:
        result.size() == 1
        result[0] == new LinterRuleResult(Severity.ERROR, "ruleId", new SourceLocation(3, 17), "Not allowed name Field")

    }

}
