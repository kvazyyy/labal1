package org.parsers;

import org.source.MissionDataSource;

public interface Parser<T> {
    T parse(MissionDataSource source) throws Exception;

    boolean canParse(MissionDataSource source);
}
