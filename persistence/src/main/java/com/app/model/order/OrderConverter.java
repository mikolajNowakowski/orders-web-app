package com.app.model.order;

import com.app.model.customer.Customer;
import com.app.model.product.Product;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Function;

public interface OrderConverter {

    Function<Order, LocalDate> toOrderDate = order -> order.orderDate;
    Function<Order, Product> toProduct = order -> order.product;
    Function<Order, Customer> toCustomer = order -> order.customer;
    Function<Order, Month> toMonth = order -> order.orderDate.getMonth();



}
