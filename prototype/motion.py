from gpiozero import MotionSensor

import logging
import time
from datetime import datetime

logfile = '/home/pi/nb-' + str(datetime.now().strftime("%Y%m%d-%H%M")) +".csv"
logging.basicConfig(filename=logfile, level=logging.DEBUG,
	format='%(asctime)s %(message)s',
datefmt='%Y-%m-%d, %H:%M:%S,')

pir = MotionSensor(4)

print('Starting')
logging.info('Starting')
while True:
	pir.wait_for_motion()
	logging.info('Motion detected')
	while pir.motion_detected:
		time.sleep(1)
		logging.info('Motion detected')
	logging.info('Motion Ended')

