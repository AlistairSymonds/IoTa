#include <string.h>
#include "HubInternalFunc.h"
#include "iota_defines.h"


HubInternalFunc::HubInternalFunc(int * numFuncs, int maxFuncs)
{

	numFuncsPtr = numFuncs;
	funcIds = new uint8_t[maxFuncs];
	map = new fixedMap<void *>(10);
}

short HubInternalFunc::getFuncId()
{
	return HUB_ID;
}

void HubInternalFunc::processCommand(uint8_t command[], void * clientToken)
{
	if (command[0] == HUB_ID) {
		map->add(clientToken);
	}
}

void HubInternalFunc::tick()
{
}

int HubInternalFunc::getStateBufLen()
{
	return *numFuncsPtr;
}

int HubInternalFunc::getStateBuffer(uint8_t * buffer)
{
	memcpy(buffer, funcIds, getStateBufLen());
	return 1;
}

int HubInternalFunc::isStateBufferUpdated()
{
	return 0;
}

int HubInternalFunc::isStateBufferUpdated(void * clientToken)
{
	return map->contains(clientToken);
}

void HubInternalFunc::addFuncId(uint8_t id)
{
	funcIds[*numFuncsPtr] = id;
}

HubInternalFunc::~HubInternalFunc()
{
}
