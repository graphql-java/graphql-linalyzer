package graphql.linalyzer.cli.output;

import graphql.linalyzer.Severity;
import graphql.linalyzer.cli.result.FileResult;
import graphql.linalyzer.cli.result.RuleResult;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.linalyzer.cli.output.Styled.bold;
import static graphql.linalyzer.cli.output.Styled.red;
import static graphql.linalyzer.cli.output.Styled.underlined;
import static graphql.linalyzer.cli.output.Styled.white;
import static graphql.linalyzer.cli.output.Styled.yellow;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

public class Print {
    public static String printExecutionResult(List<FileResult> filesResults) {
        final String printedFileResults = filesResults.stream()
                .map(Print::printFileResult).collect(joining("\n\n"));

        final Map<Severity, Long> problemCountMap = filesResults.stream()
                .flatMap(fileResult -> fileResult.getRuleResults().stream())
                .collect(groupingBy(RuleResult::getSeverity, Collectors.counting()));

        final String printedSummary = printSummary(problemCountMap);

        return String.format(
                "%s\n\n%s",
                printedFileResults,
                printedSummary
        );
    }

    private static String printSummary(Map<Severity, Long> problemCountMap) {
        final Long totalProblems = problemCountMap.values().stream().mapToLong(Long::longValue).sum();

        final String printedProblemDetails = problemCountMap.entrySet().stream()
                .map(entry ->
                        String.format(
                                "%s %s",
                                entry.getValue(),
                                (entry.getKey().toString().toLowerCase() + (entry.getValue() > 1 ? "s" : ""))
                        ))
                .collect(joining(", "));

        final String problemOrProblems = "problem" + (totalProblems > 1 ? "s" : "");

        return String.format(
                "✖ %s %s (%s)",
                totalProblems,
                problemOrProblems,
                printedProblemDetails
        );
    }

    private static String printFileResult(FileResult fileResult) {
        final String filePath = fileResult.getFilePath();

        final String printedRuleResults = fileResult.getRuleResults().stream()
                .sorted(comparingInt(RuleResult::getLine))
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
