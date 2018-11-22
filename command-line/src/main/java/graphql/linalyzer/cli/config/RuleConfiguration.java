package graphql.linalyzer.cli.config;

import graphql.linalyzer.Severity;

import java.util.Map;

public class RuleConfiguration {
    private String name;
    private Severity severity;
    private Map<String, String> arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "RuleConfiguration{" +
                "name='" + name + '\'' +
                ", severity='" + severity + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
