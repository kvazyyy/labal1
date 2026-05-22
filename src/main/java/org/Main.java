package org;

import org.model.Mission;
import org.parsers.MissionJsonParser;
import org.parsers.MissionParserFactory;
import org.parsers.MissionTxtParser;
import org.parsers.MissionXmlParser;
import org.parsers.Parser;
import org.source.FileDataSource;
import org.source.MissionDataSource;
import org.viewer.CUI;
import org.viewer.printer.BasicMissionDecorator;
import org.viewer.printer.BasicMissionPrinter;
import org.viewer.printer.DetailedMissionDecorator;
import org.viewer.printer.ExtraFieldsMissionDecorator;
import org.viewer.printer.MissionPrinter;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        MissionParserFactory parserFactory = createParserFactory();

        if (args.length > 0) {
            parseFilesFromArguments(args, parserFactory);
            return;
        }

        new CUI(parserFactory).showInterface();
    }

    private static MissionParserFactory createParserFactory() {
        MissionParserFactory parserFactory = new MissionParserFactory();
        parserFactory.registerParser(new MissionJsonParser());
        parserFactory.registerParser(new MissionXmlParser());
        parserFactory.registerParser(new MissionTxtParser());
        return parserFactory;
    }

    private static void parseFilesFromArguments(String[] args, MissionParserFactory parserFactory) {
        MissionPrinter printer = new ExtraFieldsMissionDecorator(
                new DetailedMissionDecorator(new BasicMissionDecorator(new BasicMissionPrinter()))
        );
        for (String path : args) {
            MissionDataSource source = new FileDataSource(new File(path));
            System.out.println("\nФайл: " + path);
            if (!source.isValid()) {
                System.out.println("Ошибка: файл не найден или недоступен.");
                continue;
            }
            try {
                Parser<Mission> parser = parserFactory.getParser(source);
                Mission mission = parser.parse(source);
                System.out.println(printer.formalize(mission));
            } catch (Exception exception) {
                System.out.println("Ошибка: " + exception.getMessage());
            }
        }
    }
}
