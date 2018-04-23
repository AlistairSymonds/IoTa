#ifdef ESP8266


#include "iota_defines.h"
#include "LedPropEnum.h"
#include "LedSerialMaster.h"
#include "stdlib.h"



#include <Stream.h>

LedSerialMaster::LedSerialMaster(Stream * stream, int maxTokens)
{
	serial = stream;
	tokenMap = new fixedMap<void *>(maxTokens);
}

short LedSerialMaster::getFuncId()
{
	return FID_LEDS;
}

void LedSerialMaster::processCommand(DataCapsule capsule)
{
	uint8_t *data = new uint8_t[this->getStateBufLen()];
	capsule.copyDataOut(data);
	serial->write(data, data[0]);
	delete data;
	
}

void LedSerialMaster::tick()
{
	if (serial->available()) {
		serial->readBytes(this->state, NUM_LED_PROPS);
	}
}

int LedSerialMaster::isStateBufferUpdated(long clientId)
{
	return 0;
}

int LedSerialMaster::isStateBufferUpdated()
{
	return 0;
}

int LedSerialMaster::getStateBufLen()
{
	return NUM_LED_PROPS;
}

int LedSerialMaster::getStateBuffer(DataCapsule * capsule)
{
	return 0;
}



LedSerialMaster::~LedSerialMaster()
{
	delete tokenMap;
}
#endif // !1
