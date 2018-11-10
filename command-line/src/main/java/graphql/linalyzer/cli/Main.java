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

import java.util.List;

public class Main {
    static final String defaultConfigFile = "lintalyzer-config.yml";

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

        System.out.println("Using config file: " + configFile);

        List<String> schemaFiles = commandLine.getArgList();

        System.out.println("Validating file: " + schemaFiles);


        String schema = "type Query1{foo:String}";
        Document document = new Parser().parseDocument(schema);
        System.out.println(AstPrinter.printAst(document));

    }
}
