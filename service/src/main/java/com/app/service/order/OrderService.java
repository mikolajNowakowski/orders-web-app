package com.app.service.order;

import com.app.model.comparing_type.ComparingType;
import com.app.model.customer.Customer;
import com.app.model.dto.order.OrderDto;
import com.app.model.dto.service.SendEmailDto;
import com.app.model.order.Order;
import com.app.model.product.Product;
import com.app.model.product.category.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public  interface OrderService  {
    public List<Order> getAll();
    BigDecimal getAvgPriceOfOrdersOrderedBetween(LocalDate from, LocalDate to);

    Map<Category, List<Product>> mostExpensiveProductFromEachCategory();

    List<Customer> getCustomersWhoPayTheMost();

    SendEmailDto sendInfoMail(String email);

    List<LocalDate> getDateWithMinMaxPurchases(ComparingType comparingType);

    BigDecimal getPricesOfAllOrdersWithDiscount();

    int numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(int n, String filepath);

    List<Category> getMostPopularCategory();

    LinkedHashMap<Month, Integer> monthsWithNumberOfOrderedProducts();

    Map<Month, List<Category>> monthsWithTheMostPopularCategory();

    OrderDto save(OrderDto orderDto);

}
