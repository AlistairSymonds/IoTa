#include <string.h>
#include "iota_defines.h"
#include "heartbeat.h"
#include "circularBuffer.h"



Heartbeat::Heartbeat()
{
	responseMap = new CircularBuffer<long>(10);
	state[0] = (uint8_t)60;
}

Heartbeat::Heartbeat(int size)
{
	responseMap = new CircularBuffer<long>(10);
}

short Heartbeat::getFuncId()
{
	return 1;
}

void Heartbeat::processCommand(DataCapsule *capsule)
{
	uint8_t *data = new uint8_t[capsule->getDataSize()];
	
	capsule->copyDataOut(data);
	if (data[0] == FID_HEARTBEAT) {
		responseMap->add(capsule->getSource());
	}
	delete data;
}

void Heartbeat::tick()
{
	//lol
}

int Heartbeat::getReponsesRemaining()
{
	return responseMap->available();
}

long Heartbeat::getNextMsgDest()
{
	return responseMap->read();
}

int Heartbeat::getStateBufLen()
{
	return 1;
}

int Heartbeat::getStateBuffer(uint8_t * buf)
{
	 memcpy(buf, this->state, this->getStateBufLen());
	 return 1;
}



Heartbeat::~Heartbeat()
{
	delete responseMap;
}






