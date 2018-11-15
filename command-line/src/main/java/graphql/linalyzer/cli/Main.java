package graphql.linalyzer.cli;

import graphql.linalyzer.cli.config.Configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;

public class Main {
    private static final String DEFAULT_CONFIG_FILE = "linalyzer-config.yml";

    private Execution execution = new Execution();

    public static void main(String[] args) throws ParseException {
        String result = new Main().run(args);

        System.out.println(result);
    }

    private String run(String[] args) throws ParseException {
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

        final String configFilePath = commandLine.hasOption("c")
                ? commandLine.getOptionValue("c")
                : DEFAULT_CONFIG_FILE;

        final List<String> schemaFilePaths = commandLine.getArgList();

        final Configuration configuration = Configuration.builder()
                .setSchemaFilePaths(schemaFilePaths)
                .setConfigFilePath(configFilePath)
                .build();

        return execution.execute(configuration);
    }

}
