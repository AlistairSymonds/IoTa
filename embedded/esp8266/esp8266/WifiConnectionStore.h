#pragma once
class WifiConnectionStore
{
public:
	WifiConnectionStore(int maxClients);
	//add client to store, will perform any handshaking if specified,
	int addClient(WiFiClient *c);
	WiFiClient * getAssociatedWifiConnection(long clientId);
	WiFiClient * removeWifiConnection(long clientId);
	WiFiClient * removeWifiConnection(WiFiClient *c);
	~WifiConnectionStore();

private:

};

