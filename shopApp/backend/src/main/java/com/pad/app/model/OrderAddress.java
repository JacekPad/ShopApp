package com.pad.app.model;

import lombok.Data;

@Data
public class OrderAddress {
    

    private String street;
    private String zipCode;
    private String city;
    private String country;
    private String phoneNumber;
    private String email;

}
