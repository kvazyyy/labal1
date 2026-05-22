package org.viewer.printer;

import org.model.Mission;

public class BasicMissionPrinter implements MissionPrinter {
    @Override
    public String formalize(Mission mission) {
        return "Миссия: " + safe(mission.getMissionId())
                + "\nЛокация: " + safe(mission.getLocation())
                + "\nИтог: " + safe(mission.getOutcome());
    }

    protected String safe(String value) {
        return value == null || value.isBlank() ? "не указано" : value;
    }
}
