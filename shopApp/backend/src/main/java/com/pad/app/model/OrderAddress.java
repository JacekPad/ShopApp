package com.pad.app.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderAddress implements Serializable {
    

    private String street;
    private String zipCode;
    private String city;
    private String country;
    private String phoneNumber;
    private String email;

}
