package ru.clevertec.check.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

public class DiscountCardFormatter {
    private final ObjectMapper objectMapper;

    public DiscountCardFormatter() {
        this.objectMapper = new ObjectMapper();
    }

    public String formatDiscountCardForJSON(DiscountCardDTO discountCardDTO) throws JsonProcessingException {
        ObjectNode formattedJson = objectMapper.createObjectNode();
        formattedJson.put("id", discountCardDTO.getId());
        formattedJson.put("discountCard", Integer.parseInt(discountCardDTO.getNumber()));
        formattedJson.put("discountAmount", discountCardDTO.getDiscountAmount());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(formattedJson);
    }

    public CreateDiscountCardDTO parseCreateDiscountCardDTO(String json) {
        CreateDiscountCardDTO createDiscountCardDTO = new CreateDiscountCardDTO();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(json);
            createDiscountCardDTO.setNumber(jsonNode.get("discountCard").asText());
            createDiscountCardDTO.setDiscountAmount(jsonNode.get("discountAmount").asInt());
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException();
        } catch (NullPointerException e) {
            throw new BadRequestException();
        }

        return createDiscountCardDTO;
    }
}
