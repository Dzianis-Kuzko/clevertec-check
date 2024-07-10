package ru.clevertec.check.controller.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.controller.web.servlet.util.DatabaseConfigUtil;
import ru.clevertec.check.controller.web.servlet.util.UtilServlet;
import ru.clevertec.check.core.Check;
import ru.clevertec.check.core.dto.CreateCheckDTO;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.formatter.CheckFormatter;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.api.ICheckService;
import ru.clevertec.check.service.factory.DiscountCardServiceFactory;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {
    private final ICheckService checkService;

    public CheckServlet() {
        DatabaseConfig databaseConfig = DatabaseConfigUtil.initializeDatabaseConfig();
        this.checkService = new CheckService(
                ProductServiceFactory.getInstance(databaseConfig),
                DiscountCardServiceFactory.getInstance(databaseConfig));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = UtilServlet.getRequestBodyAsJson(req);

        CreateCheckDTO createCheckDTO = new CheckFormatter().parseCreateCheckDTO(json);

        Check check = this.checkService.createCheck(createCheckDTO);

        String checkCSV = new CheckFormatter().formatCheckForCSV(check);

        PrintWriter writer = resp.getWriter();

        writer.write(checkCSV);
    }
}
