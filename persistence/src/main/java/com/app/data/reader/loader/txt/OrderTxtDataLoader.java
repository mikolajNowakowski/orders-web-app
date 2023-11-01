package com.app.data.reader.loader.txt;

import com.app.data.reader.loader.Loader;
import com.app.data.reader.model.customer.CustomerData;
import com.app.data.reader.model.filename.DataSource;
import com.app.data.reader.model.filename.OrderFilePath;
import com.app.data.reader.model.order.OrderData;
import com.app.data.reader.model.product.ProductData;
import com.app.data.reader.model.product.category.CategoryData;


import java.util.List;

public final class OrderTxtDataLoader implements Loader<List<OrderData>> {
    @Override
    public List<OrderData> load(DataSource dataSource) {
        if(dataSource==null){
            throw new IllegalArgumentException("Inputted data is null.");
        }
        var filePath = (OrderFilePath)dataSource;

        return Loader.read(filePath.getFilePath(), this::lineToORderData);
    }

    private OrderData lineToORderData(String line) {
        var items = line.split(",");

        if (items.length != 12) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
        return new OrderData(Long.parseLong(items[0]),new CustomerData(Long.parseLong(items[1]),items[2], items[3], items[4], items[5]),
                new ProductData(Long.parseLong(items[6]),items[7], items[8], CategoryData.valueOf(items[9])),
                items[10], items[11]);
    }
}
