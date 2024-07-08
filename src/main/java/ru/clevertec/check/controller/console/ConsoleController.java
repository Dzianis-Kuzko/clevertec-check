package ru.clevertec.check.controller.console;

import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.formatter.CheckFormatter;
import ru.clevertec.check.printer.ConsolePrinter;
import ru.clevertec.check.printer.FilePrinter;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ConsoleController {
    private final static String RESULT_FILE_DEFAULT_PATH = "result.csv";
    private final static String PREFIX_PARAM_DISCOUNT_CARD = "discountCard=";
    private final static String PREFIX_PARAM_BALANCE_DEBIT_CARD = "balanceDebitCard=";
    private final static String PREFIX_PARAM_RESULT_FILE_PATH = "saveToFile=";
    private final static String PREFIX_PARAM_DATASOURCE_URL = "datasource.url=";
    private final static String PREFIX_PARAM_DATASOURCE_USERNAME = "datasource.username=";
    private final static String PREFIX_PARAM_DATASOURCE_PASSWORD = "datasource.password=";

    private String resultFilePath;
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;

    private final CheckFormatter checkFormatter;

    private ICheckService checkService;

    public ConsoleController() {
        this.checkFormatter = new CheckFormatter();
    }

    public void processCheck(String[] args) {
        Check check = null;
        Exception exception = null;
        CreateCheckDTO createCheckDTO = null;

        try {
            createCheckDTO = parseCommandLineArgs(args);

            initializeCheckService();

            check = checkService.createCheck(createCheckDTO);

        } catch (BadRequestException | NotEnoughMoneyException | InternalServerErrorException e) {
            exception = e;
            initializeCheckService();

        } catch (Exception e) {
            exception = new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
            initializeCheckService();
        }

        printCheck(check, exception, args);


    }


    private CreateCheckDTO parseCommandLineArgs(String[] args) {
        Map<Long, Integer> productQuantities = new HashMap<>();
        String discountCardNumber = null;
        Double balanceDebitCard = null;

        try {
            for (String arg : args) {
                if (arg.startsWith(PREFIX_PARAM_DISCOUNT_CARD)) {
                    discountCardNumber = splitBySeparator(arg, "=")[1];

                } else if (arg.startsWith(PREFIX_PARAM_BALANCE_DEBIT_CARD)) {
                    balanceDebitCard = Double.parseDouble(splitBySeparator(arg, "=")[1]);

                } else if (arg.startsWith(PREFIX_PARAM_RESULT_FILE_PATH)) {
                    resultFilePath = splitBySeparator(arg, "=")[1];

                } else if (arg.startsWith(PREFIX_PARAM_DATASOURCE_URL)) {
                    datasourceUrl = splitBySeparator(arg, "=")[1];

                } else if (arg.startsWith(PREFIX_PARAM_DATASOURCE_USERNAME)) {
                    datasourceUsername = splitBySeparator(arg, "=")[1];

                } else if (arg.startsWith(PREFIX_PARAM_DATASOURCE_PASSWORD)) {
                    datasourcePassword = splitBySeparator(arg, "=")[1];

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

        if (productQuantities.isEmpty()
                || balanceDebitCard == null
                || resultFilePath == null
                || datasourceUrl == null
                || datasourceUsername == null
                || datasourcePassword == null

        ) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
        }

        return new CreateCheckDTO(productQuantities, discountCardNumber, balanceDebitCard);
    }


    private void printCheck(Check check, Exception exception, String[] args) {
        String message;

        if (resultFilePath == null) {
            resultFilePath = RESULT_FILE_DEFAULT_PATH;
        }

        if (exception != null) {
            message = checkFormatter.formatErrorForCsv(exception.getMessage());
            checkService.print(message, new FilePrinter(resultFilePath));
            checkService.print(message, new ConsolePrinter());
            checkService.print(Arrays.toString(args), new ConsolePrinter());
            exception.printStackTrace(System.out);
            return;
        }

        message = checkFormatter.formatCheckForCSV(check);

        checkService.print(message, new FilePrinter(resultFilePath));
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

    private void initializeCheckService() {
        DatabaseConfig databaseConfig = new DatabaseConfig(datasourceUrl, datasourceUsername, datasourcePassword);
        this.checkService = new CheckService(
                ProductServiceFactory.getInstance(databaseConfig),
                DiscountCardServiceFactory.getInstance(databaseConfig)
        );
    }

}
