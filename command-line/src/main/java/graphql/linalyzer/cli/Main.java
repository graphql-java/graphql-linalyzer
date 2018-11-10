package graphql.linalyzer.cli;

import graphql.language.AstPrinter;
import graphql.language.Document;
import graphql.parser.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Main {
    static final String defaultConfigFile = "lintalyzer-config.yml";

    private static Object parseYaml(String configFilePath) {
        Yaml yaml = new Yaml();

        try {
            return yaml.load(new FileReader(new File(configFilePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Error reading config file: " + configFilePath, e);
        }
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

        String configFile = commandLine.hasOption("c") ? commandLine.getOptionValue("c") : defaultConfigFile;

        System.out.println(parseYaml(configFile));

        List<String> schemaFiles = commandLine.getArgList();

        System.out.println("Validating file: " + schemaFiles);


        String schema = "type Query1{foo:String}";
        Document document = new Parser().parseDocument(schema);
        System.out.println(AstPrinter.printAst(document));
    }
}
