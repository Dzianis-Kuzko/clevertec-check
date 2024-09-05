package ru.clevertec.check.controller.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EncodingFilterTest {
    @Mock
    ServletRequest requestMock;
    @Mock
    ServletResponse responseMock;
    @Mock
    FilterChain chainMock;
    private EncodingFilter encodingFilter;

    @BeforeEach
    void setUp() {
        encodingFilter = new EncodingFilter();
    }

    @Test
    void testDoFilter() throws IOException, ServletException {
        encodingFilter.doFilter(requestMock, responseMock, chainMock);

        verify(requestMock).setCharacterEncoding("utf-8");
        verify(responseMock).setContentType("application/json; charset=utf-8");

        verify(chainMock).doFilter(requestMock, responseMock);
    }
}