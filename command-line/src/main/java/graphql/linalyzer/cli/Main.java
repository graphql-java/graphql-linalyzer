package graphql.linalyzer.cli;

import graphql.language.AstPrinter;
import graphql.language.Document;
import graphql.parser.Parser;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "lintalyzer", mixinStandardHelpOptions = true, version = "0.0.1")
public class Main implements Runnable {
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Verbose mode. Helpful for troubleshooting.")
    private boolean verbose;

    @CommandLine.Parameters(arity = "1..*", paramLabel = "FILE", description = "Schema file(s) to process")
    private File[] inputFiles;

    public void run() {
        if (verbose) {
            System.out.println(inputFiles.length + " files to process...");
        }

        for (File f : inputFiles) {
            System.out.println(f.getAbsolutePath());
        }

        String schema = "type Query1{foo:String}";
        Document document = new Parser().parseDocument(schema);
        System.out.println(AstPrinter.printAst(document));
    }

    public static void main(String[] args) {
        CommandLine.run(new Main(), args);
    }
}
