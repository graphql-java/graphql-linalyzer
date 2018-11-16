package graphql.linalyzer.rules;

import graphql.language.SourceLocation;
import graphql.linalyzer.LinterRule;
import graphql.linalyzer.LinterRuleResult;
import graphql.linalyzer.SchemaDefinition;
import graphql.linalyzer.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsistentLinebreakRule implements LinterRule {

    public enum LinebreakType {
        WINDOWS, LINUX
    }

    private final String ruleId;
    private final Severity severity;
    private final LinebreakType lineBreakType;

    public ConsistentLinebreakRule(String ruleId, Severity severity, LinebreakType lineBreakType) {
        this.ruleId = ruleId;
        this.severity = severity;
        this.lineBreakType = lineBreakType;
    }

    @Override
    public List<LinterRuleResult> check(SchemaDefinition schemaDefinition) {
        Pattern pattern = Pattern.compile("\\r?\\n");
        Matcher matcher = pattern.matcher(schemaDefinition.getRawString());
        String expectedLinebreak = expectedLinebreak();
        List<LinterRuleResult> result = new ArrayList<>();
        int lineCount = 1;
        int lastLineEnds = 0;
        while (matcher.find()) {
            String linebreak = matcher.group();
            if (!linebreak.equals(expectedLinebreak)) {
                int linebreakIndexAtCurLine = matcher.start() - lastLineEnds;
                result.add(createResult(lineCount, linebreakIndexAtCurLine + 1));
            }
            lastLineEnds = matcher.end();
            lineCount++;
        }
        return result;
    }

    private String expectedLinebreak() {
        if (lineBreakType == LinebreakType.WINDOWS) {
            return "\r\n";
        } else {
            return "\n";
        }
    }

    private LinterRuleResult createResult(int lineNumber, int column) {
        return new LinterRuleResult(severity, ruleId,
                new SourceLocation(lineNumber, column),
                "Not allowed linebreak at " + lineNumber + ". Expected " + lineBreakType + " style.");
    }


}
