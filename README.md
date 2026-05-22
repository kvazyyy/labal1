# Лабораторная работа №1. Локальный анализатор миссий магов

## Что реализовано

Приложение загружает файл миссии из файловой системы, определяет формат по расширению (`.json`, `.xml`, `.txt`), преобразует данные в единую внутреннюю модель `Mission` и выводит структурированную сводку.

Поддержанные форматы из ТЗ и проверочного архива:

- JSON;
- XML;
- простой текстовый формат TXT.

Приложение сделано как консольное Java-приложение, так как по ТЗ допускается консольный вывод при условии структурированного отображения данных.

## Структура проекта

```text
src/main/java/org
├── Main.java
├── logger
│   └── MissionsLogger.java
├── model
│   ├── Curse.java
│   ├── Mission.java
│   ├── MissionBuilder.java
│   ├── Sorcerer.java
│   └── Technique.java
├── parsers
│   ├── AbstractParser.java
│   ├── JsonTextReader.java
│   ├── MissionJsonParser.java
│   ├── MissionParserFactory.java
│   ├── MissionTxtParser.java
│   ├── MissionXmlParser.java
│   └── Parser.java
├── source
│   ├── FileDataSource.java
│   └── MissionDataSource.java
└── viewer
    ├── CUI.java
    └── printer
        ├── AbstractMissionDecorator.java
        ├── BasicMissionDecorator.java
        ├── BasicMissionPrinter.java
        ├── DetailedMissionDecorator.java
        ├── ExtraFieldsMissionDecorator.java
        └── MissionPrinter.java
```

## Как запустить в IntelliJ IDEA

1. Открыть папку проекта `lab1_mission_analyzer`.
2. Убедиться, что выбран JDK 17 или новее.
3. Запустить класс `org.Main`.
4. В меню выбрать пункт `1. Открыть файл миссии` и указать путь к файлу миссии.

## Как запустить из консоли без Maven

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out org.Main
```

Также можно передать пути к файлам аргументами, тогда программа сразу обработает их без меню:

```bash
java -cp out org.Main "test-data/Данные о миссиях. Вариант 1/Mission A.json"
```

## Как собрать через Maven

```bash
mvn clean package
java -jar target/lab1-mission-analyzer-1.0-SNAPSHOT.jar
```

## Проверочные данные

В папку `test-data` добавлены файлы из архива `Данные о миссиях.zip`:

- `Mission A.json`, `Mission A.xml`, `Mission A.txt`;
- `Mission B.json`, `Mission B.xml`, `Mission B.txt`.

Результаты проверки находятся в файле `TEST_RESULTS.txt`.
