package com.app.data.reader.loader.db;

import com.app.data.reader.loader.Loader;
import com.app.data.repository.orders.transaction.impl.OrderDataFromDbImpl;
import com.app.data.reader.model.filename.DataSource;
import com.app.data.reader.model.order.OrderData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderDbDataLoader implements Loader<List<OrderData>> {

    private final OrderDataFromDbImpl orderDataFromDb;

    @Override
    public List<OrderData> load(DataSource dataSource) {

        if(dataSource==null){
            throw new IllegalArgumentException("Inputted data is null.");
        }

        return orderDataFromDb.getAll();
    }
}
