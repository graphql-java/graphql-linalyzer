package graphql.linalyzer.cli.config;

import graphql.linalyzer.cli.utils.FileUtils;

import java.util.List;

import static graphql.linalyzer.cli.config.ConfigParser.parseRules;

public class Configuration {
    private final List<String> schemaFilePaths;
    private final List<RuleConfiguration> ruleConfigurations;

    public List<String> getSchemaFilePaths() {
        return schemaFilePaths;
    }

    public List<RuleConfiguration> getRuleConfigurations() {
        return ruleConfigurations;
    }

    private Configuration(
            List<String> schemaFilePaths, List<RuleConfiguration> ruleConfigurations
    ) {
        this.schemaFilePaths = schemaFilePaths;
        this.ruleConfigurations = ruleConfigurations;
    }

    public static Configuration.Builder builder() {
        return new Configuration.Builder();
    }

    public static final class Builder {
        private List<String> schemaFilePaths;
        private String configFilePath;

        private Builder() {
        }

        public Builder setSchemaFilePaths(List<String> schemaFilePaths) {
            this.schemaFilePaths = schemaFilePaths;
            return this;
        }

        public Builder setConfigFilePath(String configFilePath) {
            this.configFilePath = configFilePath;
            return this;
        }

        public Configuration build() {
            final String configContent = FileUtils.getFileContent(configFilePath);

            final List<RuleConfiguration> ruleConfigurations = parseRules(configContent);

            return new Configuration(schemaFilePaths, ruleConfigurations);

        }
    }
}
