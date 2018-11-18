package graphql.linalyzer.cli;

import graphql.linalyzer.cli.test.utils.DockerUtils;
import graphql.linalyzer.cli.test.utils.OutputChecker;
import graphql.linalyzer.cli.test.utils.OutputChecker.Line;
import graphql.linalyzer.cli.test.utils.TestFileUtils;
import graphql.linalyzer.cli.test.utils.yaml.TestConfig;
import graphql.linalyzer.cli.test.utils.yaml.TestRuleOutput;
import graphql.linalyzer.cli.test.utils.yaml.TestSchema;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.linalyzer.cli.test.utils.DockerUtils.CONTAINER_DIR;
import static graphql.linalyzer.cli.test.utils.OutputChecker.fileLine;
import static graphql.linalyzer.cli.test.utils.OutputChecker.ruleLine;
import static graphql.linalyzer.cli.test.utils.OutputChecker.summaryLine;
import static graphql.linalyzer.cli.test.utils.TestFileUtils.createTempFile;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class YamlExecuteRule implements TestRule {

    private final String imageId;

    public YamlExecuteRule(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        final String testFilePath = description.getMethodName() + ".yml";

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

        final TestConfig testConfig = new Yaml(new Constructor(TestConfig.class)).load(stream);

        final File tempDirectory = DockerUtils.createTempDirForVolume();

        final File configFile = createTempFile(testConfig.getConfig(), tempDirectory);

        final Map<File, TestSchema> schemasWithPaths = testConfig.getSchemas().stream()
                .map(testSchema -> new AbstractMap.SimpleEntry<>(
                        TestFileUtils.createTempFile(testSchema.getContent(), tempDirectory),
                        testSchema
                ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        final String output = DockerUtils.executeLinalyzer(imageId, tempDirectory, configFile, schemasWithPaths.keySet());

        final List<Line> lines = schemasWithPaths.entrySet().stream()
                .filter(entry -> entry.getValue().getOutputs() != null)
                .reduce(new ArrayList<>(), (partialLines, entry) -> {
                    final File schemaFile = entry.getKey();
                    final TestSchema testSchema = entry.getValue();

                    partialLines.add(fileLine(CONTAINER_DIR + "/" + schemaFile.getName()));

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
