package graphql.linalyzer.cli.config;

import graphql.linalyzer.Severity;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

final class ConfigParser {
    private ConfigParser() {
    }

    static List<RuleConfiguration> parseRules(String configContent) {
        Yaml yaml = new Yaml();

        Map<String, List<Map<String, String>>> configurationMap = yaml.load(configContent);

        return configurationMap.get("rules").stream()
                .map(r -> {
                    RuleConfiguration ruleConfiguration = new RuleConfiguration();
                    ruleConfiguration.setName(r.get("name"));
                    ruleConfiguration.setSeverity(Severity.valueOf(r.get("severity").toUpperCase()));

                    return ruleConfiguration;
                }).collect(toList());
    }
}
