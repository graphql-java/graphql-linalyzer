package graphql.linalyzer.cli;

import graphql.language.Document;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
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

import static java.util.stream.Collectors.toList;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "lintalyzer-config.yml";

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

        Configuration configuration = new ConfigParser().parse(configContent);
        List<LinterRule> rules = new RuleCreator().createRules(configuration.getRules());

        List<LinterRuleResult> ruleResults = schemaFiles.stream()
                .map(Main::toSchemaDefinition)
                .flatMap(schemaDefinition -> rules.stream()
                        .flatMap(rule -> rule.check(schemaDefinition).stream()))
                .collect(toList());

        System.out.println("Result: " + ruleResults);
    }
}
