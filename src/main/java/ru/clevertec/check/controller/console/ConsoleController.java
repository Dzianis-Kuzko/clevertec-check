package main.java.ru.clevertec.check.controller.console;

import main.java.ru.clevertec.check.core.Check;
import main.java.ru.clevertec.check.core.dto.CreateCheckDTO;
import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.ExceptionMessage;
import main.java.ru.clevertec.check.exception.InternalServerErrorException;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.formatter.CheckFormatter;
import main.java.ru.clevertec.check.printer.ConsolePrinter;
import main.java.ru.clevertec.check.printer.FilePrinter;
import main.java.ru.clevertec.check.service.CheckService;
import main.java.ru.clevertec.check.service.api.ICheckService;
import main.java.ru.clevertec.check.service.factory.DiscountCardServiceFactory;
import main.java.ru.clevertec.check.service.factory.ProductServiceFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ConsoleController {
    private static final String PRODUCT_FILE_PATH = "./src/main/resources/products.csv";
    private static final String DISCOUNT_CARD_FILE_PATH = "./src/main/resources/discountCards.csv";
    private static final String RESULT_FILE_PATH = "result.csv";
    private final ICheckService checkService;
    private final CheckFormatter checkFormatter;

    public ConsoleController() {
        this.checkService = new CheckService(
                ProductServiceFactory.getInstance(PRODUCT_FILE_PATH),
                DiscountCardServiceFactory.getInstance(DISCOUNT_CARD_FILE_PATH));
        this.checkFormatter = new CheckFormatter();
    }

    public void processCheck(String[] args) {
        Check check = null;
        Exception exception = null;

        try {
            check = checkService.createCheck(parseCommandLineArgs(args));
        } catch (BadRequestException | NotEnoughMoneyException | InternalServerErrorException e) {
            exception = e;
        } catch (Exception e) {
            exception = new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
        }

        printCheck(check, exception, args);

    }


    private CreateCheckDTO parseCommandLineArgs(String[] args) {
        Map<Long, Integer> productQuantities = new HashMap<>();
        String discountCardNumber = null;
        Double balanceDebitCard = null;

        try {
            for (String arg : args) {

                if (arg.startsWith("discountCard=")) {

                    discountCardNumber = splitBySeparator(arg, "=")[1];

                } else if (arg.startsWith("balanceDebitCard=")) {

                    balanceDebitCard = Double.parseDouble(splitBySeparator(arg, "=")[1]);

                } else if (arg.contains("-")) {

                    String[] parts = splitBySeparator(arg, "-");

                    long id = Long.parseLong(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);

                    productQuantities.put(id, productQuantities.getOrDefault(id, 0) + quantity);
                }
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage(), e);
        }

        if (productQuantities.isEmpty() || balanceDebitCard == null) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
        }

        return new CreateCheckDTO(productQuantities, discountCardNumber, balanceDebitCard);
    }


    private void printCheck(Check check, Exception exception, String[] args) {
        String message;

        if (exception != null) {
            message = checkFormatter.formatErrorForCsv(exception.getMessage());
            checkService.print(message, new FilePrinter(RESULT_FILE_PATH));
            checkService.print(message, new ConsolePrinter());
            checkService.print(Arrays.toString(args), new ConsolePrinter());
            exception.printStackTrace(System.out);
            return;
        }

        message = checkFormatter.formatCheckForCSV(check);

        checkService.print(message, new FilePrinter(RESULT_FILE_PATH));
        checkService.print(message, new ConsolePrinter());
        checkService.print(Arrays.toString(args), new ConsolePrinter());
    }

    private String[] splitBySeparator(String arg, String separator) {
        String[] parts = arg.split(separator);

        if (parts.length != 2) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
        }

        return parts;
    }

}
