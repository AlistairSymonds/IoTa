#include "WifiConnectionStore.h"
#include "Map.h"





WifiConnectionStore::WifiConnectionStore(int maxClients)
{
	clientMap = new iota::Map<long, WiFiClient*>(10);
}

int WifiConnectionStore::addClient(WiFiClient * c)
{
	if (clientMap->freeSpace() <= 0)
	{
		return -1;
	}
	return 1;
}

WiFiClient * WifiConnectionStore::getAssociatedWifiConnection(long clientId)
{
	return nullptr;
}

WiFiClient * WifiConnectionStore::removeWifiConnection(long clientId)
{
	return nullptr;
}

WiFiClient * WifiConnectionStore::removeWifiConnection(WiFiClient * c)
{
	return nullptr;
}

WifiConnectionStore::~WifiConnectionStore()
{
}
