package graphql.linalyzer.cli.result;

import java.util.List;

public class FileResult {
    private final String filePath;
    private final List<RuleResult> ruleResults;

    public FileResult(String filePath, List<RuleResult> ruleResults) {
        this.filePath = filePath;
        this.ruleResults = ruleResults;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<RuleResult> getRuleResults() {
        return ruleResults;
    }
}
