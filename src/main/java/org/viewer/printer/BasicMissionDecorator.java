package org.viewer.printer;

import org.model.Mission;
import org.model.Sorcerer;

public class BasicMissionDecorator extends AbstractMissionDecorator {
    public BasicMissionDecorator(MissionPrinter wrapper) {
        super(wrapper);
    }

    @Override
    public String formalize(Mission mission) {
        StringBuilder builder = new StringBuilder(wrapper.formalize(mission));
        builder.append("\nПроклятие: ")
                .append(safe(mission.getCurse().getName()))
                .append(" [уровень угрозы: ")
                .append(safe(mission.getCurse().getThreatLevel()))
                .append("]");
        builder.append("\nУчастники операции:");
        if (mission.getSorcerers().isEmpty()) {
            builder.append("\n  - не указаны");
        } else {
            for (Sorcerer sorcerer : mission.getSorcerers()) {
                builder.append("\n  - ")
                        .append(safe(sorcerer.getName()))
                        .append(", ранг: ")
                        .append(safe(sorcerer.getRank()));
            }
        }
        return builder.toString();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "не указано" : value;
    }
}
