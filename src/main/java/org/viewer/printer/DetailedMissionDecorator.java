package org.viewer.printer;

import org.model.Mission;
import org.model.Technique;

public class DetailedMissionDecorator extends AbstractMissionDecorator {
    public DetailedMissionDecorator(MissionPrinter wrapper) {
        super(wrapper);
    }

    @Override
    public String formalize(Mission mission) {
        StringBuilder builder = new StringBuilder(wrapper.formalize(mission));
        builder.append("\nДата: ").append(safe(mission.getDate()));
        builder.append("\nОценка ущерба: ").append(mission.getDamageCost());
        builder.append("\nПрименённые техники:");
        if (mission.getTechniques().isEmpty()) {
            builder.append("\n  - не указаны");
        } else {
            for (Technique technique : mission.getTechniques()) {
                builder.append("\n  - ")
                        .append(safe(technique.getName()))
                        .append("; тип: ")
                        .append(safe(technique.getType()))
                        .append("; владелец: ")
                        .append(safe(technique.getOwner()))
                        .append("; урон: ")
                        .append(technique.getDamage());
            }
        }
        builder.append("\nКомментарий: ").append(safe(mission.getComment()));
        return builder.toString();
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "не указано" : value;
    }
}
