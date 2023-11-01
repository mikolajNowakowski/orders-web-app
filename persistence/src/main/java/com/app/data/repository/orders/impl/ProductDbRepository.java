package com.app.data.repository.orders.impl;

import com.app.data.repository.generic.AbstractCrudRepository;
import com.app.data.reader.model.db.ProductDb;
import org.jdbi.v3.core.Jdbi;

public class ProductDbRepository extends AbstractCrudRepository<ProductDb,Long> {
    public ProductDbRepository(Jdbi jdbi) {
        super(jdbi);
    }
}
