package com.app.data.reader.loader.json;

import com.app.data.reader.loader.Loader;
import com.app.data.reader.loader.json.json_loader.FromJsonToObjectLoader;
import com.app.data.reader.model.filename.DataSource;
import com.app.data.reader.model.filename.OrderFilePath;
import com.app.data.reader.model.order.OrderData;
import com.google.gson.Gson;

import java.util.*;

public final class OrdersJsonDataLoader extends FromJsonToObjectLoader<List<OrderData>> implements Loader<List<OrderData>> {
    public OrdersJsonDataLoader(Gson gson) {
        super(gson);
    }

    @Override
    public List<OrderData> load(DataSource dataSource) {
        if(dataSource==null){
            throw new IllegalArgumentException("Inputted data is null.");
        }

        var filePath = (OrderFilePath)dataSource;
        return loadObject(filePath.getFilePath());
    }
}
