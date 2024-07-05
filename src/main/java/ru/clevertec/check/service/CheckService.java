package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.core.Check;
import main.java.ru.clevertec.check.core.dto.CreateCheckDTO;
import main.java.ru.clevertec.check.core.dto.DiscountCardDTO;
import main.java.ru.clevertec.check.core.dto.ProductDTO;
import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.ExceptionMessage;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.printer.IPrinter;
import main.java.ru.clevertec.check.service.api.ICheckService;
import main.java.ru.clevertec.check.service.api.IDiscountCardService;
import main.java.ru.clevertec.check.service.api.IProductService;

import java.util.Map;

public class CheckService implements ICheckService {
    private static final int DEFAULT_DISCOUNT_AMOUNT = 2;
    private final IProductService productService;
    private final IDiscountCardService discountCardService;

    public CheckService(IProductService productService, IDiscountCardService discountCardService) {
        this.productService = productService;
        this.discountCardService = discountCardService;
    }

    @Override
    public Check createCheck(CreateCheckDTO createCheckDTO) {
        validateDataForCheck(createCheckDTO);

        Check.CheckBuilder checkBuilder = new Check.CheckBuilder();

        processDiscountCard(createCheckDTO, checkBuilder);

        addProductsToCheck(createCheckDTO, checkBuilder);

        Check check = checkBuilder.build();

        validateSufficientBalance(createCheckDTO, check);

        return check;
    }

    @Override
    public void print(String message, IPrinter printer) {
        printer.print(message);
    }

    private void validateDataForCheck(CreateCheckDTO createCheckDTO) {
        validateDiscountCardNumber(createCheckDTO.getDiscountCardNumber());
        validateBalanceDebitCard(createCheckDTO.getBalanceDebitCard());
        validateProductsInCart(createCheckDTO.getCartProducts());
    }

    private boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasTwoOrFewerDecimalPlaces(double value) {
        double scaledValue = value * 100;
        return scaledValue == Math.floor(scaledValue);
    }

    private void processDiscountCard(CreateCheckDTO createCheckDTO, Check.CheckBuilder checkBuilder) {
        String discountCardNumber = createCheckDTO.getDiscountCardNumber();
        if (discountCardNumber == null) {
            checkBuilder.withoutDiscountCard();
        } else {
            DiscountCardDTO discountCardDTO = discountCardService.get(discountCardNumber);
            if (discountCardDTO == null) {
                discountCardDTO = new DiscountCardDTO();
                discountCardDTO.setNumber(discountCardNumber);
                discountCardDTO.setDiscountAmount(DEFAULT_DISCOUNT_AMOUNT);
            }
            checkBuilder.withDiscountCard(discountCardDTO);
        }
    }

    private void addProductsToCheck(CreateCheckDTO createCheckDTO, Check.CheckBuilder checkBuilder) {
        for (Map.Entry<Long, Integer> entry : createCheckDTO.getCartProducts().entrySet()) {
            Long id = entry.getKey();
            Integer quantity = entry.getValue();
            ProductDTO productDTO = productService.get(id);
            checkBuilder.addProduct(productDTO, quantity);
        }
    }

    private void validateSufficientBalance(CreateCheckDTO createCheckDTO, Check check) {
        if (check.getTotalWithDiscount() > createCheckDTO.getBalanceDebitCard()) {
            throw new NotEnoughMoneyException(ExceptionMessage.NOT_ENOUGH_MONEY.getMessage());
        }
    }

    private void validateDiscountCardNumber(String discountCardNumber) {
        if (discountCardNumber != null && (discountCardNumber.length() != 4 || !isNumber(discountCardNumber))) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
        }
    }

    private void validateBalanceDebitCard(double balanceDebitCard) {
        if (!hasTwoOrFewerDecimalPlaces(balanceDebitCard)) {
            throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
        }
    }

    private void validateProductsInCart(Map<Long, Integer> cartProducts) {
        for (Map.Entry<Long, Integer> entry : cartProducts.entrySet()) {
            Long id = entry.getKey();
            int quantity = entry.getValue();

            ProductDTO productDTO = productService.get(id);

            if (productDTO == null) {
                throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
            }

            if (productDTO.getQuantityInStock() < quantity) {
                throw new BadRequestException(ExceptionMessage.BAD_REQUEST.getMessage());
            }
        }
    }
}
