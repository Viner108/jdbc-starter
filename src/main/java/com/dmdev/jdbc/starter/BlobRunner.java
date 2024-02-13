package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.until.ConnectionManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.*;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
//        saveImage();
        getImage();
    }
    private static void getImage() throws SQLException, IOException {
        String sql = """
                SELECT image
                from ticket
                where id = ?
                """;
        try(Connection connection=ConnectionManager.open();
        PreparedStatement preparedStatement =connection.prepareStatement(sql)){
            preparedStatement.setInt(1,2);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                byte[] image = resultSet.getBytes("image");
                Files.write(Path.of("src/main/resources","new_ticket.jpg"), image, StandardOpenOption.CREATE);

            }
        }
    }

    private static void saveImage() throws SQLException, IOException {
        String sql= """
                UPDATE ticket
                set image = ?
                where id = 2
                """;
        try(Connection connection= ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setBytes(1,Files.readAllBytes(Path.of("src/", "main/resources/ticket.jpg")));
            preparedStatement.executeUpdate();
        }
    }
/*

    private static void saveImage() throws SQLException, IOException {
        String sql= """
                UPDATE ticket
                set image = ?
                where id = 1
                """;
        try(Connection connection= ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            connection.setAutoCommit(false);
            Blob blob = connection.createBlob();
            blob.setBytes(1, Files.readAllBytes(Path.of("resources", "ticket.jpg")));
            preparedStatement.setBlob(1,blob);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
*/
}
