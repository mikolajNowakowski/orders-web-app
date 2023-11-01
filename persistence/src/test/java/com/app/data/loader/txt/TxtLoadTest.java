package com.app.data.loader.txt;

import com.app.config.AppTestBeansConfig;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.loader.txt.OrderTxtDataLoader;
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
public class TxtLoadTest {

    @Autowired
    Loader<List<OrderData>> orderTxtDataLoader;
    @Value("${orders_file_path.txt}")
    private String ordersTxtPath;

    @Test
    @DisplayName("When inputted argument is null")
    void test1() {

        Assertions
                .assertThatThrownBy(
                        () -> orderTxtDataLoader.
                                load(null)

                ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inputted data is null.");
    }

    @Test
    @DisplayName("When everything is correct and method should generate specified output")
    void test2() {
        Assertions.assertThat(orderTxtDataLoader.load(OrderFilePath.of(ordersTxtPath)))
                .isEqualTo(List.of(orderDataA, orderDataB, orderDataC));
    }

}
