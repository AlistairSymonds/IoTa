/*
Name:		esp8266.ino
Created:	10/4/2017 10:50:40 PM
Author:	alist
*/
#include <limits.h>


//to make polyspace happy since these aren't explicitly included outside of the arduino environment


#include <WiFi.h>          

//needed for library
#include <WiFiClient.h>
#include <DNSServer.h>
#include <WebServer.h>
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

long uuid;



//enum for what an authednticated client is waiting to recieve next
enum connStatusEnum
{
	NOT_CONNECTED,
	FIRST_CONNECT,
	MAGIC_BYTE_RX,
	CLIENT_ID_RX,
	CONNECTED
};

enum unAuthEnum
{
	timeIn,
	authProg,
	unAuthClient
};
//Tracking unauthenticated connections,
//@1st, int = time connected in micros
//@2nd, connStatusEnum = what stage in connection the unAuthClient is in
//@3rd, WiFIClient = unAuthClient in question
std::tuple<uint, connStatusEnum, WiFiClient> *unAuthClients;

int nextFreeSpot = 0;


iota::Map<long, WiFiClient*> *clientMap;

static const size_t bufferSize = 1024;
static uint8_t rxBuf[bufferSize];
static uint8_t txBuf[bufferSize];

std::pair<int, int> tMarkers;




void setup() {
	Serial.begin(115200);

	Serial.println("Starting IoTa ESP8266");
	Serial.println("Wifi Manager init");
	WiFiManager wifiManager;
	wifiManager.setDebugOutput(false);
	wifiManager.autoConnect("AutoConnectAP");
	
	Serial.println("Setting up hub");
	byte mac[6];
	WiFi.macAddress(mac);
	Serial.printf("Mac address: %h %h %h %h %h %h \n", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
	Serial.printf("Chip id: %d\n", ESP.getEfuseMac());
	uuid = (ESP.getEfuseMac()) << 32 | mac[0] << 24 | mac[1] << 16 | mac[2] << 9 | mac[3]);
	Serial.printf("UUID : %l", uuid);

	hub = new IoTaDeviceHub(uuid);
	hub->addFunc(&hb);


	unAuthClients = new std::tuple<uint, connStatusEnum, WiFiClient>[MAX_UNAUTH_CLIENTS];
	for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
		std::get<authProg>(unAuthClients[i]) = NOT_CONNECTED;
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
		rxBuf[0] = c->read();
		rxBuf[1] = c->read();
		short msgLen = (rxBuf[0] << 8 | rxBuf[1]);
		//Fixed read in length here, huge speed increase due to fixing timeout!
		c->readBytes(&rxBuf[2], msgLen);

		long source = (rxBuf[2] << 56 | rxBuf[3] << 48 | rxBuf[4] << 40 | rxBuf[5] << 32 | rxBuf[6] << 24 | rxBuf[7] << 16 | rxBuf[8] << 8 | rxBuf[9]);
		long dest = (rxBuf[10] << 56 | rxBuf[11] << 48 | rxBuf[12] << 40 | rxBuf[13] << 32 | rxBuf[14] << 24 | rxBuf[15] << 16 | rxBuf[16] << 8 | rxBuf[17]);
		short func = (rxBuf[18] << 8 | rxBuf[19]);
		short size = (rxBuf[20] << 8 | rxBuf[21]);
		uint8_t * data = new uint8_t[size];
		memcpy(data, &rxBuf[22], size);
		for (int i = 0; i < msgLen; i++) {
			Serial.print(rxBuf[i], HEX);
			Serial.print(" ");

		}
		Serial.println();
		//data is now stored inside DataCapsule, data can be freed
		DataCapsule capsule(source, dest, func, size, data);
		printCapsuleDetails(&capsule);

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

	// Check if a new unAuthClient has connected
	WiFiClient newClient = server.available();


	/*
	Connection process:
	1. unAuthClient appears at wifi server
	2. read first byte transmitted by unAuthClient connecting
	3. if unAuthClient sent MAGIC_BYTE (65) continue
	4. send back magic byte+1
	5. wait for unAuthClient to send an empty data packet addressed for FID
	6. add clientId to clientMap
	7. enjoy your fancy new connected unAuthClient!
	*/



	//New unAuthClient, looop through unath waiting zone to find first empty
	//spot and begin auth protocol
	if (newClient) {
		for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
			if (std::get<authProg>(unAuthClients[i]) == NOT_CONNECTED)
			{
				Serial.print("New unauth client at ");
				Serial.print(i);
				int now = micros();
				Serial.print(" t = ");
				Serial.println(now);
				std::get<timeIn>(unAuthClients[i]) = now;
				std::get<authProg>(unAuthClients[i]) = FIRST_CONNECT;
				std::get<unAuthClient>(unAuthClients[i]) = newClient;
				i = MAX_UNAUTH_CLIENTS;
			}
		}
	}

	uint tnow = micros();
	uint timeout_us = 2000000;
	for (int i = 0; i < MAX_UNAUTH_CLIENTS; i++) {
		yield();
		if (tnow - std::get<timeIn>(unAuthClients[i]) > timeout_us && std::get<authProg>(unAuthClients[i]) != NOT_CONNECTED) {


			//remove due to lack of connections
			Serial.print("timeout ");
			Serial.println(i);
			std::get<timeIn>(unAuthClients[i]) = 0;
			std::get<authProg>(unAuthClients[i]) = NOT_CONNECTED;
			std::get<unAuthClient>(unAuthClients[i]).stop();
		}
		else if (std::get<authProg>(unAuthClients[i]) != NOT_CONNECTED)
		{
			switch (std::get<authProg>(unAuthClients[i]))
			{
			case(FIRST_CONNECT):

				if (std::get<unAuthClient>(unAuthClients[i]).available() > 0) {

					uint8_t firstContact = std::get<unAuthClient>(unAuthClients[i]).read();


					if (firstContact == MAGIC_BYTE) {
						std::get<authProg>(unAuthClients[i]) = MAGIC_BYTE_RX;
					}
				}
				break;

			case(MAGIC_BYTE_RX):
				std::get<unAuthClient>(unAuthClients[i]).write(MAGIC_BYTE + 1);
				std::get<authProg>(unAuthClients[i]) = CLIENT_ID_RX;

				break;

			case(CLIENT_ID_RX):
				if (std::get<unAuthClient>(unAuthClients[i]).available() > 0) {
					DataCapsule cap = createDataPacket(&std::get<unAuthClient>(unAuthClients[i]));

					printCapsuleDetails(&cap);
					clientMap->put(cap.getSource(), &std::get<unAuthClient>(unAuthClients[i]));
					std::get<authProg>(unAuthClients[i]) = CONNECTED;

					DataCapsule outCap(uuid, cap.getSource(), FID_HUB, 0, NULL);


					size_t pLen = cap.getTcpPacketLength();
					uint8_t bytes[pLen];

					cap.createTcpPacket(bytes);
					for (int i = 0; i < pLen; i++) {
						Serial.print(" ");
						Serial.print(bytes[i], HEX);
					}
					Serial.println("<- empty capsule");
					std::get<unAuthClient>(unAuthClients[i]).write(&bytes[0], pLen);
					Serial.println(cap.getSource());
					clientMap->put(cap.getSource(), &std::get<unAuthClient>(unAuthClients[i]));

				}

				break;

			case(CONNECTED): //client has been authenticated and moved to clientMap, time to clean up its spot
				std::get<timeIn>(unAuthClients[i]) = 0;
				std::get<authProg>(unAuthClients[i]) = NOT_CONNECTED;
				Serial.print(i);
				Serial.println(" has been cleaned");
				break;
			default:
				break;
			}

		}
	}







	//iterate through connected clients, if they have any data in buffer
	//read in a and process it
	for (int i = 0; i < clientMap->getMaxSize(); i++) {
		std::pair<long, WiFiClient*> *entries = clientMap->getEntryReference();

		if (entries[i].first != NULL) {
			if (!entries[i].second->connected()) {
				Serial.print(entries[i].first);
				Serial.println(" has disconnected");
				clientMap->remove(entries[i].first);

			}
			else if (entries[i].second->available() > 0) {

				DataCapsule cap = createDataPacket(entries[i].second);
				printCapsuleDetails(&cap);
				hub->processMessage(&cap);
			}
		}

	}

	yield();
	//process internal updates from hub
	hub->tick();
	yield();


	//get and broadcast state updates
	//while broadcasts waiting
	//tx to each attached unAuthClient

	while (hub->numCapsulesRemaining() > 0)
	{
		DataCapsule *cap;
		hub->getNextOutputCapsule(&cap);

		if (cap->getDestination() == 0) {//broadcast to all connected clients

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

void printCapsuleDetails(DataCapsule *c) {
	Serial.print("Source: ");
	Serial.println(c->getSource());
	Serial.print("Destination: ");
	Serial.println(c->getDestination());
	Serial.print("Func id: ");
	Serial.println(c->getFuncId());
	Serial.print("Attached data size: ");
	Serial.println(c->getDataSize());
}


