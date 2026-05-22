package org.logger;

import org.model.Mission;
import org.viewer.printer.BasicMissionDecorator;
import org.viewer.printer.BasicMissionPrinter;
import org.viewer.printer.DetailedMissionDecorator;
import org.viewer.printer.ExtraFieldsMissionDecorator;
import org.viewer.printer.MissionPrinter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

public class MissionsLogger {
    private static final Path LOG_PATH = Path.of("logs.txt");
    private final MissionPrinter printer = new ExtraFieldsMissionDecorator(
            new DetailedMissionDecorator(new BasicMissionDecorator(new BasicMissionPrinter()))
    );

    public void writeLog(Mission mission) {
        String record = "==== " + LocalDateTime.now() + " ====\n"
                + printer.formalize(mission)
                + "\n\n";
        try {
            Files.writeString(
                    LOG_PATH,
                    record,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            System.err.println("Не удалось записать лог: " + exception.getMessage());
        }
    }

    public List<String> readLogs() {
        try {
            return Files.exists(LOG_PATH) ? Files.readAllLines(LOG_PATH, StandardCharsets.UTF_8) : List.of();
        } catch (IOException exception) {
            System.err.println("Не удалось прочитать лог: " + exception.getMessage());
            return List.of();
        }
    }
}
