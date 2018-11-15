package graphql.linalyzer.cli.config;

import java.util.List;

public class Configuration {
    private List<RuleConfiguration> ruleConfigurations;

    public List<RuleConfiguration> getRuleConfigurations() {
        return ruleConfigurations;
    }

    public void setRuleConfigurations(List<RuleConfiguration> ruleConfigurations) {
        this.ruleConfigurations = ruleConfigurations;
    }
}
