package graphql.linalyzer.cli;

import graphql.linalyzer.cli.test.utils.OutputChecker;
import org.junit.Test;

import static graphql.linalyzer.cli.test.utils.OutputChecker.fileLine;
import static graphql.linalyzer.cli.test.utils.OutputChecker.ruleLine;
import static graphql.linalyzer.cli.test.utils.TestFileUtils.createTempFile;
import static java.util.Collections.singletonList;

public class ExecutionTest {

    @Test
    public void testSimpleConfigAndSchema() {
        final String config = "" +
                "rules:\n" +
                "  - name: camelCase\n" +
                "    severity: warning\n";

        final String schema = "" +
                "type Query {\n" +
                "  Name: String\n" +
                "  Id: String\n" +
                "  Age: String\n" +
                "}";

        final String configFilePath = createTempFile(config);
        final String schemaFilePath = createTempFile(schema);

        final String output = new Execution().execute(configFilePath, singletonList(schemaFilePath));

        new OutputChecker()
                .expect(
                        fileLine(schemaFilePath),
                        ruleLine("2:3","warning", "Not allowed name Name", "camelCase"),
                        ruleLine("3:3","warning", "Not allowed name Id", "camelCase"),
                        ruleLine("4:3","warning", "Not allowed name Age", "camelCase")
                )
                .check(output);
    }
}
