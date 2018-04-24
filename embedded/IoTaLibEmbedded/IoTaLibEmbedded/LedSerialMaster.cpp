#ifdef ESP8266 || ESP32
#include <Stream.h>
#include <string.h>
#include "iota_defines.h"
#include "LedPropEnum.h"
#include "LedSerialMaster.h"
#include "stdlib.h"






LedSerialMaster::LedSerialMaster(Stream * stream, int maxTokens)
{
	serial = stream;
	tokenMap = new fixedSet<void *>(maxTokens);
}

short LedSerialMaster::getFuncId()
{
	return FID_LEDS;
}

void LedSerialMaster::processCommand(DataCapsule * capsule)
{
	uint8_t *data = new uint8_t[this->getStateBufLen()];
	capsule->copyDataOut(data);
	serial->write(data, data[0]);
	delete data;
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

int LedSerialMaster::getReponsesRemaining()
{
	return newMsgThisTick;
}
long LedSerialMaster::getNextMsgDest()
{
	newMsgThisTick = 0;
	return 0;
}
int LedSerialMaster::getStateBuffer(uint8_t * buf)
{
	memcpy(buf, state, getStateBufLen());
	return 0;
}

LedSerialMaster::~LedSerialMaster()
{
	delete tokenMap;
}

#endif // !1
