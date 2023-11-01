package com.app.data.loader.json;

import com.app.config.AppTestBeansConfig;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.model.filename.OrderFilePath;
import com.app.data.reader.model.order.OrderData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.app.data.OrderDataProvider.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application_test.properties")
public class JsonLoadTest {
    @Autowired
    Loader<List<OrderData>> ordersJsonDataLoader;
    @Value("${orders_file_path.json}")
    private String ordersJsonPath;

    @Test
    @DisplayName("When inputted argument is null")
    void test1(){

        Assertions
                .assertThatThrownBy(
                        () -> ordersJsonDataLoader.
                                load(null)

                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted data is null.");
    }

    @Test
    @DisplayName("When everything is correct and method should generate specified output")
    void test2(){
        Assertions.assertThat(ordersJsonDataLoader.load(OrderFilePath.of(ordersJsonPath)))
                .isEqualTo(List.of(orderDataA,orderDataB,orderDataC));
    }
}
