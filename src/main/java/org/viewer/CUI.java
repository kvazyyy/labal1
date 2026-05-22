package org.viewer;

import org.logger.MissionsLogger;
import org.model.Mission;
import org.parsers.MissionParserFactory;
import org.parsers.Parser;
import org.source.FileDataSource;
import org.source.MissionDataSource;
import org.viewer.printer.BasicMissionDecorator;
import org.viewer.printer.BasicMissionPrinter;
import org.viewer.printer.DetailedMissionDecorator;
import org.viewer.printer.ExtraFieldsMissionDecorator;
import org.viewer.printer.MissionPrinter;

import java.io.File;
import java.util.Scanner;

public class CUI {
    private final MissionParserFactory parserFactory;
    private final MissionsLogger logger = new MissionsLogger();
    private MissionPrinter printer = new ExtraFieldsMissionDecorator(
            new DetailedMissionDecorator(new BasicMissionDecorator(new BasicMissionPrinter()))
    );

    public CUI(MissionParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public void showInterface() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nЛОКАЛЬНЫЙ АНАЛИЗАТОР МИССИЙ МАГОВ");
                System.out.println("1. Открыть файл миссии");
                System.out.println("2. Выбрать формат вывода");
                System.out.println("3. Посмотреть логи обработанных миссий");
                System.out.println("4. Выйти");
                System.out.print("Выберите пункт: ");
                String action = scanner.nextLine().trim();

                switch (action) {
                    case "1" -> openMission(scanner);
                    case "2" -> choosePrinter(scanner);
                    case "3" -> showLogs();
                    case "4" -> {
                        System.out.println("Работа завершена.");
                        return;
                    }
                    default -> System.out.println("Неверный пункт меню.");
                }
            }
        }
    }

    private void openMission(Scanner scanner) {
        System.out.print("Введите путь к файлу миссии: ");
        String path = scanner.nextLine().trim();
        MissionDataSource source = new FileDataSource(new File(path));
        if (!source.isValid()) {
            System.out.println("Файл не найден или недоступен.");
            return;
        }

        try {
            Parser<Mission> parser = parserFactory.getParser(source);
            Mission mission = parser.parse(source);
            System.out.println("\nИсточник успешно обработан.");
            printMission(mission);
            logger.writeLog(mission);
        } catch (Exception exception) {
            System.err.println("Ошибка обработки файла: " + exception.getMessage());
        }
    }

    private void choosePrinter(Scanner scanner) {
        System.out.println("\n1. Кратко: ID, локация, итог");
        System.out.println("2. Базово: кратко + проклятие + участники");
        System.out.println("3. Подробно: базово + дата + ущерб + техники + комментарий");
        System.out.println("4. Полностью: подробно + дополнительные поля");
        System.out.print("Выберите формат: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> printer = new BasicMissionPrinter();
            case "2" -> printer = new BasicMissionDecorator(new BasicMissionPrinter());
            case "3" -> printer = new DetailedMissionDecorator(new BasicMissionDecorator(new BasicMissionPrinter()));
            case "4" -> printer = new ExtraFieldsMissionDecorator(new DetailedMissionDecorator(new BasicMissionDecorator(new BasicMissionPrinter())));
            default -> {
                System.out.println("Формат не изменён.");
                return;
            }
        }
        System.out.println("Формат вывода изменён.");
    }

    private void showLogs() {
        var logs = logger.readLogs();
        if (logs.isEmpty()) {
            System.out.println("Логи пока пустые.");
            return;
        }
        logs.forEach(System.out::println);
    }

    private void printMission(Mission mission) {
        System.out.println("--------------------------------------------------");
        System.out.println(printer.formalize(mission));
        System.out.println("--------------------------------------------------");
    }
}
