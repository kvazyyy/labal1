package org.parsers;

import org.source.MissionDataSource;

import java.io.InputStream;

public abstract class AbstractParser<T> implements Parser<T> {
    @Override
    public T parse(MissionDataSource source) throws Exception {
        try (InputStream inputStream = source.getInputStream()) {
            return parseFromStream(inputStream);
        }
    }

    protected abstract T parseFromStream(InputStream inputStream) throws Exception;
}
