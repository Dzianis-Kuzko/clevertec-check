package ru.clevertec.check.dao.db;

import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.dao.api.IDiscountCardDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;
import ru.clevertec.check.exception.ExceptionMessage;
import ru.clevertec.check.exception.InternalServerErrorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountCardJDBCDao implements IDiscountCardDao {
    private DatabaseConfig databaseConfig;

    public DiscountCardJDBCDao(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public DiscountCardDTO get(String number) {
        DiscountCardDTO discountCardDTO = null;
        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, " +
                             "number, " +
                             "amount " +
                             "FROM public.discount_card " +
                             "WHERE number = ?;")) {

            preparedStatement.setInt(1, Integer.parseInt(number));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    discountCardDTO = initializeDiscountCard(rs);
                }
            }

        } catch (SQLException e) {
            throw new InternalServerErrorException(ExceptionMessage.INTERNAL_SERVER_ERROR.getMessage(), e);
        }

        return discountCardDTO;
    }


    private DiscountCardDTO initializeDiscountCard(ResultSet rs) throws SQLException {
        DiscountCardDTO discountCardDTO = new DiscountCardDTO();

        discountCardDTO.setId(rs.getInt("id"));
        discountCardDTO.setNumber(rs.getString("number"));
        discountCardDTO.setDiscountAmount(rs.getInt("amount"));

        return discountCardDTO;
    }
}
