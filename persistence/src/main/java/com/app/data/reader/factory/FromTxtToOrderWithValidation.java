package com.app.data.reader.factory;

import com.app.data.reader.converter.Converter;
import com.app.data.reader.converter.OrderDataConverter;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.loader.txt.OrderTxtDataLoader;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.validator.Validator;
import com.app.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FromTxtToOrderWithValidation implements DataFactory<List<OrderData>, List<Order>>{
    private final Validator<List<OrderData>> orderDataValidator;
    @Override
    public Validator<List<OrderData>> createValidator() {
        return orderDataValidator;
    }

    @Override
    public Converter<List<OrderData>, List<Order>> createConverter() {
        return new OrderDataConverter();
    }

    @Override
    public Loader<List<OrderData>> createLoader() {
        return new OrderTxtDataLoader();
    }
}
