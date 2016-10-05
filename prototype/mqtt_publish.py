import paho.mqtt.client as paho
import time
import datetime
#import random
from gpiozero import MotionSensor

def read_from_sensor():
    return random.getrandbits(1)

def on_publish(client, userdata, mid):
    print("mid: " + str(mid) + " " + str(userdata))

pir = MotionSensor(4)
client = paho.Client()
client.on_publish = on_publish
client.connect("localhost", 1883)
client.loop_start()

while True:
    pir.wait_for_motion()   
    #motion_detected = read_from_sensor()
    (rc, mid) = client.publish("motion_sensor/events",
                               str((datetime.datetime.utcnow().isoformat(),
                                    1)), qos=1)
    while pir.motion_detected:
	time.sleep(1)
	client.publish("motion_sensor/events",
                       str((datetime.datetime.utcnow().isoformat(),
                            1)), qos=1)
    client.publish("motion_sensor/events",
                   str((datetime.datetime.utcnow().isoformat(),
                        0)), qos=1)

print("Shutting down")
    
