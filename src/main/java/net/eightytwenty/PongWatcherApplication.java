package net.eightytwenty;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PongWatcherApplication {

	public static void main(String[] args) {
		//SpringApplication.run(PongWatcherApplication.class, args);

        String topic        = "motion_sensor/events";
        int qos             = 1;
        String broker       = "tcp://0.0.0.0:1883";
        String clientId     = "PongClient";
        MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost cause = " + cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("messageArrived topic = " + topic + " message =" + message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            System.out.println("Connecting to broker: "+broker);
			sampleClient.connect(connOpts);

            sampleClient.subscribe(topic, qos);
            System.out.println("listening");


            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Received interrupt cleaning up");
                    sampleClient.unsubscribe(topic);
                    sampleClient.disconnect();
                    System.out.println("Bye!");
                    return;
                }
            }
		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}
}
