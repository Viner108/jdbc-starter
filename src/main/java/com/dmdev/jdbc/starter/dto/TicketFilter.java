package com.dmdev.jdbc.starter.dto;

public record TicketFilter(int limit, int offset, String passagerName,String seatNo) {
}
