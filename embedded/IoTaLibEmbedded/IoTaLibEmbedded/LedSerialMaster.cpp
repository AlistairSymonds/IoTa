#include "LedPropEnum.h"
#include "LedSerialMaster.h"
#include "stdlib.h"



#include <Stream.h>

LedSerialMaster::LedSerialMaster(Stream * stream, int maxTokens)
{
	serial = stream;
	fh = new fixedHeap<void *>(maxTokens);
}

short LedSerialMaster::getFuncId()
{
	return 2;
}

void LedSerialMaster::processCommand(uint8_t command[],  void * clientToken)
{
	enum ledProp prop;
	serial->write(command, command[0]+1);
	
}

void LedSerialMaster::tick()
{
	if (serial->available()) {
		//TODO
	}
}

int LedSerialMaster::getStateBufLen()
{
	return stateCount;
}

int LedSerialMaster::copyStateBuf(uint8_t * buf, void * clientToken)
{	
	for (int i = 0; i < stateCount; i++) {
		buf[i] = state[i];
	}
	return fh->remove(clientToken);
}

int LedSerialMaster::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}

LedSerialMaster::~LedSerialMaster()
{
	delete fh;
}

