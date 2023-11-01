package com.app.data.reader.converter;

import com.app.data.reader.model.order.OrderData;
import com.app.model.order.Order;

import java.util.*;

public final class OrderDataConverter implements Converter<List<OrderData>, List<Order>> {


    @Override
    public List<Order> convert(List<OrderData> data) {
        if(data==null){
            throw new IllegalArgumentException("Inputted data is null.");
        }

        return data
                .stream()
                .map(OrderData::toOrder)
                .toList();
    }
}
