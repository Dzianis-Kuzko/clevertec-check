package main.java.ru.clevertec.check.dao.file;

import main.java.ru.clevertec.check.core.dto.ProductDTO;
import main.java.ru.clevertec.check.dao.api.IProductDao;

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

                    return new ProductDTO(productId, description, price, quantityInStock, wholesaleProduct);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
