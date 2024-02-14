package com.dmdev.jdbc.starter.exeption;

public class DaoException extends RuntimeException{
    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
