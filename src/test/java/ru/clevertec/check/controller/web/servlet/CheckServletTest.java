package ru.clevertec.check.controller.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.formatter.CheckFormatter;
import ru.clevertec.check.service.api.ICheckService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServletTest {

    @Mock
    private ICheckService checkServiceMock;

    @Mock
    private CheckFormatter checkFormatterMock;

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private PrintWriter writerMock;

    @InjectMocks
    private CheckServlet checkServlet;

    @BeforeEach
    void setUp() throws IOException {
        checkServlet = new CheckServlet();
    }

    @Disabled
    @Test
    void testDoPostSuccess() throws IOException, ServletException {
        String jsonInput = "{ \"json\": \"test\" }";
        CreateCheckDTO createCheckDTO = new CreateCheckDTO();
        Check check = new Check.CheckBuilder().build();
        String formattedCheckCSV = "test check";

        when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));
        when(responseMock.getWriter()).thenReturn(writerMock);
        when(checkFormatterMock.parseCreateCheckDTO(anyString())).thenReturn(createCheckDTO);
        when(checkServiceMock.createCheck(any(CreateCheckDTO.class))).thenReturn(check);
        when(checkFormatterMock.formatCheckForCSV(any(Check.class))).thenReturn(formattedCheckCSV);

        checkServlet.doPost(requestMock, responseMock);

        verify(checkFormatterMock).parseCreateCheckDTO(anyString());
        verify(checkServiceMock).createCheck(any(CreateCheckDTO.class));
        verify(checkFormatterMock).formatCheckForCSV(any(Check.class));
        verify(writerMock).write(formattedCheckCSV);
    }
}