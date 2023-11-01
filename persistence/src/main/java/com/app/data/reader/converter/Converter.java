package com.app.data.reader.converter;

public sealed interface Converter<S,T> permits OrderDataConverter {
    T convert(S data);

}
