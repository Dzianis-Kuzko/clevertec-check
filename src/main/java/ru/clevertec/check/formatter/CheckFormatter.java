package ru.clevertec.check.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.check.core.CartProduct;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CheckFormatter {
    private final ObjectMapper objectMapper;

    public CheckFormatter() {
        this.objectMapper = new ObjectMapper();
    }

    public String formatCheckForCSV(Check check) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = check.getDate().format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = check.getTime().format(timeFormatter);

        sb.append("Date;Time\n" + formattedDate + ";" + formattedTime + "\n\n")
                .append("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");

        for (CartProduct cartProduct : check.getCartProducts()) {
            sb.append(String.format("%d;%s;%.2f$;%.2f$;%.2f$\n",
                    cartProduct.getQuantity(),
                    cartProduct.getDescription(),
                    cartProduct.getPrice(),
                    cartProduct.getDiscount(),
                    cartProduct.getTotalPrice()));
        }

        if (check.getDiscountCardNumber() != null) {
            sb.append("\nDISCOUNT CARD;DISCOUNT PERCENTAGE\n" +
                    check.getDiscountCardNumber() + ";" +
                    check.getDiscountPercentage() + "%\n");
        }
        sb.append("\nTOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
        sb.append(String.format("%.2f$;%.2f$;%.2f$\n",
                check.getTotalPrice(),
                check.getTotalDiscount(),
                check.getTotalWithDiscount()));

        return sb.toString();
    }

    public CreateCheckDTO parseCreateCheckDTO(String jsonString) {
        JsonNode jsonNode = null;
        CreateCheckDTO createCheckDTO = new CreateCheckDTO();
        try {

            jsonNode = objectMapper.readTree(jsonString);

        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException();
        }

        try {

            Map<Long, Integer> cartProducts = new HashMap<>();
            JsonNode productsNode = jsonNode.get("products");

            for (JsonNode productNode : productsNode) {
                long id = productNode.get("id").asLong();
                int quantity = productNode.get("quantity").asInt();
                cartProducts.put(id, cartProducts.getOrDefault(id,0) + quantity);
            }

            createCheckDTO.setCartProducts(cartProducts);

            if (jsonNode.has("discountCard")) {
                createCheckDTO.setDiscountCardNumber(jsonNode.get("discountCard").asText());
            }

            createCheckDTO.setBalanceDebitCard(jsonNode.get("balanceDebitCard").asDouble());
        } catch (NullPointerException e) {
            throw new BadRequestException();
        }

        return createCheckDTO;
    }
}
