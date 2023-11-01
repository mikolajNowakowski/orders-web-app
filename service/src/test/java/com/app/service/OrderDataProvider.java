package com.app.service;

import com.app.model.customer.Customer;
import com.app.model.order.Order;
import com.app.model.product.Product;
import com.app.model.product.category.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderDataProvider {


    Order orderA = Order.of(1L,Customer.of(1L,"AAA","AAAA",50,"aaaa@gmail.com"),
            Product.of(1L,"BIKEA", new BigDecimal("15000.00"), Category.A),2,
            LocalDate.of(2035,12,9));

    Order orderB = Order.of(2L,Customer.of(2L,"BBB","BBBB",27,"bbbb@gmail.com"),
            Product.of(2L,"BIKEB", new BigDecimal("19000.00"), Category.B),4,
            LocalDate.of(2027,7,27));

    Order orderC = Order.of(3L,Customer.of(3L,"CCC","CCCC",29,"cccc@gmail.com"),
            Product.of(3L,"BIKEC", new BigDecimal("20000.00"), Category.C),7,
            LocalDate.of(2100,8,27));

    Order orderD = Order.of(4L,Customer.of(4L,"DDD","DDDD",36,"dddd@gmail.com"),
            Product.of(4L,"BIKED", new BigDecimal("21000.00"), Category.A),7,
            LocalDate.of(2024,5,1));
    Order orderE = Order.of(5L,Customer.of(5L,"EEE","EEEE",22,"eeee@gmail.com"),
            Product.of(5L,"BIKEE", new BigDecimal("22000.00"), Category.B),2,
            LocalDate.of(2031,2,27));

    Order orderF = Order.of(6L,Customer.of(6L,"FFF","FFFF",62,"ffff@gmail.com"),
            Product.of(6L,"BIKEF", new BigDecimal("96000.00"), Category.C),16,
            LocalDate.of(2033,11,27));

    Customer customerA = Customer.of(1L,"AAA","AAAA",50,"aaaa@gmail.com");
    Customer customerB = Customer.of(2L,"BBB","BBBB",27,"bbbb@gmail.com");
    Customer customerC = Customer.of(3L,"CCC","CCCC",29,"cccc@gmail.com");
    Customer customerD = Customer.of(4L,"DDD","DDDD",36,"dddd@gmail.com");
    Customer customerE = Customer.of(5L,"EEE","EEEE",22,"eeee@gmail.com");
    Customer customerF = Customer.of(6L,"FFF","FFFF",62,"ffff@gmail.com");





}
