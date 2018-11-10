package graphql.linalyzer.cli;

import graphql.language.AstPrinter;
import graphql.language.Document;
import graphql.parser.Parser;

public class Main {

    public static void main(String[] args) {
        String schema = "type Query1{foo:String}";
        Document document = new Parser().parseDocument(schema);
        System.out.println(AstPrinter.printAst(document));
    }

}
