package ru.clevertec.check.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CheckFormatterTest {

    @ParameterizedTest
    @MethodSource("provideCheckData")
    void testFormatCheckForCSV(Check check, String expectedCSV) {
        CheckFormatter formatter = new CheckFormatter();

        LocalDate fixedDate = LocalDate.of(2024, 8, 24);
        LocalTime fixedTime = LocalTime.of(19, 7, 27);

        Check spyСheck = Mockito.spy(check);
        Mockito.when(spyСheck.getDate()).thenReturn(fixedDate);
        Mockito.when(spyСheck.getTime()).thenReturn(fixedTime);

        String actualCSV = formatter.formatCheckForCSV(spyСheck);

        assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testParseCreateCheckDTO() {
        CheckFormatter formatter = new CheckFormatter();
        CreateCheckDTO expectedCreateCheckDTO = initCreateCheckDTO();
        String json = initJSON();

        CreateCheckDTO actualCreateCheckDTO = formatter.parseCreateCheckDTO(json);

        assertEquals(expectedCreateCheckDTO, actualCreateCheckDTO);
    }

    @Test
    void testParseCreateCheckDTOThrowsInternalServerErrorException() {
        CheckFormatter formatter = new CheckFormatter();

        String invalidJson = """
                 {
                  products:
                  [
                        {
                            "id": 13,
                            "quantity": 5
                        }
                        ]
                }""";

        assertThrows(InternalServerErrorException.class, () -> formatter.parseCreateCheckDTO(invalidJson));
    }


    @Test
    void testParseCreateCheckDTOThrowsBadRequestException() {
        CheckFormatter formatter = new CheckFormatter();

        String invalidJson = """
                 {
                  "products":
                  [
                        {
                         "quantity": 5
                        }
                  ]
                }""";

        assertThrows(BadRequestException.class, () -> formatter.parseCreateCheckDTO(invalidJson));
    }


    static Stream<Arguments> provideCheckData() {
        return Stream.of(
                Arguments.of(initCheck(true), initCSVWithDiscountCard()),
                Arguments.of(initCheck(false), initCSVWithoutDiscountCard())
        );
    }

    private static Check initCheck(boolean withDiscountCard) {
        ProductDTO dto1 = new ProductDTO(13, "Baguette 360g", 1.30, 10, true);
        ProductDTO dto2 = new ProductDTO(9, "Packed bananas 1kg", 1.10, 25, true);
        ProductDTO dto3 = new ProductDTO(4, "Packed potatoes 1kg", 1.47, 30, false);
        ProductDTO dto4 = new ProductDTO(1, "Milk", 1.07, 10, true);

        Check.CheckBuilder checkBuilder = new Check.CheckBuilder();

        if (withDiscountCard) {
            DiscountCardDTO discountCardDTO = new DiscountCardDTO(1, "1111", 3);
            checkBuilder.withDiscountCard(discountCardDTO);
        }

        checkBuilder.addProduct(dto1, 5);
        checkBuilder.addProduct(dto2, 1);
        checkBuilder.addProduct(dto3, 30);
        checkBuilder.addProduct(dto4, 6);

        return checkBuilder.build();
    }

    private static String initCSVWithDiscountCard() {
        return """
                Date;Time
                24.08.2024;19:07:27
                                
                QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
                5;Baguette 360g;1.30$;0.65$;6.50$
                1;Packed bananas 1kg;1.10$;0.03$;1.10$
                30;Packed potatoes 1kg;1.47$;1.32$;44.10$
                6;Milk;1.07$;0.64$;6.42$

                DISCOUNT CARD;DISCOUNT PERCENTAGE
                1111;3%
                                
                TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
                58.12$;2.64$;55.48$
                """;
    }

    private static String initCSVWithoutDiscountCard() {
        return """
                Date;Time
                24.08.2024;19:07:27
                                
                QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
                5;Baguette 360g;1.30$;0.65$;6.50$
                1;Packed bananas 1kg;1.10$;0.00$;1.10$
                30;Packed potatoes 1kg;1.47$;0.00$;44.10$
                6;Milk;1.07$;0.64$;6.42$
                                
                TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
                58.12$;1.29$;56.83$
                """;
    }


    private String initJSON() {
        return """
                {
                    "products": [
                        {
                            "id": 13,
                            "quantity": 5
                        },
                        {
                            "id": 9,
                            "quantity": 1
                        },
                        {
                            "id": 4,
                            "quantity": 30
                        },
                        {
                            "id": 1,
                            "quantity": 6
                        }
                    ],
                    "discountCard": 1111,
                    "balanceDebitCard": 100.25
                }""";
    }

    private CreateCheckDTO initCreateCheckDTO() {
        CreateCheckDTO dto = new CreateCheckDTO();
        dto.setCartProducts(Map.of(13l, 5, 9l, 1, 4l, 30, 1l, 6));
        dto.setDiscountCardNumber("1111");
        dto.setBalanceDebitCard(100.25);

        return dto;
    }


}