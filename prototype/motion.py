import paho.mqtt.client as paho
import time
import datetime
import random
import json
from gpiozero import MotionSensor

def read_from_sensor():
	pir.wait_for_motion()

def on_publish(client, userdata, mid):
    print("mid: " + userdata)

pir = MotionSensor(4)
client = paho.Client()
client.on_publish = on_publish
client.connect("localhost", 1883)
client.loop_start()

while True:
    motion_detected = read_from_sensor()
    (rc, mid) = client.publish("motion_sensor/events", json.dumps({'ts': datetime.datetime.utcnow().isoformat(), 'm': motion_detected}), qos=1)
	while pir.motion_detected:
		time.sleep(10)
        (rc, mid) = client.publish("motion_sensor/events", json.dumps({'ts': datetime.datetime.utcnow().isoformat(), 'm': motion_detected}), qos=1)
    (rc, mid) = client.publish("motion_sensor/events", json.dumps({'ts': datetime.datetime.utcnow().isoformat(), 'm': motion_detected}), qos=1)

#logfile = '/home/pi/nb-' + str(datetime.now().strftime("%Y%m%d-%H%M")) +".csv"
#logging.basicConfig(filename=logfile, level=logging.DEBUG,
#	format='%(asctime)s %(message)s',
#datefmt='%Y-%m-%d, %H:%M:%S,')


