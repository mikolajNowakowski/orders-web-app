package com.app.data;

import com.app.data.reader.model.customer.CustomerData;
import com.app.data.reader.model.db.CustomerDb;
import com.app.data.reader.model.db.OrderDb;
import com.app.data.reader.model.db.ProductDb;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.model.product.ProductData;
import com.app.data.reader.model.product.category.CategoryData;
import com.app.model.customer.Customer;
import com.app.model.order.Order;
import com.app.model.product.Product;
import com.app.model.product.category.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderDataProvider {

    OrderData orderDataA = OrderData.of(1L,CustomerData.of(1L,"AAA", "AAAA", "50", "aaaa@gmail.com"),
            ProductData.of(1L,"BIKEA", "15000.00", CategoryData.A), "2", "17-07-2035");

    OrderData orderDataB = OrderData.of(2L,CustomerData.of(2L,"BBB", "BBBB", "27", "bbbb@gmail.com"),
            ProductData.of(2L,"BIKEB", "19000.00", CategoryData.B), "4", "27-07-2035");

    OrderData orderDataC = OrderData.of(3L,CustomerData.of(3L,"CCC", "CCCC", "29", "cccc@gmail.com"),
            ProductData.of(3L,"BIKEC", "20000.00", CategoryData.C), "7", "27-08-2035");



    Order orderA = Order.of(1L,Customer.of(1L,"AAA","AAAA",50,"aaaa@gmail.com"),
            Product.of(1L,"BIKEA", new BigDecimal("15000.00"), Category.A),2,
            LocalDate.of(2035,7,17));

    Order orderB = Order.of(2L,Customer.of(2L,"BBB","BBBB",27,"bbbb@gmail.com"),
            Product.of(2L,"BIKEB", new BigDecimal("19000.00"), Category.B),4,
            LocalDate.of(2035,7,27));

    Order orderC = Order.of(3L,Customer.of(3L,"CCC","CCCC",29,"cccc@gmail.com"),
            Product.of(3L,"BIKEC", new BigDecimal("20000.00"), Category.C),7,
            LocalDate.of(2035,8,27));



    OrderDb orderDbA = OrderDb.of(1L,CustomerDb.of(1L,"AAA","AAAA",50,"aaaa@gmail.com"),
            ProductDb.of(1L,"BIKEA",new BigDecimal("15000.00"),"A"),2,"17-07-2035");

    OrderDb orderDbB = OrderDb.of(2L,CustomerDb.of(2L,"BBB","BBBB",27,"bbbb@gmail.com"),
            ProductDb.of(2L,"BIKEB",new BigDecimal("19000.00"),"B"),4,"27-07-2035");

    OrderDb orderDbC = OrderDb.of(3L,CustomerDb.of(3L,"CCC","CCCC",29,"cccc@gmail.com"),
            ProductDb.of(3L,"BIKEC",new BigDecimal("20000.00"),"C"),7,"27-08-2035");





}
