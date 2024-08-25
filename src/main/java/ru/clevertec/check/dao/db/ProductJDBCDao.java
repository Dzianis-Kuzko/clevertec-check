package ru.clevertec.check.dao.db;

import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.api.IProductDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;
import ru.clevertec.check.exception.BadRequestException;

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
            throw new BadRequestException();
        }

        return productDTO;
    }

    @Override
    public ProductDTO create(CreateProductDTO item) {
        ProductDTO productDTO = new ProductDTO(item);

        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO public.product(" +
                             "description, " +
                             "price, " +
                             "quantity_in_stock, " +
                             "wholesale_product) " +
                             "VALUES (?, ?, ?, ?) " +
                             "RETURNING id;")) {

            preparedStatement.setString(1, item.getDescription());
            preparedStatement.setDouble(2, item.getPrice());
            preparedStatement.setInt(3, item.getQuantityInStock());
            preparedStatement.setBoolean(4, item.isWholesaleProduct());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    productDTO.setId(rs.getLong("id"));
                }
            }

        } catch (SQLException e) {
            throw new BadRequestException();
        }

        return productDTO;
    }

    @Override
    public void update(ProductDTO item) {
        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE public.product SET " +
                             "description = ?, " +
                             "price = ?, " +
                             "quantity_in_stock = ?, " +
                             "wholesale_product = ? " +
                             "WHERE id = ?")) {

            preparedStatement.setString(1, item.getDescription());
            preparedStatement.setDouble(2, item.getPrice());
            preparedStatement.setInt(3, item.getQuantityInStock());
            preparedStatement.setBoolean(4, item.isWholesaleProduct());
            preparedStatement.setLong(5, item.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new BadRequestException();
            }

        } catch (SQLException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public void delete(long id) {
        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM public.product WHERE id = ?")) {

            preparedStatement.setLong(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new BadRequestException();
            }

        } catch (SQLException e) {
            throw new BadRequestException();
        }
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
