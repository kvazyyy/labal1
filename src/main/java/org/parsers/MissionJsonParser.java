package org.parsers;

import org.model.Curse;
import org.model.Mission;
import org.model.MissionBuilder;
import org.model.Sorcerer;
import org.model.Technique;
import org.source.MissionDataSource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MissionJsonParser extends AbstractParser<Mission> {
    @Override
    public boolean canParse(MissionDataSource source) {
        return source.getIdentifier().endsWith(".json");
    }

    @Override
    protected Mission parseFromStream(InputStream inputStream) throws Exception {
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        MissionBuilder builder = new MissionBuilder();

        builder.setMissionId(JsonTextReader.findString(json, "missionId"));
        builder.setDate(JsonTextReader.findString(json, "date"));
        builder.setLocation(JsonTextReader.findString(json, "location"));
        builder.setOutcome(JsonTextReader.findString(json, "outcome"));
        Long damageCost = JsonTextReader.findLong(json, "damageCost");
        if (damageCost != null) {
            builder.setDamageCost(damageCost);
        }
        builder.setComment(firstNotBlank(JsonTextReader.findString(json, "comment"), JsonTextReader.findString(json, "note")));

        String curseText = JsonTextReader.findObject(json, "curse");
        if (curseText != null) {
            builder.setCurse(new Curse(
                    JsonTextReader.findString(curseText, "name"),
                    JsonTextReader.findString(curseText, "threatLevel")
            ));
        }

        for (String sorcererText : JsonTextReader.splitObjects(JsonTextReader.findArray(json, "sorcerers"))) {
            builder.addSorcerer(new Sorcerer(
                    JsonTextReader.findString(sorcererText, "name"),
                    JsonTextReader.findString(sorcererText, "rank")
            ));
        }

        for (String techniqueText : JsonTextReader.splitObjects(JsonTextReader.findArray(json, "techniques"))) {
            Long damage = JsonTextReader.findLong(techniqueText, "damage");
            builder.addTechnique(new Technique(
                    JsonTextReader.findString(techniqueText, "name"),
                    JsonTextReader.findString(techniqueText, "type"),
                    JsonTextReader.findString(techniqueText, "owner"),
                    damage == null ? 0 : damage
            ));
        }

        return builder.build();
    }

    private String firstNotBlank(String first, String second) {
        return first != null && !first.isBlank() ? first : second;
    }
}
