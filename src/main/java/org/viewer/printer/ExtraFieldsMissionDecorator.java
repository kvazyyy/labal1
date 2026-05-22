package org.viewer.printer;

import org.model.Mission;

import java.util.Map;

public class ExtraFieldsMissionDecorator extends AbstractMissionDecorator {
    public ExtraFieldsMissionDecorator(MissionPrinter wrapper) {
        super(wrapper);
    }

    @Override
    public String formalize(Mission mission) {
        StringBuilder builder = new StringBuilder(wrapper.formalize(mission));
        if (mission.getExtraFields() != null && !mission.getExtraFields().isEmpty()) {
            builder.append("\nДополнительные данные:");
            for (Map.Entry<String, String> entry : mission.getExtraFields().entrySet()) {
                builder.append("\n  - ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue());
            }
        }
        return builder.toString();
    }
}
