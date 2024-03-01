package com.warehousekeeper.root.util;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponse {
    private String message;
    private Date date;

    public ErrorResponse(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

}
