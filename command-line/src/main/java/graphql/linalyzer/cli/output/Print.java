package graphql.linalyzer.cli.output;

import graphql.linalyzer.Severity;
import graphql.linalyzer.cli.result.FileResult;
import graphql.linalyzer.cli.result.RuleResult;

import static graphql.linalyzer.cli.output.Styled.bold;
import static graphql.linalyzer.cli.output.Styled.red;
import static graphql.linalyzer.cli.output.Styled.underlined;
import static graphql.linalyzer.cli.output.Styled.white;
import static graphql.linalyzer.cli.output.Styled.yellow;
import static java.util.stream.Collectors.joining;

public class Print {
    public static String printFileResult(FileResult fileResult) {
        final String filePath = fileResult.getFilePath();

        final String printedRuleResults = fileResult.getRuleResults().stream()
                .map(Print::printRuleResult)
                .collect(joining("\n"));

        return String.format(
                "%s\n%s",
                underlined(filePath),
                printedRuleResults
        );
    }

    private static String printRuleResult(RuleResult ruleResult) {
        return String.format(
                "\t%s\t%-20s%-40s%s",
                white(String.format("%s:%s", ruleResult.getLine(), ruleResult.getColumn())),
                printSeverity(ruleResult.getSeverity()),
                bold(ruleResult.getMessage()),
                white(ruleResult.getRuleName())
        );
    }

    private static String printSeverity(Severity severity) {
        switch (severity) {
            case ERROR:
                return red(severity.toString().toLowerCase());
            case WARNING:
                return yellow(severity.toString().toLowerCase());
            default:
                return severity.toString().toLowerCase();
        }
    }
}
