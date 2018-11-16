package graphql.linalyzer.cli;

import graphql.linalyzer.cli.test.utils.OutputChecker;
import graphql.linalyzer.cli.test.utils.OutputChecker.Line;
import graphql.linalyzer.cli.test.utils.yaml.TestConfig;
import graphql.linalyzer.cli.test.utils.TestFileUtils;
import graphql.linalyzer.cli.test.utils.yaml.TestRuleOutput;
import graphql.linalyzer.cli.test.utils.yaml.TestSchema;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.linalyzer.cli.test.utils.OutputChecker.fileLine;
import static graphql.linalyzer.cli.test.utils.OutputChecker.ruleLine;
import static graphql.linalyzer.cli.test.utils.OutputChecker.summaryLine;
import static graphql.linalyzer.cli.test.utils.TestFileUtils.createTempFile;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class YamlExecuteRule implements MethodRule {

    @Override
    public Statement apply(
            Statement base, FrameworkMethod method, Object target
    ) {

        final String testFilePath = method.getName() + ".yml";

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                execute(testFilePath);

                base.evaluate();
            }
        };
    }

    private void execute(String testFilePath) {
        final InputStream stream = YamlExecuteRule.class.getResourceAsStream(testFilePath);

        final TestConfig testConfig = new org.yaml.snakeyaml.Yaml(new Constructor(TestConfig.class)).load(stream);

        final String configFilePath = createTempFile(testConfig.getConfig());

        final Map<String, TestSchema> schemasWithPaths = testConfig.getSchemas().stream()
                .map(testSchema -> new AbstractMap.SimpleEntry<>(
                        TestFileUtils.createTempFile(testSchema.getContent()),
                        testSchema
                ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        final String output = new Execution().execute(configFilePath, new ArrayList<>(schemasWithPaths.keySet()));

        final List<Line> lines = schemasWithPaths.entrySet().stream()
                .filter(entry -> entry.getValue().getOutputs() != null)
                .reduce(new ArrayList<>(), (partialLines, entry) -> {
                    final String schemaPath = entry.getKey();
                    final TestSchema testSchema = entry.getValue();

                    partialLines.add(fileLine(schemaPath));

                    partialLines.addAll(getRuleLines(testSchema));

                    return partialLines;

                }, (partialLines, partialLines2) -> {
                    partialLines.addAll(partialLines2);

                    return partialLines;
                });

        lines.add(getSummaryLine(testConfig.getSchemas()));

        new OutputChecker()
                .expect(lines)
                .check(output);
    }

    private Line getSummaryLine(List<TestSchema> testSchemas) {
        final Map<String, Long> severityCount = testSchemas.stream()
                .filter(testSchema -> testSchema.getOutputs() != null)
                .flatMap(testSchema -> testSchema.getOutputs().stream())
                .collect(groupingBy(TestRuleOutput::getSeverity, Collectors.counting()));

        return summaryLine(
                severityCount.getOrDefault("error", 0L).intValue(),
                severityCount.getOrDefault("warning", 0L).intValue()
        );
    }

    private List<OutputChecker.RuleLine> getRuleLines(TestSchema testSchema) {
        return testSchema.getOutputs().stream()
                .map(testRuleOutput -> ruleLine(
                        testRuleOutput.getLocation(),
                        testRuleOutput.getSeverity(),
                        testRuleOutput.getMessage(),
                        testRuleOutput.getRuleName())
                )
                .collect(toList());
    }

}
