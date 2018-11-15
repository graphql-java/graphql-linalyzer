package graphql.linalyzer.cli.test.utils;

import graphql.linalyzer.cli.output.Styled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OutputChecker {
    private final List<Line> expectedOutputLines;

    private static final Map<String, String> SEVERITY_STYLE = new HashMap<>();

    static {
        SEVERITY_STYLE.put("warning", Styled.YELLOW);
        SEVERITY_STYLE.put("error", Styled.RED);
    }

    public OutputChecker() {
        this.expectedOutputLines = new ArrayList<>();
    }

    public OutputChecker expect(Line... lines) {
        expectedOutputLines.addAll(Arrays.asList(lines));

        return this;
    }

    private String ruleLineWithStyle(String location, String severity, String message, String ruleName) {
        final String severityStyle = SEVERITY_STYLE.get(severity);

        return String.format(
                "\t\u001B[37m%s\u001B[0m\t%s%s\u001B[0m\u001B[1m%s\u001B[0m\u001B[37m%s\u001B[0m",
                location,
                severityStyle,
                severity,
                message,
                ruleName
        );
    }

    private String fileLineWithStyle(String filePath) {
        return String.format("\u001B[4m%s\u001B[0m", filePath);
    }

    private String summaryLineWithStyle(int errors, int warnings, int problems) {
        final List<String> problemDetails = new ArrayList<>();

        if(errors > 0) {
            problemDetails.add(String.format("%s error" + (errors > 1 ? "s" : ""), errors));
        }

        if(warnings > 0) {
            problemDetails.add(String.format("%s warning" + (warnings > 1 ? "s" : ""), warnings));
        }

        return String.format(
                "✖ %s %s (%s)",
                problems,
                "problem" + (problems > 1 ? "s" : ""),
                problemDetails.stream().collect(Collectors.joining(", "))
        );
    }

    public void check(String actualOutput) {
        final List<String> actualOutputLines = Stream.of(actualOutput.split("\n"))
                .filter(line -> !line.isEmpty())
                .collect(toList());

        if (actualOutputLines.size() != expectedOutputLines.size()) {
            fail(String.format(
                    "Expected output lines should have the same number of lines than the actual output. " +
                            "Expected is %s, actual is %s",
                    expectedOutputLines.size(),
                    actualOutputLines.size()));
        }

        for (int i = 0; i < actualOutputLines.size(); i++) {
            final String actualOutputLine = actualOutputLines.get(i);
            final Line expectedOutputLine = expectedOutputLines.get(i);

            if (expectedOutputLine instanceof FileLine) {
                final FileLine fileLine = (FileLine) expectedOutputLine;

                assertThat(actualOutputLine, equalTo(fileLineWithStyle(fileLine.filePath)));

            } else if (expectedOutputLine instanceof RuleLine) {
                final RuleLine ruleLine = (RuleLine) expectedOutputLine;

                final String expected =
                        ruleLineWithStyle(
                                ruleLine.location,
                                ruleLine.severity,
                                ruleLine.message,
                                ruleLine.ruleName
                        );

                final String expectedNoWhitespaces = expected.replaceAll(" ", "");
                final String actualOutputLineNoWhitespaces = actualOutputLine.replaceAll(" ", "");

                assertThat(actualOutputLineNoWhitespaces, equalTo(expectedNoWhitespaces));
            } else if (expectedOutputLine instanceof SummaryLine) {
                final SummaryLine summaryLine = (SummaryLine) expectedOutputLine;

                final String expected = summaryLineWithStyle(
                        summaryLine.errors,
                        summaryLine.warnings,
                        summaryLine.problems
                );

                assertThat(actualOutputLine, equalTo(expected));
            }
        }
    }

    public static FileLine fileLine(String filePath) {
        return new FileLine(filePath);
    }

    public static RuleLine ruleLine(String location, String severity, String message, String ruleName) {
        return new RuleLine(location, severity, message, ruleName);
    }

    public static SummaryLine summaryLine(int errors, int warnings) {
        return new SummaryLine(errors, warnings);
    }

    public interface Line {

    }

    private static class FileLine implements Line {
        final String filePath;

        FileLine(String filePath) {
            this.filePath = filePath;
        }
    }

    private static class RuleLine implements Line {
        final String location;
        final String severity;
        final String message;
        final String ruleName;

        RuleLine(String location, String severity, String message, String ruleName) {
            this.location = location;
            this.severity = severity;
            this.message = message;
            this.ruleName = ruleName;
        }
    }

    private static class SummaryLine implements Line {
        final int problems;
        final int warnings;
        final int errors;

        public SummaryLine(int errors, int warnings) {
            this.problems = warnings + errors;
            this.warnings = warnings;
            this.errors = errors;
        }
    }
}
