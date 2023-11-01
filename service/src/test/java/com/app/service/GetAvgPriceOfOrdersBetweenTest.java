package com.app.service;


import com.app.config.AppTestBeansConfig;
import com.app.service.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.math.BigDecimal;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public final class GetAvgPriceOfOrdersBetweenTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When LocalFrom parameter is null")
    void test1(){
        Assertions
                .assertThatThrownBy(() -> orderService.getAvgPriceOfOrdersOrderedBetween(null, LocalDate.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One of inputted argument is null");
    }

    @Test
    @DisplayName("When LocalTo parameter is null")
    void test2(){
        Assertions
                .assertThatThrownBy(() -> orderService.getAvgPriceOfOrdersOrderedBetween(LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("One of inputted argument is null");
    }

    @Test
    @DisplayName("When LocalDate from parameter is earlier than to.")
    void test3(){
        Assertions
                .assertThatThrownBy(() -> orderService.getAvgPriceOfOrdersOrderedBetween(LocalDate.now(), LocalDate.now().minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The start of te date range cannot be after the end");
    }


    @Test
    @DisplayName("When input arguments are correct and method must generate specified output.")
    void test4(){
        Assertions
                .assertThat(orderService.getAvgPriceOfOrdersOrderedBetween(LocalDate.of(2030,5,10),LocalDate.of(2050,10,11)))
                .isEqualTo(new BigDecimal("536666.67"));

    }




}
