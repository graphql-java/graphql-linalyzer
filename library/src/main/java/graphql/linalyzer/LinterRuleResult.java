package graphql.linalyzer;

import graphql.language.SourceLocation;

import java.util.Objects;

public class LinterRuleResult {

    private final Severity severity;
    private final String ruleId;
    private final SourceLocation sourceLocation;
    private final String message;

    public LinterRuleResult(Severity severity, String ruleId, SourceLocation sourceLocation, String message) {
        this.severity = severity;
        this.ruleId = ruleId;
        this.sourceLocation = sourceLocation;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinterRuleResult that = (LinterRuleResult) o;
        return severity == that.severity &&
                Objects.equals(ruleId, that.ruleId) &&
                Objects.equals(sourceLocation, that.sourceLocation) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, ruleId, sourceLocation, message);
    }

    @Override
    public String toString() {
        return "LinterRuleResult{" +
                "severity=" + severity +
                ", ruleId='" + ruleId + '\'' +
                ", sourceLocation=" + sourceLocation +
                ", message='" + message + '\'' +
                '}';
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getRuleId() {
        return ruleId;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public String getMessage() {
        return message;
    }
}
