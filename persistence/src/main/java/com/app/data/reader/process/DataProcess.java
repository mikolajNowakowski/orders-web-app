package com.app.data.reader.process;

import com.app.data.reader.model.filename.DataSource;

public interface DataProcess<T> {
    T process(DataSource dataSource);
}
