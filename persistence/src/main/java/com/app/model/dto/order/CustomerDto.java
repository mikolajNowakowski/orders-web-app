package com.app.model.dto.order;


import com.app.data.reader.model.db.CustomerDb;

public record CustomerDto(String name, String surname, String age, String email) {

    public CustomerDb toCustomerDb(){
        return new CustomerDb(null,name,surname,Integer.parseInt(age),email);
    }

}

