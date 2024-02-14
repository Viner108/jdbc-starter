package com.dmdev.jdbc.starter.dao;

public class TicketDao {
    private static final TicketDao INSTANCE = new TicketDao();

    public TicketDao() {
    }
    public static TicketDao getInstance(){
        return INSTANCE;
    }

}
