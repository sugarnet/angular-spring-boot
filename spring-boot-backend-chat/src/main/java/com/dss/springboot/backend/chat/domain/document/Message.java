package com.dss.springboot.backend.chat.domain.document;

import java.io.Serializable;

public class Message implements Serializable {
    private String text;
    private Long date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
