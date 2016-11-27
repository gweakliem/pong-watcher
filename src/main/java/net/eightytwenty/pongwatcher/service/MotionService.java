package net.eightytwenty.pongwatcher.service;

import net.eightytwenty.pongwatcher.data.MotionEventRepository;
import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import net.eightytwenty.pongwatcher.service.model.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class MotionService {
    private MotionEventRepository motionEventRepository;

    @Autowired
    public MotionService(MotionEventRepository motionEventRepository) {
        this.motionEventRepository = motionEventRepository;
    }

    public Usage getUsage() {
        MotionEvent lastActivity = motionEventRepository.findFirstByOrderByTimestampDesc();
        if (lastActivity == null) {
            return new Usage(false, LocalDateTime.MIN);
        }
        LocalDateTime lastTime = Instant.ofEpochSecond(lastActivity.getTimestamp())
                .atZone(ZoneId.of("GMT"))
                .toLocalDateTime();
        boolean inUse = lastActivity.isMotionDetected();
        return new Usage(inUse, lastTime);
    }

    public List<MotionEvent> getHistory() {
        return motionEventRepository.findAllByOrderByTimestampDesc();
    }
}
