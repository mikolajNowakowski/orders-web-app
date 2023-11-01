package com.app.data.reader.process;

import com.app.data.reader.converter.Converter;
import com.app.data.reader.factory.DataFactory;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.model.filename.DataSource;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.validator.Validator;
import com.app.model.order.Order;

import java.util.List;

public final class OrderDataTxtProcessImpl implements OrderDataProcess {

    private final Loader<List<OrderData>> loader;
    private final Validator<List<OrderData>> validator;
    private final Converter<List<OrderData>, List<Order>> converter;

    public OrderDataTxtProcessImpl(DataFactory<List<OrderData>, List<Order>> dataFactory) {
        this.loader = dataFactory.createLoader();
        this.validator = dataFactory.createValidator();
        this.converter = dataFactory.createConverter();
    }

    @Override
    public List<Order> process(DataSource dataSource) {
        if(dataSource == null){
            throw new IllegalArgumentException("Inputted argument is null.");
        }

        var loadedData = loader.load(dataSource);
        var validatedData = validator.validate(loadedData);
        return converter.convert(validatedData);
    }
}
