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
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.service.api.IDiscountCardService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountCardServletTest {
    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private IDiscountCardService discountCardServiceMock;

    @Mock
    private PrintWriter writerMock;

    private MockedStatic<DiscountCardServiceFactory> mockedFactory;
    private MockedStatic<UtilServlet> mockedUtilServlet;

    private DiscountCardServlet discountCardServlet;

    @BeforeEach
    void setUp() {
        mockedFactory = mockStatic(DiscountCardServiceFactory.class);
        mockedUtilServlet = mockStatic(UtilServlet.class);
        when(DiscountCardServiceFactory.getInstance(any())).thenReturn(discountCardServiceMock);

        discountCardServlet = new DiscountCardServlet();
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
        long discountCardId = 1L;
        DiscountCardDTO discountCard = getDiscountCardDTO();

        when(UtilServlet.getId(requestMock, "id")).thenReturn(discountCardId);
        when(discountCardServiceMock.get(discountCardId)).thenReturn(discountCard);
        when(responseMock.getWriter()).thenReturn(writerMock);

        discountCardServlet.doGet(requestMock, responseMock);

        verify(writerMock).write(anyString());
    }

    @Test
    void testDoGetThrowsBadRequestException() {
        long discountCardId = 1L;

        when(UtilServlet.getId(requestMock, "id")).thenReturn(discountCardId);
        when(discountCardServiceMock.get(discountCardId)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> discountCardServlet.doGet(requestMock, responseMock));
    }

    @Test
    void testDoPostSuccess() throws ServletException, IOException {

        String jsonInput = """
                {
                "discountCard": 1111,
                "discountAmount": 3
                }
                """;

        CreateDiscountCardDTO createDiscountCardDTO = getCreateDiscountCardDTO();

        when(UtilServlet.getRequestBodyAsJson(requestMock)).thenReturn(jsonInput);

        discountCardServlet.doPost(requestMock, responseMock);

        verify(discountCardServiceMock).create(createDiscountCardDTO);
    }

    @Test
    void doDeleteSuccess() throws ServletException, IOException {
        long discountCardId = 1L;
        when(UtilServlet.getId(requestMock, "id")).thenReturn(discountCardId);

        discountCardServlet.doDelete(requestMock, responseMock);
        verify(discountCardServiceMock).delete(discountCardId);
    }

    @Test
    void doPutSuccess() throws ServletException, IOException {
        long discountCardId = 1L;

        String jsonInput = """
                {
                "discountCard": 1111,
                "discountAmount": 3
                }
                """;
        DiscountCardDTO discountCardDTO = getDiscountCardDTO();

        when(UtilServlet.getId(requestMock, "id")).thenReturn(discountCardId);
        when(UtilServlet.getRequestBodyAsJson(requestMock)).thenReturn(jsonInput);

        discountCardServlet.doPut(requestMock, responseMock);

        verify(discountCardServiceMock).update(discountCardDTO);

    }

    private DiscountCardDTO getDiscountCardDTO() {
        DiscountCardDTO dto = new DiscountCardDTO();
        dto.setId(1);
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

    private CreateDiscountCardDTO getCreateDiscountCardDTO() {
        CreateDiscountCardDTO dto = new CreateDiscountCardDTO();
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

}