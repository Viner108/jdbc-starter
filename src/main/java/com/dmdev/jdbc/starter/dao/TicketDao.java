package com.dmdev.jdbc.starter.dao;

import com.dmdev.jdbc.starter.entity.TicketEntity;
import com.dmdev.jdbc.starter.exeption.DaoException;
import com.dmdev.jdbc.starter.until.ConnectionManager;

import java.sql.*;

public class TicketDao {
    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
            DELETE from ticket
            where id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO ticket(passenger_no, passager_name, seat_no, cost)
            VALUES (?,?,?,?)
            """;

    public TicketDao() {
    }
    public TicketEntity save(TicketEntity ticketEntity){
        try(Connection connection=ConnectionManager.get();
        PreparedStatement preparedStatement=connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,ticketEntity.getPassengerNo());
            preparedStatement.setString(2,ticketEntity.getPassengerName());
            preparedStatement.setString(3,ticketEntity.getSeatNo());
            preparedStatement.setBigDecimal(4,ticketEntity.getCost());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                ticketEntity.setId(generatedKeys.getLong("id"));
            }
            return ticketEntity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

}
