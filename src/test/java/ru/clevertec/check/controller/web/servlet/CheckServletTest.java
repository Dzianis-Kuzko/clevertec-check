package ru.clevertec.check.controller.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.controller.web.servlet.util.UtilServlet;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.api.IDiscountCardService;
import ru.clevertec.check.service.api.IProductService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServletTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private ICheckService checkServiceMock;

    @Mock
    private IDiscountCardService discountCardServiceMock;

    @Mock
    private IProductService productServiceMock;

    @Mock
    private PrintWriter writerMock;

    private MockedStatic<ProductServiceFactory> mockedProductFactory;
    private MockedStatic<DiscountCardServiceFactory> mockedDiscountCardFactory;
    private MockedStatic<UtilServlet> mockedUtilServlet;

    private CheckServlet checkServlet;

    @BeforeEach
    void setUp() throws Exception {
        mockedProductFactory = mockStatic(ProductServiceFactory.class);
        mockedDiscountCardFactory = mockStatic(DiscountCardServiceFactory.class);
        mockedUtilServlet = mockStatic(UtilServlet.class);

        when(DiscountCardServiceFactory.getInstance(any())).thenReturn(discountCardServiceMock);
        when(ProductServiceFactory.getInstance(any())).thenReturn(productServiceMock);

        checkServlet = new CheckServlet();

        replaceField(checkServlet, "checkService", checkServiceMock);

        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @AfterEach
    void tearDown() {
        if (mockedProductFactory != null) {
            mockedProductFactory.close();
        }
        if (mockedDiscountCardFactory != null) {
            mockedDiscountCardFactory.close();
        }
        if (mockedUtilServlet != null) {
            mockedUtilServlet.close();
        }

    }

    @Test
    void testDoPostSuccess() throws ServletException, IOException {
        String jsonInput = initJSON();
        CreateCheckDTO createCheckDTO = initCreateCheckDTO();
        Check check = initCheck(true);
        String checkCSV = initCSVWithDiscountCard();

        when(UtilServlet.getRequestBodyAsJson(requestMock)).thenReturn(jsonInput);
        when(checkServiceMock.createCheck(createCheckDTO)).thenReturn(check);

        checkServlet.doPost(requestMock, responseMock);

        verify(checkServiceMock).createCheck(createCheckDTO);

    }

    private void replaceField(Object target, String fieldName, Object newValue) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, newValue);
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

    private CreateCheckDTO initCreateCheckDTO() {
        CreateCheckDTO dto = new CreateCheckDTO();
        Map<Long, Integer> cartProducts = new HashMap<>();
        cartProducts.put(13l, 5);
        cartProducts.put(9l, 1);
        cartProducts.put(4l, 30);
        cartProducts.put(1l, 6);
        dto.setCartProducts(cartProducts);
        dto.setDiscountCardNumber("1111");
        dto.setBalanceDebitCard(100.25);

        return dto;
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
}
