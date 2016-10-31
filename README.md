= Pong-watcher

== Setup
* On the Raspberry Pi
** Install mosquitto (https://mosquitto.org/)
`sudo apt-get install mosquitto`
or
`brew install mosquitto`
** Install paho MQTT library
`sudo pip install paho-mqtt`
** Make sure gpizero is installed
`pip install gpizero`
** Install h2 - I used `brew install h2`
*** Start h2 - `h2` unless it's installed as a service

== TODO
1. Create queue for scheduling next players
2. figure out how to hook up pi in abscence of Internet access (either Bluetooth or as a hub)

== References

Raspberry Pi 3 Bluetooth Networking https://www.youtube.com/watch?v=4Ac0wc-f9HI
Pi BT config https://kofler.info/bluetooth-konfiguration-im-terminal-mit-bluetoothctl/
Pi scripts for BT NAP service https://github.com/Douglas6/pinaple
Internet connection sharing over bluetooth http://hints.macworld.com/article.php?story=20031117142051675
PPPD https://developer.apple.com/legacy/library/documentation/Darwin/Reference/ManPages/man8/pppd.8.html

PiPoint https://www.pi-point.co.uk/documentation/
PiUi https://github.com/dps/piui