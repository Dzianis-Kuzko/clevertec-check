package ru.clevertec.check.controller.web.servlet.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilServletTest {
    @Mock
    HttpServletRequest requestMock;

    @Test
    void testGetId() {
        String paramName = "id";

        when(requestMock.getParameter(paramName)).thenReturn("1");

        long expectedID = 1;

        long actualID = UtilServlet.getId(requestMock, paramName);

        assertEquals(expectedID, actualID);

    }


    @Test
    void testGetRequestBodyAsJson() throws IOException {
        String testJson = "{\"key\":\"value\"}";

        BufferedReader reader = new BufferedReader(new StringReader(testJson));

        when(requestMock.getReader()).thenReturn(reader);

        String result = UtilServlet.getRequestBodyAsJson(requestMock);

        assertEquals(testJson, result);
    }
}