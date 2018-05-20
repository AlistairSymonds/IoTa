#include <string.h>
#include "HubInternalFunc.h"
#include "iota_defines.h"


HubInternalFunc::HubInternalFunc(IoTaFuncBase ** funcs, int * numFuncs, int maxFuncs)
{

	numFuncsPtr = numFuncs;
	funcsPtr = funcs;
	responseMap = new CircularBuffer<long>(10);
}

short HubInternalFunc::getFuncId()
{
	return FID_HUB;
}

void HubInternalFunc::processCommand(DataCapsule *capsule)
{

	uint8_t *data = new uint8_t[this->getStateBufLen()];
	capsule->copyDataOut(data);

	if (capsule->getDataSize() == 2 && data[0] == FID_HUB) {
		responseMap->add(capsule->getSource());
	}
	delete [] data;
}

void HubInternalFunc::tick()
{
}

int HubInternalFunc::getReponsesRemaining()
{
	return responseMap->available();
}

long HubInternalFunc::getNextMsgDest()
{
	return responseMap->read();
}

int HubInternalFunc::getStateBufLen()
{
	return (*numFuncsPtr) * 2;
}

int HubInternalFunc::getStateBuffer(uint8_t * buf)
{
	short * ids;
	ids = new short[*numFuncsPtr];
	for (int i = 0; i < *numFuncsPtr; i++) {
		ids[i] = funcsPtr[i]->getFuncId();
	}

	memcpy(buf, ids, (*numFuncsPtr)*2);
	return 0;
}


HubInternalFunc::~HubInternalFunc()
{
	delete responseMap;
}
