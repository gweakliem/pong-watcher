package net.eightytwenty.pongwatcher.service.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class MatchRequestEntity {
    @NotNull
    protected String name;
    protected String phone;
    private String location;

    public MatchRequestEntity() {
    }

    public MatchRequestEntity(String name, String phone, String location) {
        this.name = name;
        this.phone = phone;
        this.location = location;
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

    public MatchRequestEntity setName(String name) {
        this.name = name;
        return this;
    }

    public MatchRequestEntity setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public MatchRequestEntity setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchRequestEntity that = (MatchRequestEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, location);
    }

    @Override
    public String toString() {
        return "MatchRequestEntity{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + "\'" +
                '}';
    }
}
