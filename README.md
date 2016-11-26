# Pong-watcher

An over-engineered solution to the most important question of the day: is anybody playing ping pong? Can I use the table,
or do I have to walk all the way downstairs only to find out I have to wait? Must I submit to the indignity of writing
name on the whiteboard? Can I self-service this process?

## Setup
* On the Raspberry Pi
    * Install mosquitto (https://mosquitto.org/)
    `sudo apt-get install mosquitto` or `brew install mosquitto`
    * Install paho MQTT library
    `sudo pip install paho-mqtt`
    * Make sure gpizero is installed
    `pip install gpizero`
* On the host (the one running the spring boot app)
* Install h2 - I used `brew install h2`

## Running the server

Assuming you installed the DB giving your login user create DB access on postgres:
`./gradlew flywayMigrate -i`

`./gradlew clean assemble bootRun`

`./gradlew flywayMigrate -Dflyway.url=jdbc:postgresql://localhost:5432/test_pong_watch`

To attach a debugger
`GRADLE_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005" ./gradlew`

## Raspberry Pi Notes
Set up Wireless Access Point using PiPoint docs
* PiPoint https://www.pi-point.co.uk/documentation/
    * Important change here: I was getting `Failed to create interface mon.wlan0: -95` when trying to run hostapd. To
    fix this, I removed the `driver=nl80211` line from hostapd.conf
    * https://learn.adafruit.com/setting-up-a-raspberry-pi-as-a-wifi-access-point/install-software

To check the status of the access point / DHCP
````
sudo service hostapd status
sudo service isc-dhcp-server status
````
* PiUi https://github.com/dps/piui

## TODO
1. Create queue for scheduling next players
2. figure out how to hook up pi in absence of Internet access (either Bluetooth or as a hub)

## References

* Raspberry Pi 3 Bluetooth Networking https://www.youtube.com/watch?v#4Ac0wc-f9HI
* Pi BT config https://kofler.info/bluetooth-konfiguration-im-terminal-mit-bluetoothctl/
* Pi scripts for BT NAP service https://github.com/Douglas6/pinaple
* Internet connection sharing over bluetooth http://hints.macworld.com/article.php?story#20031117142051675
* PPPD https://developer.apple.com/legacy/library/documentation/Darwin/Reference/ManPages/man8/pppd.8.html


# local notes
pi Ethernet mac
(10.0.0.155) at b8:27:eb:9a:f9:7 on en0 ifscope [ethernet]


https://learn.adafruit.com/setting-up-a-raspberry-pi-as-a-wifi-access-point/install-software
https://cdn-learn.adafruit.com/downloads/pdf/setting-up-a-raspberry-pi-as-a-wifi-access-point.pdf
https://www.raspberrypi.org/forums/viewtopic.php?f=36&t=63048
https://www.raspberrypi.org/forums/viewtopic.php?f=28&t=143749
http://sirlagz.net/2013/02/10/how-to-use-the-raspberry-pi-as-a-wireless-access-pointrouter-part-3b
https://www.raspberrypi.org/forums/viewtopic.php?t=50267&p=389868
http://raspberrypi.stackexchange.com/questions/15393/connect-to-unsecured-wireless-network


http://raspberrypi.stackexchange.com/questions/3867/ssh-to-rpi-without-a-network-connection

http://elinux.org/RPI-Wireless-Hotspot

`createdb -h localhost -p 5432   pong_watch`
`psql -d pong_watch`
```
CREATE SCHEMA pong_watch;
GRANT ALL PRIVILEGES ON DATABASE pong_watch TO ping;
GRANT ALL PRIVILEGES ON SCHEMA pong_watch TO ping;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA pong_watch to ping;
```