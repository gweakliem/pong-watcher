package net.eightytwenty.pongwatcher.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.eightytwenty.pongwatcher.data.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Convert(converter =LocalDateTimeAttributeConverter.class)
    public LocalDateTime created;
    @Convert(converter =LocalDateTimeAttributeConverter.class)
    public LocalDateTime completed;
    protected String name;
    protected String phone;
    private String location;
    @Convert(converter =LocalDateTimeAttributeConverter.class)
    private LocalDateTime updated;

    protected MatchEntity() {
    }

    @JsonCreator
    public MatchEntity(
            Long id,
            String name,
             String phone,
             String location,
             LocalDateTime completed,
             LocalDateTime created,
             LocalDateTime updated) {
        this.id = id;
        this.created = created;
        this.completed = completed;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getCompleted() {
        return completed;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public MatchEntity setCompleted(LocalDateTime completed) {
        this.completed = completed;
        return this;
    }

    public MatchEntity setName(String name) {
        this.name = name;
        return this;
    }

    public MatchEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public MatchEntity setLocation(String location) {
        this.location = location;
        return this;
    }

    public MatchEntity setUpdated(LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchEntity that = (MatchEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(created, that.created) &&
                Objects.equals(completed, that.completed) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(location, that.location) &&
                Objects.equals(updated, that.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, completed, name, phone, location, updated);
    }
}
