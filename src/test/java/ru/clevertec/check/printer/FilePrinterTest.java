package ru.clevertec.check.printer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FilePrinterTest {


    private FilePrinter filePrinter = new FilePrinter("testFile.txt");


    @Test
    void testPrintSuccess1() throws IOException {
        try (MockedConstruction<FileWriter> mocked = mockConstruction(FileWriter.class)) {

            filePrinter.print("test");

            FileWriter mockFileWriter = mocked.constructed().get(0);
            verify(mockFileWriter).write("test");
        }
    }

    @Test
    void testPrintThrowsInternalServerErrorException() {
        try (MockedConstruction<FileWriter> mockedFileWriter = mockConstruction(FileWriter.class,
                (mock, context) -> doThrow(IOException.class).when(mock).write(anyString()))) {

            assertThrows(InternalServerErrorException.class, () -> filePrinter.print("Test"));
        }
    }
}
