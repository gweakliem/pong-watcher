package net.eightytwenty.pongwatcher.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfiguration {
    @Value("${pong.mqtt.topic}")
    private String topic;
    @Value("${pong.mqtt.broker}")
    private String broker;
    @Value("${pong.mqtt.clientName}")
    private String clientName;
    @Value("${pong.mqtt.qos}")
    private int qos;

/*
    @Bean
    public MqttListenerService mqttClient() {
        return new MqttListenerService(topic,
                qos, broker,
                clientName);
    }
*/

    @Bean
    public DefaultMqttPahoClientFactory clientFactory() {
        return new DefaultMqttPahoClientFactory();
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        System.out.println("Instantiating inbound clientName = " + clientName);
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(broker, clientName,
                        clientFactory(), // Do I need this? Sample seems to autowire this one.
                        topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

   /*
   @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return message -> System.out.println(message.getPayload());
    }
    */

}
