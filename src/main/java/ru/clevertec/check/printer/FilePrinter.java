package ru.clevertec.check.printer;

import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.FileWriter;
import java.io.IOException;

public class FilePrinter implements IPrinter {
    private String filePath;

    public FilePrinter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void print(String message) {
        try (FileWriter writer = new FileWriter(filePath)) {

            writer.write(message);

        } catch (IOException e) {
            throw new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
        }
    }


}
