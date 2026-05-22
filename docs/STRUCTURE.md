# Свиток структуры проклятых данных

Это схема внутренней модели, к которой приводятся JSON, XML и TXT-файлы.

```text
Mission
├── missionId: String
├── date: String
├── location: String
├── outcome: String
├── damageCost: long
├── comment: String
├── curse: Curse
│   ├── name: String
│   └── threatLevel: String
├── sorcerers: List<Sorcerer>
│   └── Sorcerer
│       ├── name: String
│       └── rank: String
├── techniques: List<Technique>
│   └── Technique
│       ├── name: String
│       ├── type: String
│       ├── owner: String
│       └── damage: long
└── extraFields: Map<String, String>
```

## Связи сущностей

- `Mission` содержит одну цель миссии — `Curse`.
- `Mission` содержит список участников — `List<Sorcerer>`.
- `Mission` содержит список применённых техник — `List<Technique>`.
- `Technique.owner` хранит имя мага, которому принадлежит техника.
- `extraFields` нужен для сохранения неизвестных полей, если в будущем в файлах появятся дополнительные данные.

## Почему модель единая

Форматы входных данных разные, но предметная сущность одна — миссия. Поэтому каждый парсер отвечает только за чтение своего формата, а результат всегда один и тот же объект `Mission`.
