package net.eightytwenty.pongwatcher.service.model;

import java.time.LocalDateTime;

public class Usage {
    private boolean inUse;
    private LocalDateTime lastActivity;

    public Usage(boolean inUse, LocalDateTime lastActivity) {
        this.inUse = inUse;
        this.lastActivity = lastActivity;
    }

    public boolean isInUse() {
        return inUse;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
}
