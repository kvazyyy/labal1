package org.viewer.printer;

public abstract class AbstractMissionDecorator implements MissionPrinter {
    protected final MissionPrinter wrapper;

    protected AbstractMissionDecorator(MissionPrinter wrapper) {
        this.wrapper = wrapper;
    }
}
