package net.eightytwenty.pongwatcher.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jdk.nashorn.internal.parser.JSONParser;
import net.eightytwenty.pongwatcher.data.MotionEventRepository;
import net.eightytwenty.pongwatcher.data.model.MotionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class MqttListenerService {
    private MotionEventRepository motionEventRepository;

    @Autowired
    public MqttListenerService(MessageHandler mqttMessageHandler, MotionEventRepository motionEventRepository) {
        this.motionEventRepository = motionEventRepository;
    }

    private MessageHandler messageHandler = message -> {
        System.out.println("message = " + message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode motionMsg = mapper.readTree(message.getPayload().toString());
            System.out.println("motionMsg = " + motionMsg);
            motionEventRepository.save(
                    new MotionEvent()
                            .setTimestamp(
                                    LocalDateTime.parse(motionMsg.get("ts").asText(),
                                            DateTimeFormatter.ISO_DATE_TIME).toEpochSecond(ZoneOffset.UTC))
                            .setMotionDetected(motionMsg.get("m").asBoolean()));
        } catch (IOException e) {
            System.out.println("Exception processing message = " + message.toString() + " :" + e);
        }
    };

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return messageHandler;
    }
}
