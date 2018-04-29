#pragma once
#include <ESP8266WiFi.h>

#include "Map.h"
//insert wifi unAuthClient and forget about it!
class WifiConnectionStore
{
public:
	WifiConnectionStore(int maxClients);
	//add unAuthClient to store, will perform any handshaking if specified,
	int addClient(WiFiClient *c);
	WiFiClient * getAssociatedWifiConnection(long clientId);
	WiFiClient * removeWifiConnection(long clientId);
	WiFiClient * removeWifiConnection(WiFiClient *c);
	~WifiConnectionStore();

private:
	iota::Map<long, WiFiClient*> *clientMap;
	iota::Map<long, long> *heartbeatMap;
};

