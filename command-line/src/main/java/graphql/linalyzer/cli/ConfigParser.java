package graphql.linalyzer.cli;

import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ConfigParser {
    public Configuration parse(String configContent) {
        Yaml yaml = new Yaml();

        Map<String, List<Map<String, String>>> configurationMap = yaml.load(configContent);

        List<Rule> rules = configurationMap.get("rules").stream()
                .map(r -> {
                    Rule rule = new Rule();
                    rule.setName(r.get("name"));
                    rule.setSeverity(r.get("severity"));

                    return rule;
                }).collect(toList());

        Configuration configuration = new Configuration();

        configuration.setRules(rules);

        return configuration;
    }
}
