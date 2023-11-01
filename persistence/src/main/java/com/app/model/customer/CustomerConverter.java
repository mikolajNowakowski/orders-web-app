package com.app.model.customer;

import java.util.function.Function;

public interface CustomerConverter {
    Function<Customer,String> toMail = customer -> customer.email;
    Function<Customer,String> toName = customer -> customer.name;
    Function<Customer,String> toSurname = customer -> customer.surname;
    Function<Customer,String> toNameSurname = customer -> "%s %s".formatted(customer.name,customer.surname);
    Function<Customer, Integer> toAge = customer -> customer.age;
}
