package ru.clevertec.check.controller.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.controller.web.servlet.util.DatabaseConfigUtil;
import ru.clevertec.check.controller.web.servlet.util.UtilServlet;
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.exception.ProductNotFoundException;
import ru.clevertec.check.formatter.ProductFormatter;
import ru.clevertec.check.service.api.IProductService;
import ru.clevertec.check.service.factory.ProductServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private final static String ID_PARAM_NAME = "id";
    private final IProductService productService;

    public ProductServlet() {
        DatabaseConfig databaseConfig = DatabaseConfigUtil.initializeDatabaseConfig();
        this.productService = ProductServiceFactory.getInstance(databaseConfig);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        ProductDTO productDTO = this.productService.get(id);

        if (productDTO == null) {
            throw new ProductNotFoundException();
        }

        writer.write(new ProductFormatter().formatProductForJSON(productDTO));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String json = UtilServlet.getRequestBodyAsJson(req);

        CreateProductDTO createProductDTO = new ProductFormatter().parseCreateProductDTO(json);

        this.productService.create(createProductDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        String json = UtilServlet.getRequestBodyAsJson(req);

        CreateProductDTO createProductDTO = new ProductFormatter().parseCreateProductDTO(json);

        ProductDTO productDTO = new ProductDTO(createProductDTO);
        productDTO.setId(id);

        this.productService.update(productDTO);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = UtilServlet.getId(req, ID_PARAM_NAME);

        this.productService.delete(id);
    }
}
