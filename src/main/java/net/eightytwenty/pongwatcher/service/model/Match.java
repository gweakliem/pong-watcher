package net.eightytwenty.pongwatcher.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.eightytwenty.pongwatcher.data.LocalDateTimeAttributeConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Match {
    @NotNull
    private String id;
    @NotNull
    private String name;
    private String phone;
    private String location;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updated;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime completed;

    public Match() {
    }

    @JsonCreator
    public Match(@JsonProperty("id") String id,
                 @JsonProperty("name") String name,
                 @JsonProperty("phone") String phone,
                 @JsonProperty("location") String location,
                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                 @JsonProperty("completed") LocalDateTime completed,
                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                 @JsonProperty("created") LocalDateTime created,
                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                 @JsonProperty("updated") LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.created = created;
        this.completed = completed;
        this.updated = updated;
    }

    public String getId() {
        return id;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getCreated() {
        return created;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getCompleted() {
        return completed;
    }

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getUpdated() {
        return updated;
    }
}
