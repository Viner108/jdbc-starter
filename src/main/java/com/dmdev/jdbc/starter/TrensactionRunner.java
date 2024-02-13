package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.until.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TrensactionRunner {
    public static void main(String[] args) throws SQLException {
        long book = 2;
        String deleteBookSql = "DELETE FROM book where id = "+book;
        String deleteAuthorSql = "DELETE from author where book = "+book;
        Connection connection= null;
//        PreparedStatement deleteBookStatement= null;
//        PreparedStatement deleteAuthorStatement=null;
        Statement statement =null;
        try{
            connection= ConnectionManager.open();
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            statement.addBatch(deleteAuthorSql);
            statement.addBatch(deleteBookSql);

            int[] ints = statement.executeBatch();
//            deleteBookStatement = connection.prepareStatement(deleteBookSql);
//            deleteAuthorStatement = connection.prepareStatement(deleteAuthorSql);
//            deleteBookStatement.setLong(1, book);
//            deleteAuthorStatement.setLong(1, book);
//            int deleteAuthorResult = deleteAuthorStatement.executeUpdate();
//            if(true){
//                throw new RuntimeException("Ooooops...");
//            }
//            int deleteBookResult = deleteBookStatement.executeUpdate();
            connection.commit();
        }catch (Exception e){
            if (connection!= null){
                connection.rollback();
            }
            throw e;
        }finally {
            if (connection!= null){
                connection.close();
            }
            if (statement!=null){
                statement.close();
            }
//            if(deleteBookStatement!= null){
//                deleteBookStatement.close();
//            }
//            if (deleteAuthorStatement!=null){
//                deleteAuthorStatement.close();
//            }
        }
    }
}
