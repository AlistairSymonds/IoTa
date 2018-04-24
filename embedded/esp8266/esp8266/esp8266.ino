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
		sbuf[1] = c->read();
		short msgLen = (sbuf[0] << 8 |sbuf[1]);
		//Fixed read in length here, huge speed increase due to fixing timeout!
		c->readBytes(&sbuf[1], msgLen);

		long source = (sbuf[2] << 56 | sbuf[3] << 48 | sbuf[4] << 40 | sbuf[5] << 32 | sbuf[6] << 24 | sbuf[7] << 16 | sbuf[8] << 8 | sbuf[9]);
		long dest = (sbuf[10] << 56 | sbuf[11] << 48 | sbuf[12] << 40 | sbuf[13] << 32 | sbuf[14] << 24 | sbuf[15] << 16 | sbuf[16] << 8 | sbuf[17]);
		short func = (sbuf[18] << 8 | sbuf[19]);
		short size = (sbuf[20] << 8 | sbuf[21]);
		uint8_t * data = new uint8_t[size];
		memcpy(data, &sbuf[22], size);

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


