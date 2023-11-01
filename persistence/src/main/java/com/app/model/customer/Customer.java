package com.app.model.customer;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Customer {
    final Long id;
    final String name;
    final String surname;
    final int age;
    final String email;

    private Customer(Long id, String name, String surname, int age, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }

    public boolean hasAgeBetween(int min, int max){
        if(min>max){
            throw new IllegalArgumentException("Min range of customer age cannot be greater than max range");
        }

        return this.age>=min && this.age<=max;
    }

    public boolean hasEmailAs(String email){
        return email.equals(this.email);
    }

    public static Customer of(Long id,String name, String surname, int age, String email){
        return new Customer(id, name,surname,age,email);
    }

}
