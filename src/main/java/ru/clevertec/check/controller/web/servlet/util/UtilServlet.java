package ru.clevertec.check.controller.web.servlet.util;

import jakarta.servlet.http.HttpServletRequest;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class UtilServlet {
    public static long getId(HttpServletRequest req, String param) {
        String idParam = req.getParameter(param);
        if (idParam == null || idParam.isEmpty()) {
            throw new BadRequestException();
        }
        try {
            return Long.parseLong(idParam);

        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
    }

    public static String getRequestBodyAsJson(HttpServletRequest req) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new BadRequestException();
        }

        return stringBuilder.toString();
    }



}
