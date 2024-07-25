package com.example.forwardme.Data;

public class Country {
    private String name;
    private String code;
    private int flagResource;

    public Country(String name, String code, int flagResource) {
        this.name = name;
        this.code = code;
        this.flagResource = flagResource;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getFlagResource() {
        return flagResource;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}

