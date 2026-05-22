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