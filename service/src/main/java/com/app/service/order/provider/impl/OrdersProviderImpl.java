package com.app.service.order.provider.impl;

import com.app.data.reader.model.filename.OrderDBSource;
import com.app.data.reader.model.filename.OrderFilePath;
import com.app.data.reader.process.OrderDataProcess;
import com.app.data.reader.process.type.ProcessType;
import com.app.model.order.Order;
import com.app.service.order.provider.OrdersProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdersProviderImpl implements OrdersProvider {

    @Value("${process.type}")
    private ProcessType processType;

    @Value("${orders_file_path.json}")
    private String jsonFilePath;

    @Value("${orders_file_path.txt}")
    private String txtFilePath;

    private final OrderDataProcess orderDataTxtProcessImpl;
    private final OrderDataProcess orderDataJsonProcessImpl;
    private final OrderDataProcess orderDataDbProcessImpl;
    private final OrderDBSource orderDBSource;

    @Override
    public List<Order> provide() {
        return switch (processType) {
            case FROM_TXT_FILE_TO_ORDERS -> orderDataTxtProcessImpl.process(new OrderFilePath(txtFilePath));
            case FROM_JSON_FILE_TO_ORDERS -> orderDataJsonProcessImpl.process(new OrderFilePath(jsonFilePath));
            case FROM_DB_TO_ORDERS -> orderDataDbProcessImpl.process(orderDBSource);
        };
    }
}
