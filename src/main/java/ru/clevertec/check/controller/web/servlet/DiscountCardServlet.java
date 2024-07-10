package ru.clevertec.check.controller.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.controller.web.servlet.util.DatabaseConfigUtil;
import ru.clevertec.check.controller.web.servlet.util.UtilServlet;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.formatter.DiscountCardFormatter;
import ru.clevertec.check.service.api.IDiscountCardService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/discountcards")
public class DiscountCardServlet extends HttpServlet {
    private final static String ID_PARAM_NAME = "id";
    private final IDiscountCardService discountCardService;

    public DiscountCardServlet() {
        DatabaseConfig databaseConfig = DatabaseConfigUtil.initializeDatabaseConfig();
        this.discountCardService = DiscountCardServiceFactory.getInstance(databaseConfig);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        DiscountCardDTO discountCard = this.discountCardService.get(id);

        if (discountCard == null) {
            throw new BadRequestException();
        }

        writer.write(new DiscountCardFormatter().formatDiscountCardForJSON(discountCard));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = UtilServlet.getRequestBodyAsJson(req);

        CreateDiscountCardDTO createDiscountCardDTO = new DiscountCardFormatter().parseCreateDiscountCardDTO(json);

        this.discountCardService.create(createDiscountCardDTO);

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        this.discountCardService.delete(id);

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        String json = UtilServlet.getRequestBodyAsJson(req);

        CreateDiscountCardDTO createDiscountCardDTO = new DiscountCardFormatter().parseCreateDiscountCardDTO(json);

        DiscountCardDTO discountCardDTO = new DiscountCardDTO(createDiscountCardDTO);
        discountCardDTO.setId(id);

        this.discountCardService.update(discountCardDTO);
    }
}
