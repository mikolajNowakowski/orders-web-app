package com.app.service;

import com.app.config.AppTestBeansConfig;
import com.app.model.comparing_type.ComparingType;
import com.app.service.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;



@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class GetDateWithMinMaxPurchasesTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("When inputted argument is null")
    void test1() {
        Assertions.assertThatThrownBy(() -> orderService.getDateWithMinMaxPurchases(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted argument is null.");
    }

    @ParameterizedTest
    @EnumSource(ComparingType.class)
    @DisplayName("When getDateWithMinMaxPurchases() method is called with correct arguments.")
    void test2(ComparingType comparingType) {
        Assertions.assertThat(orderService.getDateWithMinMaxPurchases(comparingType))
                .isEqualTo(List.of(
                        LocalDate.of(2033,11,27),
                        LocalDate.of(2031,2,27),
                        LocalDate.of(2027,7,27),
                        LocalDate.of(2035,12,9),
                        LocalDate.of(2100,8,27),
                        LocalDate.of(2024,5,1)));
    }
}
