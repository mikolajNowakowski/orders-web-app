package com.app.service;


import com.app.config.AppTestBeansConfig;
import com.app.model.customer.Customer;
import com.app.service.order.OrderService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class NumberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJsonTest {

    @Autowired
    private OrderService orderService;
@Value("${Number_of_customers.json_output_file_path}")
    private String outputJsonFile;

    @Test
    @DisplayName("When inputted number of purchases is negative.")
    void test1() {
        Assertions.assertThatThrownBy(() ->
                        orderService.numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(-1,
                                "./src/test/resources/output.json"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Number of purchases of the product must be positive.");
    }

    @Test
    @DisplayName("When inputted path to json file is null")
    void test2() {
        Assertions.assertThatThrownBy(() ->
                        orderService.numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(1,
                                null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted json file path is null.");
    }

    @Test
    @DisplayName("When inputted arguments are correct and method() must write specified data into json file.")
    void test3() {


        orderService.numberOfCustomersWhoBoughtTheSameProductSpecifiedTimesAndWriteToJson(2, outputJsonFile);

        Assertions.assertThat(
                      customerDataLoader())
                .isEqualTo(List.of(Customer.of(1L,"AAA","AAAA",50,"aaaa@gmail.com"),
                        Customer.of(5L,"EEE","EEEE",22,"eeee@gmail.com")));
    }


    private List<Customer> customerDataLoader() {
        Gson gson = new Gson();
        List<Customer> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(outputJsonFile))) {
            data = gson.fromJson(reader, new TypeToken<List<Customer>>() {
            }.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
