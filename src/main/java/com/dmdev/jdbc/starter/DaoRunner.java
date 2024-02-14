package com.dmdev.jdbc.starter;

import com.dmdev.jdbc.starter.dao.TicketDao;
import com.dmdev.jdbc.starter.entity.TicketEntity;

import javax.xml.stream.events.DTD;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        List<TicketEntity> tickets = TicketDao.getInstance().findAll();
        System.out.println(tickets);
    }

    private static void updateById() {
        TicketDao ticketDao = TicketDao.getInstance();
        Optional<TicketEntity> maybeTicket = ticketDao.findById(2L);
        System.out.println(maybeTicket);

        maybeTicket.ifPresent(ticketEntity1 -> {
                    ticketEntity1.setCost(BigDecimal.valueOf(188.88));
                    ticketDao.update(ticketEntity1);
                }
        );
    }

    private static void deleteById() {
        TicketDao ticketDao = TicketDao.getInstance();
        Boolean deleteResult = ticketDao.delete(18L);
        System.out.println(deleteResult);
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setPassengerNo("63257");
        ticketEntity.setPassengerName("Test");
        ticketEntity.setSeatNo("A18");
        ticketEntity.setCost(BigDecimal.TEN);

        TicketEntity ticket = ticketDao.save(ticketEntity);
        System.out.println(ticket);
    }
}
