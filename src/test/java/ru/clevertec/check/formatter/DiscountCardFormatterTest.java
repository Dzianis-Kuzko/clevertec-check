package ru.clevertec.check.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountCardFormatterTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void formatDiscountCardForJSON() throws JsonProcessingException {
        DiscountCardFormatter formater = new DiscountCardFormatter();

        DiscountCardDTO dto = new DiscountCardDTO();
        dto.setId(1);
        dto.setNumber("1111");
        dto.setDiscountAmount(3);

        String expectedJson = """
                {
                    "id": 1,
                    "discountCard": 1111,
                    "discountAmount": 3
                }""";

        String actualJson = formater.formatDiscountCardForJSON(dto);

        assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(actualJson));
    }

    @Test
    void testParseCreateDiscountCardDTO() {
        DiscountCardFormatter formater = new DiscountCardFormatter();

        String json = """
                {
                    "discountCard": 1111,
                    "discountAmount": 3
                }""";

        CreateDiscountCardDTO expectedDTO = new CreateDiscountCardDTO();

        expectedDTO.setNumber("1111");
        expectedDTO.setDiscountAmount(3);

        CreateDiscountCardDTO actualDTO = formater.parseCreateDiscountCardDTO(json);

        assertEquals(expectedDTO, actualDTO);

    }


    @Test
    void testParseCreateDiscountCardDTOThrowsBadRequestException() {
        DiscountCardFormatter formatter = new DiscountCardFormatter();

        String json = """
                {
                    "discountAmount": 3
                }""";

        assertThrows(BadRequestException.class, () -> formatter.parseCreateDiscountCardDTO(json));
    }

    @Test
    void testParseCreateDiscountCardDTOThrowsInternalServerErrorException() {
        DiscountCardFormatter formatter = new DiscountCardFormatter();

        String json = """
                {
                    discountCard: 1111,
                    "discountAmount": 3
                }""";

        assertThrows(InternalServerErrorException.class, () -> formatter.parseCreateDiscountCardDTO(json));

    }
}