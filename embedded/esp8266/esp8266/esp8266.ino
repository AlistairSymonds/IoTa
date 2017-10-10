/*
 Name:		esp8266.ino
 Created:	10/4/2017 10:50:40 PM
 Author:	alist
*/

#include <ESP8266WiFi.h>          //https://github.com/esp8266/Arduino

//needed for library
#include <WiFiClient.h>
#include <DNSServer.h>
#include <ESP8266WebServer.h>
#include <WiFiManager.h>         //https://github.com/tzapu/WiFiManager

#define HUB_SIZE 4
#include <IoTaDeviceHub.h>
#include "heartbeat.h"
#include "LedSerialMaster.h"

#define MAX_CLIENTS 3


WiFiServer server(2812);
WiFiClient clients[MAX_CLIENTS];

IoTaDeviceHub hub;
Heartbeat hb;
LedSerialMaster led(&Serial, MAX_CLIENTS);


int nextFreeSpot = 0;

void processClient(WiFiClient);

void setup() {
	Serial.begin(9600);
	WiFiManager wifiManager;
	wifiManager.setDebugOutput(false);
	wifiManager.autoConnect("AutoConnectAP");
	

	hub.addFunc(&hb);
	hub.addFunc(&led);
	

	server.begin();
}



int readInAndSubmitData(WiFiClient *c) {
	//max length of each message is 256 bytes
	while (c->available() > 0)
	{
		uint8_t msgBuffer[256];
		msgBuffer[0] = c->read();
		
		c->readBytes(&msgBuffer[1], msgBuffer[0]);
		for (int i = 0; i < msgBuffer[0]; i++) {
			Serial.print(msgBuffer[i]);
			Serial.print(" ");
		}
		Serial.println("end msg");
		hub.processMessage(msgBuffer, c);
	}

}


void loop() {

	// Check if a new client has connected
	WiFiClient newClient = server.available();

	if (newClient) {
		clients[nextFreeSpot] = newClient;
		nextFreeSpot++;
		Serial.println("new c");
		if (nextFreeSpot == MAX_CLIENTS) {
			nextFreeSpot = 0;
		}
	}

	for (int c = 0; c < nextFreeSpot; c++) {
		if (clients[c].connected()) {
			if(clients[c].available() > 0) {
				readInAndSubmitData(&clients[c]);
			}
		} else {
			nextFreeSpot--;
			if (nextFreeSpot < 0) {
				nextFreeSpot = 0;
			}
		}
	}
	

	hub.tick();
	for (int c = 0; c < nextFreeSpot; c++) {
		

		uint8_t buffer[255];
		hub.copyAndFormatResponses(buffer, &clients[c]);
		if (buffer[0] > 1) {
			clients[c].write(&buffer[0], buffer[0]);
			
			for (int i = 0; i < buffer[0]; i++) {
				Serial.print(buffer[i]);
				Serial.print(" ");
			}
			Serial.println("end output");
		}	
	}
	

}


