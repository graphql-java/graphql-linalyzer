package graphql.linalyzer.cli.test.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public final class TestFileUtils {
    private TestFileUtils() {
    }

    public static String createTempFile(String contents) {
        final String fileName = UUID.randomUUID().toString();
        final String suffix = ".temp";

        final File tempFile;

        try {
            tempFile = File.createTempFile(fileName, suffix);
        } catch (IOException e) {
            throw new IllegalStateException("Error creating temporary file", e);
        }

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] strToBytes = contents.getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing file", e);
        }

        return tempFile.getAbsolutePath();
    }
}
