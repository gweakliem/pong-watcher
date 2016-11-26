package net.eightytwenty.pongwatcher.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Using design suggested in http://stackoverflow.com/questions/3312857/h2-database-clustered-indexes-support
 */
@Entity
@Table(name = "motion_events")
public class MotionEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long timestamp;
    private boolean motionDetected;

    public Long getId() {
        return id;
    }

    public MotionEvent setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public MotionEvent setMotionDetected(boolean motionDetected) {
        this.motionDetected = motionDetected;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getTimestampAsLocal() {
        return LocalDateTime.ofEpochSecond(this.timestamp,0, ZoneOffset.UTC);
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotionEvent that = (MotionEvent) o;
        return motionDetected == that.motionDetected &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, motionDetected);
    }

    @Override
    public String toString() {
        return "MotionEvent{" +
                "timestamp=" + timestamp +
                ", motionDetected=" + motionDetected +
                '}';
    }
}
