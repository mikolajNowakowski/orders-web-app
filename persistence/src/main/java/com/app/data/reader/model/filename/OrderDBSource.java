package com.app.data.reader.model.filename;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;

@Data
@RequiredArgsConstructor
public class OrderDBSource implements DataSource{
    private final Jdbi jdbi;

}
