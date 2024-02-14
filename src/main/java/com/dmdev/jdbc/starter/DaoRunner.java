package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.dao.TicketDao;
import com.dmdev.jdbc.starter.entity.TicketEntity;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
        TicketDao ticketDao = TicketDao.getInstance();
        Boolean deleteResult = ticketDao.delete(18L);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        TicketEntity ticketEntity=new TicketEntity();
        ticketEntity.setPassengerNo("63257");
        ticketEntity.setPassengerName("Test");
        ticketEntity.setSeatNo("A18");
        ticketEntity.setCost(BigDecimal.TEN);

        TicketEntity ticket=ticketDao.save(ticketEntity);
        System.out.println(ticket);
    }
}
