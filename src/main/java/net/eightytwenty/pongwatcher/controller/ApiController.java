package net.eightytwenty.pongwatcher.controller;

import net.eightytwenty.pongwatcher.data.MotionEventRepository;
import net.eightytwenty.pongwatcher.service.model.MotionEvent;
import net.eightytwenty.pongwatcher.service.model.MotionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ApiController {
    MotionEventRepository motionEventRepository;

    @Autowired
    public ApiController(MotionEventRepository repository) {
        this.motionEventRepository = repository;
    }

    @RequestMapping("/history")
    public MotionHistory history() {
        return new MotionHistory(
                motionEventRepository.findAllByOrderByTimestampDesc().stream()
                        .map(me -> new MotionEvent(
                                LocalDateTime.ofEpochSecond(me.getTimestamp(), 0, ZoneOffset.UTC),
                                me.isMotionDetected()))
                        .collect(toList()));
    }

    @RequestMapping("/lastevent")
    public MotionEvent lastEvent() {
        net.eightytwenty.pongwatcher.data.model.MotionEvent event =
                motionEventRepository.findFirstByMotionDetectedOrderByTimestampDesc(true);
        return new MotionEvent(event.getTimestampAsLocal(), event.isMotionDetected());
    }
}
