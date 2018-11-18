package graphql.linalyzer.cli.test.utils.yaml;

import java.util.List;

public class TestConfig {
    private String config;
    private List<TestSchema> schemas;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<TestSchema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<TestSchema> schemas) {
        this.schemas = schemas;
    }
}
