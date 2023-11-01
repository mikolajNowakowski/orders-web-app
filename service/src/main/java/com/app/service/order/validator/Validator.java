package com.app.service.order.validator;

import java.util.Map;

public interface Validator<T> {

    Map<String,String> validate(T t);

}
