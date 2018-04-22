#include <string.h>
#include "HubInternalFunc.h"
#include "iota_defines.h"


HubInternalFunc::HubInternalFunc(int * numFuncs, int maxFuncs)
{

	numFuncsPtr = numFuncs;
	funcIds = new uint8_t[maxFuncs];
	map = new fixedMap<long>(10);
}

short HubInternalFunc::getFuncId()
{
	return HUB_ID;
}

void HubInternalFunc::processCommand(DataCapsule *capsule)
{

	uint8_t *data = new uint8_t[this->getStateBufLen()];
	capsule->copyDataOut(data);
	if (data[0] == 60) {
		map->add(capsule->getSource());
	}
	delete data;
}

void HubInternalFunc::tick()
{
}

int HubInternalFunc::getStateBufLen()
{
	return *numFuncsPtr;
}

int HubInternalFunc::getStateBuffer(DataCapsule *capsule)
{
	//memcpy(buffer, funcIds, getStateBufLen());
	capsule->updateData(this->getStateBufLen(), funcIds);
	return 1;
}

int HubInternalFunc::isStateBufferUpdated()
{
	return 0;
}

int HubInternalFunc::isStateBufferUpdated(long clientId)
{
	return map->contains(clientId);
}

void HubInternalFunc::addFuncId(uint8_t id)
{
	funcIds[*numFuncsPtr] = id;
}

HubInternalFunc::~HubInternalFunc()
{
}
