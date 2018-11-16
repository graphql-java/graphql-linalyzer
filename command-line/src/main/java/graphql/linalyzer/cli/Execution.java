package graphql.linalyzer.cli;

import graphql.language.Document;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.cli.config.ConfigTransformer;
import graphql.linalyzer.cli.config.Configuration;
import graphql.linalyzer.cli.output.Print;
import graphql.linalyzer.cli.result.FileResult;
import graphql.linalyzer.cli.result.RuleResult;
import graphql.linalyzer.cli.utils.FileUtils;
import graphql.parser.Parser;

import java.util.List;
import java.util.Map;

import static graphql.linalyzer.cli.result.ResultTransformer.transformLinterRuleResult;
import static java.util.stream.Collectors.toList;

public class Execution {
    public String execute(String configFilePath, List<String> schemaFilePaths) {
        final Configuration configuration = Configuration.builder()
                .setSchemaFilePaths(schemaFilePaths)
                .setConfigFilePath(configFilePath)
                .build();

        Map<String, LinterRule> rules = ConfigTransformer.transformRuleConfigurations(configuration.getRuleConfigurations());

        final List<FileResult> fileResults = configuration.getSchemaFilePaths().stream()
                .map(filePath -> analyzeSchemaFile(filePath, rules))
                .collect(toList());

        return Print.printExecutionResult(fileResults);
    }

    private FileResult analyzeSchemaFile(String filePath, Map<String, LinterRule> rules) {
        final SchemaDefinition schemaDefinition = toSchemaDefinition(filePath);


        final List<RuleResult> ruleResults = rules.entrySet().stream()
                .flatMap(entry -> {
                    final String ruleName = entry.getKey();
                    final LinterRule linterRule = entry.getValue();

                    return linterRule.check(schemaDefinition).stream()
                            .map(linterRuleResult -> transformLinterRuleResult(ruleName, linterRuleResult));
                })
                .collect(toList());

        return new FileResult(filePath, ruleResults);
    }

    private SchemaDefinition toSchemaDefinition(String schemaFilePath) {
        String fileContent = FileUtils.getFileContent(schemaFilePath);
        Document document = new Parser().parseDocument(fileContent);

        return new SchemaDefinition(fileContent, document);
    }

}
