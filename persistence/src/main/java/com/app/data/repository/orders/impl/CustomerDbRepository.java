package com.app.data.repository.orders.impl;

import com.app.data.repository.generic.AbstractCrudRepository;
import com.app.data.reader.model.db.CustomerDb;
import org.jdbi.v3.core.Jdbi;

public class CustomerDbRepository extends AbstractCrudRepository<CustomerDb,Long> {
    public CustomerDbRepository(Jdbi jdbi) {
        super(jdbi);
    }


}
