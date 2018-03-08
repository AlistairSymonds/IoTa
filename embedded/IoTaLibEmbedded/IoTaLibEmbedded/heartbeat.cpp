#include "heartbeat.h"


Heartbeat::Heartbeat()
{
	fh = new fixedMap<void*>(10);
	state[0] = 60;
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

uint8_t * Heartbeat::getStateBuffer(void * clientToken)
{
	fh->remove(clientToken);
	return state;
}



int Heartbeat::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}

Heartbeat::~Heartbeat()
{
	delete fh;
}






