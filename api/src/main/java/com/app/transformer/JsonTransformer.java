package com.app.transformer;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

@Component
@RequiredArgsConstructor
public class JsonTransformer implements ResponseTransformer {
    private final Gson gson;

    @Override
    public String render(Object o) throws Exception {
        return gson.toJson(o);
    }
}
