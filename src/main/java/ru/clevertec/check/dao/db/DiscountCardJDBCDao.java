package ru.clevertec.check.dao.db;

import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
import ru.clevertec.check.dao.api.IDiscountCardDao;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;
import ru.clevertec.check.exception.BadRequestException;
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
    public DiscountCardDTO get(long id) {
        DiscountCardDTO discountCardDTO = null;
        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT id, " +
                             "number, " +
                             "amount " +
                             "FROM public.discount_card " +
                             "WHERE id = ?;")) {

            preparedStatement.setLong(1, (id));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    discountCardDTO = initializeDiscountCard(rs);
                }
            }

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return discountCardDTO;
    }

    @Override
    public DiscountCardDTO create(CreateDiscountCardDTO item) {
        DiscountCardDTO discountCardDTO = new DiscountCardDTO(item);

        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO public.discount_card(" +
                             "number, " +
                             "amount) " +
                             "VALUES ( ?, ?) " +
                             "RETURNING id;")) {

            preparedStatement.setInt(1, Integer.parseInt(item.getNumber()));
            preparedStatement.setInt(2, item.getDiscountAmount());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    discountCardDTO.setId(rs.getLong("id"));
                }
            }

        } catch (SQLException e) {
            throw new BadRequestException();
        }

        return discountCardDTO;
    }

    @Override
    public void update(DiscountCardDTO item) {
        try (Connection connection = DatabaseConnectionFactory.getConnection(databaseConfig);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE public.discount_card SET " +
                             "number = ?, " +
                             "amount = ? " +
                             "WHERE id = ?")) {

            preparedStatement.setInt(1, Integer.parseInt(item.getNumber()));
            preparedStatement.setInt(2, item.getDiscountAmount());
            preparedStatement.setLong(3, item.getId());

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
                     "DELETE FROM public.discount_card WHERE id = ?")) {

            preparedStatement.setLong(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new BadRequestException();
            }

        } catch (SQLException e) {
            throw new BadRequestException();
        }
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
            throw new BadRequestException();
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
