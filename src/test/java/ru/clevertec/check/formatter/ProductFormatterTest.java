package ru.clevertec.check.formatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductFormatterTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFormatProductForJSON() throws JsonProcessingException {
        ProductFormatter productFormatter = new ProductFormatter();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setDescription("Milk");
        productDTO.setPrice(1.0);
        productDTO.setQuantityInStock(4);
        productDTO.setWholesaleProduct(true);

        String expectedJson = """
                {
                  "id": 1,
                  "description": "Milk",
                  "price": "1.00",
                  "quantity": 4,
                  "isWholesale": true
                }""";

        String actualJson = productFormatter.formatProductForJSON(productDTO);
        assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(actualJson));
    }

    @Test
    void testParseCreateProductDTO() throws JsonProcessingException {

        ProductFormatter productFormatter = new ProductFormatter();

        String json = """
                {
                  "description": "Milk",
                  "price": 1.00,
                  "quantity": 4,
                  "isWholesale": true
                }""";

        CreateProductDTO expectedDTO = new CreateProductDTO();
        expectedDTO.setDescription("Milk");
        expectedDTO.setPrice(1.00);
        expectedDTO.setQuantityInStock(4);
        expectedDTO.setWholesaleProduct(true);

        CreateProductDTO actualDTO = productFormatter.parseCreateProductDTO(json);

        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    void testParseCreateProductDTOThrowsBadRequestException() {
        ProductFormatter productFormatter = new ProductFormatter();

        String json = """
                {
                  "price": 1.00,
                  "quantity": 4,
                  "isWholesale": true
                }""";

        assertThrows(BadRequestException.class, () -> productFormatter.parseCreateProductDTO(json));
    }


    @Test
    void testParseCreateProductDTOThrowsInternalServerErrorException() {
        ProductFormatter productFormatter = new ProductFormatter();

        String json = """
                {
                  "description":,
                  "price": 1.00,
                  "quantity": 4,
                  "isWholesale": true
                }""";

        assertThrows(InternalServerErrorException.class, () -> productFormatter.parseCreateProductDTO(json));
    }
}