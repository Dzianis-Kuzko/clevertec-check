package ru.clevertec.check.controller.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.ProductNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExceptionHandlingFilterTest {

    @Mock
    private FilterChain chainMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    private ExceptionHandlingFilter filter;

    @BeforeEach
    void setUp() throws IOException {
        filter = new ExceptionHandlingFilter();
        when(responseMock.getWriter()).thenReturn(writerMock);
    }

    @Test
    void testDoFilterBadRequestException() throws IOException, ServletException {
        doThrow(new BadRequestException("Bad request")).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);

        verify(writerMock).write(ExceptionMessage.BAD_REQUEST.getMessage());
    }

    @Test
    void testDoFilterNotEnoughMoneyException() throws IOException, ServletException {
        doThrow(new NotEnoughMoneyException("Not enough money")).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);

        verify(writerMock).write(ExceptionMessage.NOT_ENOUGH_MONEY.getMessage());
    }

    @Test
    void testDoFilterProductNotFoundException() throws IOException, ServletException {
        doThrow(new ProductNotFoundException("Product not found")).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);

        verify(writerMock).write(ExceptionMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    void testDoFilterInternalServerErrorException() throws IOException, ServletException {
        doThrow(new InternalServerErrorException("Internal server error")).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);

        verify(writerMock).write(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testDoFilterGenericException() throws IOException, ServletException {
        doThrow(new RuntimeException("Generic exception")).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);

        verify(writerMock).write(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testDoFilterSuccess() throws IOException, ServletException {
        filter.doFilter(requestMock, responseMock, chainMock);

        verify(chainMock).doFilter(requestMock, responseMock);
        verify(writerMock, never()).write(anyString());
    }
}