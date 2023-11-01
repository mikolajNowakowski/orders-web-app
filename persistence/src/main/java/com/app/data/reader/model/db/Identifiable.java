package com.app.data.reader.model.db;

public sealed interface Identifiable permits CustomerDb, OrderDb, ProductDb {
    Long getId();
}
