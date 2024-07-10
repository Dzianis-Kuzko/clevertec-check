package ru.clevertec.check.controller.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.exception.ProductNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/*")
public class ExceptionHandlingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        PrintWriter writer = response.getWriter();

        try {
            chain.doFilter(request, response);
        } catch (BadRequestException e) {
            writer.write(ExceptionMessage.BAD_REQUEST.getMessage());
        } catch (NotEnoughMoneyException e) {
            writer.write(ExceptionMessage.NOT_ENOUGH_MONEY.getMessage());
        } catch (ProductNotFoundException e) {
            writer.write(ExceptionMessage.PRODUCT_NOT_FOUND.getMessage());
        } catch (InternalServerErrorException e) {
            writer.write(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage());
        } catch (Exception e) {
            writer.write(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

}
