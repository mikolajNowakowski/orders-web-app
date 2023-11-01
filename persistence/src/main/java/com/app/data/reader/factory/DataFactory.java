package com.app.data.reader.factory;

import com.app.data.reader.converter.Converter;
import com.app.data.reader.loader.Loader;
import com.app.data.reader.validator.Validator;

public interface DataFactory<S,T> {
    Validator<S> createValidator();
    Converter<S,T> createConverter();
    Loader<S> createLoader();

}
