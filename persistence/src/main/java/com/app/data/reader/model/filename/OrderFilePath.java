package com.app.data.reader.model.filename;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderFilePath implements DataSource {
    private final String filePath;

    public static OrderFilePath of(String filePath){
        return new OrderFilePath(filePath);
    }

}
