package com.dmdev.jdbc.starter.dao;

import com.dmdev.jdbc.starter.dto.TicketFilter;
import com.dmdev.jdbc.starter.entity.TicketEntity;
import com.dmdev.jdbc.starter.exeption.DaoException;
import com.dmdev.jdbc.starter.until.ConnectionManager;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

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
    private static final String UPDATE_SQL = """
            UPDATE ticket
            set passenger_no = ?,
                passager_name= ?,
                seat_no = ?,
                cost = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
            passenger_no,
            passager_name,
            seat_no,
            cost
            from ticket
            """;
    private static final String FIND_BY_ID = FIND_ALL_SQL + """
            where id = ?
            """;
    public TicketDao() {
    }
    public List<TicketEntity> findAll(TicketFilter filter){
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if(filter.seatNo()!=null){
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" +filter.seatNo() + "%");
        }
        if(filter.passagerName()!=null){
            whereSql.add("passager_name = ?");
            parameters.add(filter.passagerName());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        String where = whereSql.stream()
                .collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        String sql = FIND_ALL_SQL + where;
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i+1, parameters.get(i));
            }
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketEntity> ticketEntities = new ArrayList<>();
            while (resultSet.next()){
                ticketEntities.add(buildTicket(resultSet));
            }
            return ticketEntities;
        }catch (SQLException e) {
            throw new DaoException(e);
        }

    }
    public List<TicketEntity> findAll(){
        try(Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement=connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<TicketEntity> ticketEntities = new ArrayList<>();
            while (resultSet.next()){
                ticketEntities.add(buildTicket(resultSet));
            }
            return ticketEntities;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Optional<TicketEntity> findById(Long id){
        try(Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement=connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            TicketEntity ticketEntity= null;
            if(resultSet.next()){
                ticketEntity = buildTicket(resultSet);
            }
            return Optional.ofNullable(ticketEntity);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static TicketEntity buildTicket(ResultSet resultSet) throws SQLException {
        return new TicketEntity(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passager_name"),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public void update(TicketEntity ticketEntity){
        try(Connection connection=ConnectionManager.get();
            PreparedStatement preparedStatement=connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1,ticketEntity.getPassengerNo());
            preparedStatement.setString(2,ticketEntity.getPassengerName());
            preparedStatement.setString(3,ticketEntity.getSeatNo());
            preparedStatement.setBigDecimal(4,ticketEntity.getCost());
            preparedStatement.setLong(5,ticketEntity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
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
