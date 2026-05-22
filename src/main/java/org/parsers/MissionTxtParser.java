package org.parsers;

import org.model.Mission;
import org.model.MissionBuilder;
import org.model.Sorcerer;
import org.model.Technique;
import org.source.MissionDataSource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MissionTxtParser extends AbstractParser<Mission> {
    private static final Pattern SORCERER_PATTERN = Pattern.compile("sorcerer\\[(\\d+)]\\.(name|rank)");
    private static final Pattern TECHNIQUE_PATTERN = Pattern.compile("technique\\[(\\d+)]\\.(name|type|owner|damage)");

    @Override
    public boolean canParse(MissionDataSource source) {
        return source.getIdentifier().endsWith(".txt");
    }

    @Override
    protected Mission parseFromStream(InputStream inputStream) {
        MissionBuilder builder = new MissionBuilder();
        Map<Integer, Sorcerer> sorcerers = new TreeMap<>();
        Map<Integer, Technique> techniques = new TreeMap<>();

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(":", 2);
                if (parts.length != 2) {
                    builder.addExtraField("unparsed", line);
                    continue;
                }
                String key = parts[0].trim();
                String value = parts[1].trim();
                processField(builder, sorcerers, techniques, key, value);
            }
        }

        sorcerers.values().forEach(builder::addSorcerer);
        techniques.values().forEach(builder::addTechnique);
        return builder.build();
    }

    private void processField(MissionBuilder builder,
                              Map<Integer, Sorcerer> sorcerers,
                              Map<Integer, Technique> techniques,
                              String key,
                              String value) {
        switch (key) {
            case "missionId" -> builder.setMissionId(value);
            case "date" -> builder.setDate(value);
            case "location" -> builder.setLocation(value);
            case "outcome" -> builder.setOutcome(value);
            case "damageCost" -> builder.setDamageCost(parseLong(value));
            case "curse.name" -> builder.setCurseName(value);
            case "curse.threatLevel" -> builder.setCurseThreatLevel(value);
            case "comment", "note" -> builder.setComment(value);
            default -> processIndexedField(builder, sorcerers, techniques, key, value);
        }
    }

    private void processIndexedField(MissionBuilder builder,
                                     Map<Integer, Sorcerer> sorcerers,
                                     Map<Integer, Technique> techniques,
                                     String key,
                                     String value) {
        Matcher sorcererMatcher = SORCERER_PATTERN.matcher(key);
        if (sorcererMatcher.matches()) {
            int index = Integer.parseInt(sorcererMatcher.group(1));
            String field = sorcererMatcher.group(2);
            Sorcerer sorcerer = sorcerers.computeIfAbsent(index, i -> new Sorcerer());
            if ("name".equals(field)) {
                sorcerer.setName(value);
            } else {
                sorcerer.setRank(value);
            }
            return;
        }

        Matcher techniqueMatcher = TECHNIQUE_PATTERN.matcher(key);
        if (techniqueMatcher.matches()) {
            int index = Integer.parseInt(techniqueMatcher.group(1));
            String field = techniqueMatcher.group(2);
            Technique technique = techniques.computeIfAbsent(index, i -> new Technique());
            switch (field) {
                case "name" -> technique.setName(value);
                case "type" -> technique.setType(value);
                case "owner" -> technique.setOwner(value);
                case "damage" -> technique.setDamage(parseLong(value));
                default -> builder.addExtraField(key, value);
            }
            return;
        }

        builder.addExtraField(key, value);
    }

    private long parseLong(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return Long.parseLong(text.trim());
    }
}
