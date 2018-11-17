package graphql.linalyzer.cli.test.utils;

import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public final class DockerUtils {
    public static final String CONTAINER_DIR = "/files";

    private static final File projectRoot = new File("../");

    private DockerUtils() {
    }

    public static String buildDockerImage() {
        final ProcessResult processResult;
        try {
            processResult = new ProcessExecutor()
                    .directory(projectRoot)
                    .readOutput(true)
                    .redirectOutput(System.out)
                    .command("docker", "build", "-t", "linalyzer", ".")
                    .execute();
        } catch (Exception e) {
            throw new IllegalStateException("Error creating Docker image for integration tests", e);
        }

        if (processResult.getExitValue() != 0) {
            throw new IllegalStateException("Docker image creation script returned non-zero code: " + processResult.getExitValue());
        }

        final String regex = "Successfully built (.*)";

        final String successLine = processResult.getOutput().getLines().stream()
                .filter(line -> line.matches(regex))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't find imageId on the create image command output"));

        final Matcher matcher = Pattern.compile(regex).matcher(successLine);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new IllegalStateException("Couldn't find imageId on the create image command output");
    }

    public static String executeLinalyzer(
            String imageId, File directory, File configFile, Collection<File> schemaFiles
    ) {
        final ProcessResult processResult;

        final List<String> commands = new ArrayList<>();

        final List<String> schemaFilesString = schemaFiles.stream()
                .map(schemaFile -> "/files/" + schemaFile.getName())
                .collect(toList());

        commands.add("docker");
        commands.add("run");
        commands.add("--rm");
        commands.add("-v");
        commands.add(directory.getAbsolutePath() + ":/files");
        commands.add(imageId);
        commands.add("-c");
        commands.add("/files/"+configFile.getName());
        commands.addAll(schemaFilesString);

        try {
            processResult = new ProcessExecutor()
                    .directory(projectRoot)
                    .readOutput(true)
                    .redirectOutput(System.out)
                    .command(commands.toArray(new String[0]))
                    .execute();

        } catch (Exception e) {
            throw new IllegalStateException("Error executing command on Docker container", e);
        }

        return processResult.getOutput().getString();
    }

    public static File createTempDirForVolume() {
        final File tempDir = new File("build/tmp/temp-for-volume");

        try {
            if (!tempDir.exists()) {
                Files.createDirectory(tempDir.toPath());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error creating temporary directory: " + tempDir.getAbsolutePath(), e);
        }

        return tempDir;
    }
}
