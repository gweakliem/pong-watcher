package net.eightytwenty.pongwatcher.service.model;

import java.util.List;
import java.util.Objects;

public class MotionHistory {
    private List<MotionEvent> motionEvents;

    public MotionHistory(List<MotionEvent> motionEvents) {
        this.motionEvents = motionEvents;
    }

    public List<MotionEvent> getMotionEvents() {
        return motionEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotionHistory that = (MotionHistory) o;
        return Objects.equals(motionEvents, that.motionEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motionEvents);
    }

    @Override
    public String toString() {
        return "MotionHistory{" +
                "motionEvents=" + motionEvents +
                '}';
    }
}
