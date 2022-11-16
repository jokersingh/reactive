package com.example.reactive.domain;

public class Data {

    public Data(String name, String shortCode) {
        this.name = name;
        this.shortCode = shortCode;
    }

    private String name;
    private String shortCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
