package org.parsers;

import org.model.Mission;
import org.source.MissionDataSource;

import java.util.ArrayList;
import java.util.List;

public class MissionParserFactory {
    private final List<Parser<Mission>> parsers = new ArrayList<>();

    public void registerParser(Parser<Mission> parser) {
        parsers.add(parser);
    }

    public Parser<Mission> getParser(MissionDataSource source) {
        for (Parser<Mission> parser : parsers) {
            if (parser.canParse(source)) {
                return parser;
            }
        }
        throw new IllegalArgumentException("Формат файла не поддерживается: " + source.getIdentifier());
    }
}
