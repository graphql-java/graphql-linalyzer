package graphql.linalyzer.cli.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public final class TestFileUtils {
    private TestFileUtils() {
    }

    public static File createTempFile(String contents) {
        try {
            return createTempFile(contents, Files.createTempDirectory(UUID.randomUUID().toString()).toFile());
        } catch (IOException e) {
            throw new IllegalStateException("Error creating temporary directory", e);
        }
    }

    public static File createTempFile(String contents, File tempDirectory) {
        final String fileName = UUID.randomUUID().toString();
        final String suffix = ".temp";

        final File tempFile;

        try {
            tempFile = File.createTempFile(fileName, suffix, tempDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Error creating temporary file", e);
        }

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] strToBytes = contents.getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing file", e);
        }

        return tempFile;
    }
}
