import paho.mqtt.client as paho
import time
import datetime
import random
import json
#from gpiozero import MotionSensor

def read_from_sensor():
    return random.getrandbits(1)

def on_publish(client, userdata, mid):
    print("mid: " + str(mid))

client = paho.Client()
client.on_publish = on_publish
client.connect("localhost", 1883)
client.loop_start()

while True:
    motion_detected = read_from_sensor()
    (rc, mid) = client.publish("motion_sensor/events", json.dumps({'ts': datetime.datetime.utcnow().isoformat(), 'm': motion_detected}), qos=1)
    time.sleep(3)
