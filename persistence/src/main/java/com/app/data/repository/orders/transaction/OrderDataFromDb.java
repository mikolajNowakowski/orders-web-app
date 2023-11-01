package com.app.data.repository.orders.transaction;

import com.app.model.dto.order.OrderDto;

import java.util.List;

public interface OrderDataFromDb<T> {
    List<T> getAll();
    T save(OrderDto orderDto);
    T delete(Long id);
    T findById(Long id);
}
