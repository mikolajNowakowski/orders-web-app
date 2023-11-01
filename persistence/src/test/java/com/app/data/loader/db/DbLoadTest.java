package com.app.data.loader.db;

import com.app.config.AppTestBeansConfig;
import com.app.data.OrderDataProvider;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.loader.db.OrderDbDataLoader;
import com.app.data.reader.model.filename.OrderDBSource;
import com.app.data.reader.model.order.OrderData;
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
public class DbLoadTest {
    @Autowired
    Loader<List<OrderData>> orderDbDataLoader;
    @Autowired
    OrderDBSource orderDBSource;

    @Test
    @DisplayName("When inputted argument is null")
    void test1() {

        Assertions
                .assertThatThrownBy(
                        () -> orderDbDataLoader.
                                load(null)

                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted data is null.");
    }

    @Test
    @DisplayName("When everything is correct and method should generate specified output")
    void test2() {
        Assertions.assertThat(orderDbDataLoader.load(orderDBSource))
                .isEqualTo(List.of(orderDataA, orderDataB, orderDataC));
    }

}
