package graphql.linalyzer.cli.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class OutputChecker {
    private final List<Line> expectedOutputLines;

    public OutputChecker() {
        this.expectedOutputLines = new ArrayList<>();
    }

    public OutputChecker expect(Line... lines) {
        expectedOutputLines.addAll(Arrays.asList(lines));

        return this;
    }

    private String ruleLineWithStyle(String location, String severity, String message, String ruleName) {
        return String.format(
                "\t\u001B[37m%s\u001B[0m\t\u001B[33m%s\u001B[0m\u001B[1m%s\u001B[0m\u001B[37m%s\u001B[0m",
                location,
                severity,
                message,
                ruleName
        );
    }

    private String fileLineWithStyle(String filePath) {
        return String.format("\u001B[4m%s\u001B[0m", filePath);
    }

    public void check(String actualOutput) {
        final String[] actualOutputLines = actualOutput.split("\n");

        for (int i = 0; i < Math.min(actualOutputLines.length, expectedOutputLines.size()); i++) {
            final String actualOutputLine = actualOutputLines[i];
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
            }
        }
   }

    public static FileLine fileLine(String filePath) {
        return new FileLine(filePath);
    }

    public static RuleLine ruleLine(String location, String severity, String message, String ruleName) {
        return new RuleLine(location, severity, message, ruleName);
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
}
