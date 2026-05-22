package org.source;

import java.io.InputStream;

public interface MissionDataSource {
    InputStream getInputStream() throws Exception;

    String getIdentifier();

    boolean isValid();
}
