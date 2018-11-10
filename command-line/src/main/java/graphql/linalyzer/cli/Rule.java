package graphql.linalyzer.cli;

import java.util.List;

public class Rule {
    private String name;
    private String severity;
    private List<String> arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", severity='" + severity + '\'' +
                ", arguments=" + arguments +
                '}';
    }
}
