package ru.clevertec.check.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.text.DecimalFormat;

public class ProductFormatter {
    private final ObjectMapper objectMapper;
    private final DecimalFormat decimalFormat;

    public ProductFormatter() {
        this.objectMapper = new ObjectMapper();
        this.decimalFormat = new DecimalFormat("#.00");
    }

    public String formatProductForJSON(ProductDTO productDTO) throws JsonProcessingException {
        ObjectNode formattedJson = objectMapper.createObjectNode();
        formattedJson.put("id", productDTO.getId());
        formattedJson.put("description", productDTO.getDescription());
        formattedJson.put("price", decimalFormat.format(productDTO.getPrice()));
        formattedJson.put("quantity", productDTO.getQuantityInStock());
        formattedJson.put("isWholesale", productDTO.isWholesaleProduct());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(formattedJson);
    }

    public CreateProductDTO parseCreateProductDTO(String json) {
        CreateProductDTO createProductDTO = new CreateProductDTO();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
            createProductDTO.setDescription(jsonNode.get("description").asText());
            createProductDTO.setPrice(jsonNode.get("price").asDouble());
            createProductDTO.setQuantityInStock(jsonNode.get("quantity").asInt());
            createProductDTO.setWholesaleProduct(jsonNode.get("isWholesale").asBoolean());

        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException();
        } catch (NullPointerException e) {
            throw new BadRequestException();
        }

        return createProductDTO;
    }
}
