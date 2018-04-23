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
#include "DataCapsule.h"

#define MAX_CLIENTS 3


WiFiServer server(2812);
WiFiClient clients[MAX_CLIENTS];

IoTaDeviceHub hub;
Heartbeat hb;
LedSerialMaster led(&Serial, MAX_CLIENTS);


int nextFreeSpot = 0;

void processClient(WiFiClient);

static const size_t bufferSize = 1024;
static uint8_t sbuf[bufferSize];

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
		
		//uint8_t msgBuffer[256];
		sbuf[0] = c->read();
		

		//Fixed read in length here, huge speed increase due to fixing timeout!
		c->readBytes(&sbuf[1], sbuf[0]-1);

		long source = (sbuf[1] << 56 | sbuf[2] << 48 | sbuf[3] << 40 | sbuf[4] << 32 | sbuf[5] << 24 | sbuf[6] << 16 | sbuf[7] << 8 | sbuf[8]);
		long dest = (sbuf[9] << 56 | sbuf[10] << 48 | sbuf[11] << 40 | sbuf[12] << 32 | sbuf[13] << 24 | sbuf[14] << 16 | sbuf[15] << 8 | sbuf[16]);
		short func = (sbuf[17] << 8 | sbuf[18]);
		short size = (sbuf[19] << 8 | sbuf[20]);
		uint8_t * data = new uint8_t[size];

		DataCapsule capsule(source, dest, func, size, data);
		hub.processMessage(&capsule);

		delete data;
	}


}


void loop() {
	
	// Check if a new client has connected
	WiFiClient newClient = server.available();
	long time = micros();
	if (newClient) {
		clients[nextFreeSpot] = newClient;
		nextFreeSpot++;
		if (nextFreeSpot == MAX_CLIENTS) {
			nextFreeSpot = 0;
		}
	}

	//storing connected clients
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
	
	//process internal updates from hub
	hub.tick();


	//get and broadcast state updates
	//while broadcasts waiting
	//tx to each attached client
	
	while (hub.numCapsulesRemaining() > 0)
	{
		DataCapsule *cap;
		hub.getNextOutputCapsule(&cap);

		if (cap->getDestination() == 0) {//broadcast to all connect clients
			
		}
		else
		{

		}
	}

	
	

}


