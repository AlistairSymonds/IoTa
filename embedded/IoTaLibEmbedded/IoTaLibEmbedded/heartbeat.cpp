#include <string.h>
#include "iota_defines.h"
#include "heartbeat.h"



Heartbeat::Heartbeat()
{
	fh = new fixedMap<long>(10);
	state[0] = (uint8_t)60;
}

Heartbeat::Heartbeat(int size)
{
	fh = new fixedMap<long>(size);
}

short Heartbeat::getFuncId()
{
	return 1;
}

void Heartbeat::processCommand(DataCapsule *capsule)
{
	uint8_t *data = new uint8_t[capsule->getDataSize()];
	
	capsule->copyDataOut(data);
	if (data[0] == HEARTBEAT_ID) {
		fh->add(capsule->getSource());
	}
	delete data;
}

void Heartbeat::tick()
{
	//lol
}

int Heartbeat::getStateBufLen()
{
	return 1;
}

int Heartbeat::getStateBuffer(DataCapsule * capsule)
{
	capsule->updateData(this->getStateBufLen(), state);
	return 0;
}

int Heartbeat::isStateBufferUpdated(long clientId)
{
	if (fh->contains(clientId)) {
		fh->remove(clientId);
		return 1;
	}
	else
	{
		return 0;
	}
	
}

int Heartbeat::isStateBufferUpdated()
{
	return 0;
}



Heartbeat::~Heartbeat()
{
	delete fh;
}






