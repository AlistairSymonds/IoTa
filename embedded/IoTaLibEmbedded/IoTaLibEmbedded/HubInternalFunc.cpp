#include "HubInternalFunc.h"
#include "iota_defines.h"


HubInternalFunc::HubInternalFunc(int * numFuncs, int maxFuncs)
{

	numFuncsPtr = numFuncs;
	funcIds = new uint8_t[maxFuncs];
	fh = new fixedMap<void *>(10);
}

short HubInternalFunc::getFuncId()
{
	return HUB_ID;
}

void HubInternalFunc::processCommand(uint8_t command[], void * clientToken)
{
	if (command[0] == HUB_ID) {
		fh->add(clientToken);
	}
}

void HubInternalFunc::tick()
{
}

int HubInternalFunc::getStateBufLen()
{
	return *numFuncsPtr;
}

uint8_t * HubInternalFunc::getStateBuffer(void * clientToken)
{
	fh->remove(clientToken);
	return funcIds;
}

int HubInternalFunc::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}

void HubInternalFunc::addFuncId(uint8_t id)
{
	funcIds[*numFuncsPtr] = id;
}

HubInternalFunc::~HubInternalFunc()
{
}
