package com.app.service;

import com.app.config.AppTestBeansConfig;
import com.app.data.repository.generic.CrudRepository;
import com.app.data.repository.orders.impl.CustomerDbRepository;
import com.app.data.repository.orders.impl.ProductDbRepository;
import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.data.reader.model.db.OrderDb;
import com.app.data.reader.model.order.OrderData;
import com.app.model.dto.order.CustomerDto;
import com.app.model.dto.order.OrderDto;
import com.app.model.dto.order.ProductDto;
import com.app.service.order.OrderService;
import org.assertj.core.api.Assertions;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class SaveTest {

    @Autowired
    @Qualifier("orderServiceSaveTest")
    private OrderService orderService;
    @Autowired
    private OrderDataFromDbImpl orderDataFromDbImpl;
    @Autowired
    @Qualifier("productDbRepositorySaveTest")
    private ProductDbRepository productDbRepository;
    @Autowired
    @Qualifier("customerDbRepositorySaveTest")
    private CustomerDbRepository customerDbRepository;
    @Autowired
    private Jdbi jdbi;

    @Test
    @DisplayName("When inputted data is correct.")
    void testN() {
        var orderDto1 = new OrderDto(new CustomerDto("AAAA", "AAAAA", "20", "aaaaaa@gmail.com"),
                new ProductDto("AAAAA", "150.00", "A"),
                "02-02-2030",
                "4");

        var orderDto2 = new OrderDto(new CustomerDto("AAAAA", "AAAAAA", "20", "aaaaaaa@gmail.com"),
                new ProductDto("AAAAAA", "152.00", "A"),
                "02-02-2030",
                "4");

        orderService.save(orderDto1);

        var numberOfProductsBefore = productDbRepository.findAll().size();
        var numberOfCustomersBefore = customerDbRepository.findAll().size();

        var lastId = getLastAddedOrderData();

        orderService.save(orderDto2);

        var addedOrder = getLastAddedOrderData();

        var numberOfProductsAfter = productDbRepository.findAll().size();
        var numberOfCustomersAfter = customerDbRepository.findAll().size();

        orderDataFromDbImpl.delete(lastId.getId());
        orderDataFromDbImpl.delete(addedOrder.getId());

        productDbRepository.deleteAllById(List.of(lastId.getProduct().getId(),addedOrder.getProduct().getId()));
        customerDbRepository.deleteAllById(List.of(lastId.getCustomer().getId(),addedOrder.getCustomer().getId()));

        Assertions.assertThat(lastId.getId()+1).isEqualTo(addedOrder.getId());
        Assertions.assertThat(numberOfProductsBefore+1).isEqualTo(numberOfProductsAfter);
        Assertions.assertThat(numberOfCustomersBefore+1).isEqualTo(numberOfCustomersAfter);


    }

    private OrderData getLastAddedOrderData() {
        var getAddedDataSql = "select * from orders order by id desc limit :n";
        return CrudRepository.findOneWithMapping(jdbi, getAddedDataSql, 1, ((rs, ctx) -> OrderDb.builder()
                .id(rs.getLong("id"))
                .orderDate(rs.getString("order_date"))
                .quantity(rs.getInt("quantity"))
                .productDb(productDbRepository.findById(rs.getLong("product_id")).orElseThrow())
                .customerDb(customerDbRepository.findById(rs.getLong("customer_id")).orElseThrow())
                .build())).toOrderData();
    }
}
