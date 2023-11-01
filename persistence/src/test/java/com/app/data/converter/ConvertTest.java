package com.app.data.converter;


import com.app.config.AppTestBeansConfig;
import com.app.data.reader.converter.Converter;
import com.app.data.reader.model.order.OrderData;
import com.app.model.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.app.data.OrderDataProvider.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class ConvertTest {

    @Autowired
    private Converter<List<OrderData>, List<Order>> converter;

    @Test
    @DisplayName("When convert() method is called with correct argument.")
    void test1() {

        Assertions
                .assertThatThrownBy(
                        () -> converter.
                                convert(null)

                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted data is null.");
    }

    @Test
    @DisplayName("When convert() method is called with correct argument.")
    void test2() {
        Assertions
                .assertThat(converter.convert(List.of(orderDataA, orderDataB, orderDataC)))
                .isEqualTo(List.of(orderA, orderB, orderC));
    }


    @Test
    @DisplayName("When convert() method is called with empty argument.")
    void test3() {
        Assertions
                .assertThat(converter.convert(List.of()))
                .isEmpty();
    }

}
