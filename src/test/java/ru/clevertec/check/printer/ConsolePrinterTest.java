package ru.clevertec.check.printer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolePrinterTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsolePrinter consolePrinter;

    @BeforeEach
    void setUp() {
        consolePrinter = new ConsolePrinter();
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    void testPrintSuccess() {
        consolePrinter.print("Test print");
        assertEquals("Test print" + System.lineSeparator(), outputStream.toString());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

}