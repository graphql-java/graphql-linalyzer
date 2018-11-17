package graphql.linalyzer.cli;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class CliYamlTests {

    @ClassRule
    public static final DockerImageRule dockerImageRule = new DockerImageRule();

    @Rule
    public final YamlExecuteRule yamlExecuteRule = new YamlExecuteRule(dockerImageRule.getImageId());

    @Test
    public void testCamelCase() {
    }

    @Test
    public void testNoTabs() {
    }
}
