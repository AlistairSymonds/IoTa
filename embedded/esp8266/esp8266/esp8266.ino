/*
 Name:		esp8266.ino
 Created:	10/4/2017 10:50:40 PM
 Author:	alist
*/
#include <limits.h>


//to make polyspace happy since these aren't explicitly included outside of the arduino environment


#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed for library
#include <WiFiClient.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager

#define HUB_SIZE 4
#include <IoTaDeviceHub.h>

#include "iota_defines.h"
#include "heartbeat.h"
#include "LedSerialMaster.h"
#include "DataCapsule.h"

#define MAX_CLIENTS 10
#define MAX_UNAUTH_CLIENTS 3
#include "Map.h"

WiFiServer server(2812);
IoTaDeviceHub *hub;
Heartbeat hb;
LedSerialMaster led(&Serial, MAX_CLIENTS);


std::pair<int, WiFiClient*> *unAuthClients;

int nextFreeSpot = 0;


iota::Map<long, WiFiClient*> *clientMap;

static const size_t bufferSize = 1024;
static uint8_t sbuf[bufferSize];

std::pair<int, int> tMarkers;


void setup() {
	Serial.begin(9600);
	
	Serial.println("Starting IoTa ESP8266");
	Serial.println("Wifi Manager init");
	WiFiManager wifiManager;
	wifiManager.setDebugOutput(false);
	wifiManager.autoConnect("AutoConnectAP");
	
	Serial.println("Setting up hub");
	byte mac[6];
	WiFi.macAddress(mac);
	Serial.printf("Mac address: %h %h %h %h %h %h \n", mac[0], mac[1],mac[2],mac[3],mac[4],mac[5]);
	Serial.printf("Chip id: %d", ESP.getChipId());
	long uuid = (ESP.getChipId() << 32 | mac[0] << 24 | mac[1] << 16 | mac[2] << 9 | mac[3]);
	Serial.printf("UUID : %l", uuid);
	
	hub = new IoTaDeviceHub(uuid);
	hub->addFunc(&hb);
	hub->addFunc(&led);
	
	unAuthClients = new std::pair<int, WiFiClient*>[MAX_UNAUTH_CLIENTS];
	for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
		unAuthClients[i].first = -1;
	}

	clientMap = new iota::Map<long, WiFiClient*>(10);

	Serial.println("Starting TCP server on 2812");
	server.begin();
	Serial.println("Setup complete!");
	tMarkers.first = INT_MAX;
	tMarkers.second = 0;
}



DataCapsule createDataPacket(WiFiClient *c) {
	//max length of each message is 2^16 bytes
	if (c->available() > 0)
	{
		
		
		//uint8_t msgBuffer[256];
		sbuf[0] = c->read();
		sbuf[1] = c->read();
		short msgLen = (sbuf[0] << 8 |sbuf[1]);
		//Fixed read in length here, huge speed increase due to fixing timeout!
		c->readBytes(&sbuf[2], msgLen);

		long source = (sbuf[2] << 56 | sbuf[3] << 48 | sbuf[4] << 40 | sbuf[5] << 32 | sbuf[6] << 24 | sbuf[7] << 16 | sbuf[8] << 8 | sbuf[9]);
		long dest = (sbuf[10] << 56 | sbuf[11] << 48 | sbuf[12] << 40 | sbuf[13] << 32 | sbuf[14] << 24 | sbuf[15] << 16 | sbuf[16] << 8 | sbuf[17]);
		short func = (sbuf[18] << 8 | sbuf[19]);
		short size = (sbuf[20] << 8 | sbuf[21]);
		uint8_t * data = new uint8_t[size];
		memcpy(data, &sbuf[22], size);

		//data is now stored inside DataCapsule, data can be freed
		DataCapsule capsule(source, dest, func, size, data);
		Serial.print("MEssage recieved from ");
		Serial.println(capsule.getSource());

		delete data;
		return capsule;
	}
	
}

void printDebug() {
	Serial.print("Free heap: ");
	Serial.println(ESP.getFreeHeap());

	Serial.print("Clients connected: ");
	Serial.println(clientMap->size());
}

void loop() {
	
	// Check if a new client has connected
	WiFiClient newClient = server.available();
	

	/*
	Connection process:
		1. client appears at wifi server
		2. read first byte transmitted by client connecting
		3. if client sent MAGIC_BYTE (65) continue
		4. send back magic byte+1
		5. wait for client to send an empty data packet addressed for FID
		6. add clientId to clientMap
		7. enjoy your fancy new connected client!
	*/

	if (newClient) {
		for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
			if (unAuthClients[i].first < 0)
			{
				Serial.print("New unauth client at ");
				Serial.print(i);
				int now = millis();
				Serial.print(" t = ");
				Serial.println(now);
				unAuthClients[i].first = now;
				unAuthClients[i].second = &newClient;
				i = MAX_UNAUTH_CLIENTS;
			}
		}
		
		
	}
	
	int tnow = millis();
	int timeout_ms = 4000;
	for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
		if (tnow - unAuthClients[i].first > timeout_ms && unAuthClients[i].first > 0) {
			//remove due to lack of connections
			Serial.print("Removing client timeout ");
			Serial.println(i);
			unAuthClients[i].first = -1;
		}
		else if(unAuthClients[i].first > 0)
		{	
			//Serial.print("Checking ");
			//Serial.println(i);
			//try to handshake
			if (unAuthClients[i].second->available() > 0 && unAuthClients[i].second->connected()) {
				uint8_t firstContact = unAuthClients[i].second->read();
				Serial.print("Unauth sent: ");
				Serial.println(firstContact);

				if (firstContact == MAGIC_BYTE) {
					Serial.println("Got first part of handshake, responding");
					unAuthClients[i].second->write(MAGIC_BYTE + 1);
				}
			}
			
		}
	}
	

	//process internal updates from hub
	hub->tick();


	//get and broadcast state updates
	//while broadcasts waiting
	//tx to each attached client
	
	while (hub->numCapsulesRemaining() > 0)
	{
		DataCapsule *cap;
		hub->getNextOutputCapsule(&cap);

		if (cap->getDestination() == 0) {//broadcast to all connect clients
			
		}
		else
		{

		}
	}

	
	//timed debug
	int deltaMicros = 5000000; //5 seconds
	tMarkers.second = micros();
	if ((tMarkers.first - tMarkers.second) >= deltaMicros) {
		tMarkers.first = micros();
		printDebug();
		tMarkers.second = micros();
	}

}


