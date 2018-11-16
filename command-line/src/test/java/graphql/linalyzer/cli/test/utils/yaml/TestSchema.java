package graphql.linalyzer.cli.test.utils.yaml;

import graphql.linalyzer.cli.test.utils.yaml.TestRuleOutput;

import java.util.List;

public class TestSchema {
    private String content;
    private List<TestRuleOutput> outputs;
    private String filePath;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TestRuleOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TestRuleOutput> outputs) {
        this.outputs = outputs;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
