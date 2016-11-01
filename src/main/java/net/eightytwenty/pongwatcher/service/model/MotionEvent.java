package net.eightytwenty.pongwatcher.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class MotionEvent {
    public LocalDateTime eventTime;
    public boolean motionDetected;

    public MotionEvent(LocalDateTime eventTime, boolean motionDetected) {
        this.eventTime = eventTime;
        this.motionDetected = motionDetected;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getEventTime() {
        return eventTime;
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
                Objects.equals(eventTime, that.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventTime, motionDetected);
    }

    @Override
    public String toString() {
        return "MotionEvent{" +
                "eventTime=" + eventTime +
                ", motionDetected=" + motionDetected +
                '}';
    }
}
