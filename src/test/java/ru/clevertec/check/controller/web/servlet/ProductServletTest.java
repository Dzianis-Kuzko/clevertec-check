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
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.service.api.IProductService;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServletTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private IProductService productServiceMock;

    @Mock
    private PrintWriter writerMock;

    private MockedStatic<ProductServiceFactory> mockedFactory;
    private MockedStatic<UtilServlet> mockedUtilServlet;

    private ProductServlet productServlet;

    @BeforeEach
    void setUp() {
        mockedFactory = mockStatic(ProductServiceFactory.class);
        mockedUtilServlet = mockStatic(UtilServlet.class);
        when(ProductServiceFactory.getInstance(any())).thenReturn(productServiceMock);

        productServlet = new ProductServlet();
    }

    @AfterEach
    void tearDown() {
        if (mockedFactory != null) {
            mockedFactory.close();
        }
        if (mockedUtilServlet != null) {
            mockedUtilServlet.close();
        }
    }

    @Test
    void testDoGetSuccess() throws ServletException, IOException {
        long productId = 1L;
        ProductDTO productDTO = getProductDTO();

        when(UtilServlet.getId(requestMock, "id")).thenReturn(productId);
        when(productServiceMock.get(productId)).thenReturn(productDTO);
        when(responseMock.getWriter()).thenReturn(writerMock);

        productServlet.doGet(requestMock, responseMock);

        verify(writerMock).write(anyString());
    }

    @Test
    void testDoGetThrowsProductNotFoundException() {
        long productId = 1L;

        when(UtilServlet.getId(requestMock, "id")).thenReturn(productId);
        when(productServiceMock.get(productId)).thenReturn(null);

        assertThrows(ProductNotFoundException.class, () -> productServlet.doGet(requestMock, responseMock));
    }

    @Test
    void testDoPostSuccess() throws ServletException, IOException {
        String jsonInput = """
                {
                "description": "Milk",
                "price": 1.00,
                "quantity": 4,
                "isWholesale": true
                }
                """;

        CreateProductDTO createProductDTO = getCreateProductDTO();

        when(UtilServlet.getRequestBodyAsJson(requestMock)).thenReturn(jsonInput);

        productServlet.doPost(requestMock, responseMock);

        verify(productServiceMock).create(createProductDTO);
    }

    @Test
    void testDoPutSuccess() throws ServletException, IOException {
        long productId = 1L;
        String jsonInput = """
                {
                "description": "Milk",
                "price": 1.00,
                "quantity": 4,
                "isWholesale": true
                }
                """;
        ProductDTO productDTO = getProductDTO();

        when(UtilServlet.getId(requestMock, "id")).thenReturn(productId);
        when(UtilServlet.getRequestBodyAsJson(requestMock)).thenReturn(jsonInput);

        productServlet.doPut(requestMock, responseMock);

        verify(productServiceMock).update(productDTO);
    }

    @Test
    void testDoDeleteSuccess() throws ServletException, IOException {
        long productId = 1L;
        when(UtilServlet.getId(requestMock, "id")).thenReturn(productId);

        productServlet.doDelete(requestMock, responseMock);

        verify(productServiceMock).delete(productId);
    }

    private ProductDTO getProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setDescription("Milk");
        dto.setPrice(1.0);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }

    private CreateProductDTO getCreateProductDTO() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setDescription("Milk");
        dto.setPrice(1.00);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }
}