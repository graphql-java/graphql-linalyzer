package graphql.linalyzer.cli.config;

import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public final class ConfigParser {
    private ConfigParser() {
    }

    public static Configuration parse(String configContent) {
        Yaml yaml = new Yaml();

        Map<String, List<Map<String, String>>> configurationMap = yaml.load(configContent);

        List<RuleConfiguration> ruleConfigurations = configurationMap.get("rules").stream()
                .map(r -> {
                    RuleConfiguration ruleConfiguration = new RuleConfiguration();
                    ruleConfiguration.setName(r.get("name"));
                    ruleConfiguration.setSeverity(r.get("severity"));

                    return ruleConfiguration;
                }).collect(toList());

        Configuration configuration = new Configuration();

        configuration.setRuleConfigurations(ruleConfigurations);

        return configuration;
    }
}
