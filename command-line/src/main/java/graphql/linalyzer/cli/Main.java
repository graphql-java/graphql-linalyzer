package graphql.linalyzer.cli;

import graphql.language.Document;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.cli.config.ConfigParser;
import graphql.linalyzer.cli.config.ConfigTransformer;
import graphql.linalyzer.cli.config.Configuration;
import graphql.linalyzer.cli.output.Print;
import graphql.linalyzer.cli.result.FileResult;
import graphql.linalyzer.cli.result.RuleResult;
import graphql.parser.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static graphql.linalyzer.cli.result.ResultTransformer.transformLinterRuleResult;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "linalyzer-config.yml";

    private static String getFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file: " + filePath, e);
        }
    }

    private static SchemaDefinition toSchemaDefinition(String schemaFilePath) {
        Document document = new Parser().parseDocument(getFileContent(schemaFilePath));

        return new SchemaDefinition(document);
    }

    private static FileResult analyzeSchemaFile(String filePath, Map<String, LinterRule> rules) {
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

    public static void main(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();

        Options options = new Options();
        options.addOption(
                Option.builder("c")
                        .longOpt("config")
                        .desc("Path to the config file")
                        .hasArg()
                        .build()
        );

        CommandLine commandLine = parser.parse(options, args);

        String configFile = commandLine.hasOption("c") ? commandLine.getOptionValue("c") : DEFAULT_CONFIG_FILE;

        List<String> schemaFiles = commandLine.getArgList();

        String configContent = getFileContent(configFile);

        Configuration configuration = ConfigParser.parse(configContent);

        Map<String, LinterRule> rules = ConfigTransformer.transformRuleConfigurations(configuration.getRuleConfigurations());

        final String results = schemaFiles.stream()
                .map(filePath -> analyzeSchemaFile(filePath, rules))
                .map(Print::printFileResult)
                .collect(joining("\n"));

        System.out.println(results);
    }
}
