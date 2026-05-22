package org.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MissionBuilder {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse = new Curse();
    private final List<Sorcerer> sorcerers = new ArrayList<>();
    private final List<Technique> techniques = new ArrayList<>();
    private String comment = "Без комментариев о миссии";
    private final Map<String, String> extraFields = new LinkedHashMap<>();

    public MissionBuilder setMissionId(String missionId) {
        this.missionId = missionId;
        return this;
    }

    public MissionBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    public MissionBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public MissionBuilder setOutcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    public MissionBuilder setDamageCost(long damageCost) {
        this.damageCost = damageCost;
        return this;
    }

    public MissionBuilder setCurseName(String name) {
        this.curse.setName(name);
        return this;
    }

    public MissionBuilder setCurseThreatLevel(String threatLevel) {
        this.curse.setThreatLevel(threatLevel);
        return this;
    }

    public MissionBuilder setCurse(Curse curse) {
        if (curse != null) {
            this.curse = curse;
        }
        return this;
    }

    public MissionBuilder addSorcerer(Sorcerer sorcerer) {
        if (sorcerer != null) {
            this.sorcerers.add(sorcerer);
        }
        return this;
    }

    public MissionBuilder addTechnique(Technique technique) {
        if (technique != null) {
            this.techniques.add(technique);
        }
        return this;
    }

    public MissionBuilder setComment(String comment) {
        if (comment != null && !comment.isBlank()) {
            this.comment = comment;
        }
        return this;
    }

    public MissionBuilder addExtraField(String key, String value) {
        if (key != null && value != null) {
            this.extraFields.put(key, value);
        }
        return this;
    }

    public Mission build() {
        Mission mission = new Mission();
        mission.setMissionId(missionId);
        mission.setDate(date);
        mission.setLocation(location);
        mission.setOutcome(outcome);
        mission.setDamageCost(damageCost);
        mission.setCurse(curse);
        mission.setSorcerers(new ArrayList<>(sorcerers));
        mission.setTechniques(new ArrayList<>(techniques));
        mission.setComment(comment);
        mission.setExtraFields(new LinkedHashMap<>(extraFields));
        return mission;
    }
}
