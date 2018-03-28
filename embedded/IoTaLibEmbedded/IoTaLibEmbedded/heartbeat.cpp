#include <string.h>
#include "heartbeat.h"


Heartbeat::Heartbeat()
{
	fh = new fixedMap<void*>(10);
	state[0] = (uint8_t)60;
}

Heartbeat::Heartbeat(int size)
{
	fh = new fixedMap<void*>(size);
}

short Heartbeat::getFuncId()
{
	return 1;
}

void Heartbeat::processCommand(uint8_t command[],  void * clientToken)
{
	if (command[0] == 60) {
		fh->add(clientToken);
	}
}

void Heartbeat::tick()
{
	//lol
}

int Heartbeat::getStateBufLen()
{
	return 1;
}

int Heartbeat::isStateBufferUpdated(void * clientToken)
{
	if (fh->contains(clientToken)) {
		fh->remove(clientToken);
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

int Heartbeat::getStateBuffer(uint8_t * buffer)
{	
	memcpy(buffer, state, getStateBufLen());
	return 1;
}

Heartbeat::~Heartbeat()
{
	delete fh;
}






