package com.app.service.order.provider;
public sealed interface Provider<T> permits OrdersProvider {
    T provide();
}
