package ru.clevertec.check.dao.file;

import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProductFileDao implements IProductDao {
    private String filePath;

    public ProductFileDao(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public ProductDTO get(long id) {
        ProductDTO productDTO = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                long productId = Long.parseLong(values[0]);
                if (productId == id) {
                    String description = values[1];
                    double price = Double.parseDouble(values[2]);
                    int quantityInStock = Integer.parseInt(values[3]);
                    boolean wholesaleProduct = values[4].equalsIgnoreCase("true");

                    productDTO = new ProductDTO(productId, description, price, quantityInStock, wholesaleProduct);
                }
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
        }

        return productDTO;
    }
}
