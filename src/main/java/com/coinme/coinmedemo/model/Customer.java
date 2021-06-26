package com.coinme.coinmedemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

    private final int id;
    private final String name;

    public Customer(@JsonProperty("id") int id,
                    @JsonProperty("name") String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
