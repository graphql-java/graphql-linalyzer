package graphql.linalyzer.cli;

import java.util.List;

public class Configuration {
    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
