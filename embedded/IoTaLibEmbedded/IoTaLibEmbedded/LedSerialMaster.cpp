#ifndef _WIN32


#include "iota_defines.h"
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
	return LED_ID;
}

void LedSerialMaster::processCommand(uint8_t command[],  void * clientToken)
{
	enum ledProp prop;
	serial->write(command, command[0]+1);
	
}

void LedSerialMaster::tick()
{
	if (serial->available()) {
		serial->readBytes(this->state, NUM_LED_PROPS);
	}
}

int LedSerialMaster::getStateBufLen()
{
	return stateCount;
}

uint8_t * LedSerialMaster::getStateBuffer(void * clientToken)
{
	fh->remove(clientToken);
	return state;
}

int LedSerialMaster::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}

LedSerialMaster::~LedSerialMaster()
{
	delete fh;
}
#endif // !1
