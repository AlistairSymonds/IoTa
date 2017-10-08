#include "heartbeat.h"


Heartbeat::Heartbeat()
{
	fh = new fixedHeap<void*>(10);
}

Heartbeat::Heartbeat(int size)
{
	fh = new fixedHeap<void*>(size);
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

int Heartbeat::copyStateBuf(uint8_t * buf, void * clientToken)
{	
	*buf = (uint8_t)60;
	return 1;
}

int Heartbeat::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}

Heartbeat::~Heartbeat()
{
	delete fh;
}






