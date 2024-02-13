package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.until.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TrensactionRunner {
    public static void main(String[] args) throws SQLException {
        long book = 2;
        String deleteBookSql = "DELETE FROM book where id = ?";
        String deleteAuthorSql = "DELETE from author where book = ?";
        Connection connection= null;
        PreparedStatement deleteBookStatement= null;
        PreparedStatement deleteAuthorStatement=null;
        try{
            connection= ConnectionManager.open();
            deleteBookStatement = connection.prepareStatement(deleteBookSql);
            deleteAuthorStatement = connection.prepareStatement(deleteAuthorSql);
            connection.setAutoCommit(false);
            deleteBookStatement.setLong(1, book);
            deleteAuthorStatement.setLong(1, book);

            deleteAuthorStatement.executeUpdate();
            if(true){
                throw new RuntimeException("Ooooops...");
            }
            deleteBookStatement.executeUpdate();
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
            if(deleteBookStatement!= null){
                deleteBookStatement.close();
            }
            if (deleteAuthorStatement!=null){
                deleteAuthorStatement.close();
            }
        }
    }
}
