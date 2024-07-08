package ru.clevertec.check.dao.db;

import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductJDBCDao implements IProductDao {
    private DatabaseConfig databaseConfig;

    public ProductJDBCDao(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public ProductDTO get(long id) {
        ProductDTO productDTO = null;

        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, " +
                             "description, " +
                             "price, " +
                             "quantity_in_stock, " +
                             "wholesale_product " +
                             "FROM public.product " +
                             "WHERE id = ?;")) {

            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {
                    productDTO = initializeProduct(rs);
                }

            }
        } catch (SQLException e) {
            throw new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
        }

        return productDTO;
    }

    private ProductDTO initializeProduct(ResultSet rs) throws SQLException {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(rs.getLong("id"));
        productDTO.setDescription(rs.getString("description"));
        productDTO.setPrice(rs.getDouble("price"));
        productDTO.setQuantityInStock(rs.getInt("quantity_in_stock"));
        productDTO.setWholesaleProduct(rs.getBoolean("wholesale_product"));

        return productDTO;
    }
}
