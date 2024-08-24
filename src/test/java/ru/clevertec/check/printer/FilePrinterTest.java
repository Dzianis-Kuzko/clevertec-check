package ru.clevertec.check.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilePrinterTest {

    private static final String TEST_FILE_PATH = "test_file.txt";
    private static final String TEST_INVALID_FILE_PATH = "invalid/file/test_file.txt";

    @Test
    void testPrintSuccess() {
        FilePrinter filePrinter = new FilePrinter(TEST_FILE_PATH);

        String testMessage = "Test message";

        filePrinter.print(testMessage);

        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        } catch (IOException e) {
            content = "test failed";
        }

        assertEquals(testMessage, content);
    }

    @Test
    void testPrintThrowsException() throws IOException {
        FilePrinter filePrinter = new FilePrinter(TEST_INVALID_FILE_PATH);
        String message = "Test message";

        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, () -> filePrinter.print(message));

        assertEquals(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), exception.getMessage());
    }


    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}