#ifdef ESP8266


#include "iota_defines.h"
#include "LedPropEnum.h"
#include "LedSerialMaster.h"
#include "stdlib.h"



#include <Stream.h>

LedSerialMaster::LedSerialMaster(Stream * stream, int maxTokens)
{
	serial = stream;
	fh = new fixedMap<void *>(maxTokens);
}

short LedSerialMaster::getFuncId()
{
	return LEDS_ID;
}

void LedSerialMaster::processCommand(uint8_t command[],  void * clientToken)
{
	
	
	
	serial->write(command, command[0]);
	fh->add(clientToken);
	
}

void LedSerialMaster::tick()
{
	if (serial->available()) {
		serial->readBytes(this->state, NUM_LED_PROPS);
	}
}

int LedSerialMaster::getStateBufLen()
{
	return NUM_LED_PROPS;
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
