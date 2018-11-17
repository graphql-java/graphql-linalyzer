package graphql.linalyzer.cli;

import graphql.linalyzer.cli.test.utils.DockerUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DockerImageRule implements TestRule {
    private String imageId;

    @Override
    public Statement apply(
            Statement base, Description description
    ) {
        imageId = DockerUtils.buildDockerImage();

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
            }
        };
    }

    String getImageId() {
        return imageId;
    }
}
