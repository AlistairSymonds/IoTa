#ifdef ESP8266
#include "LedPropEnum.h"
#include"IoTaFuncBase.h"
#ifndef _LEDSERIALMASTER_H
#define _LEDSERIALMASTER_H

#include <Stream.h>
#include "fixedMap.h"

 




class LedSerialMaster: public IoTaFuncBase
{
public:
	LedSerialMaster(Stream* stream, int maxTokens);
	
	short getFuncId();
	void processCommand(DataCapsule *capsule);
	void tick();


	int isStateBufferUpdated(long clientId);
	int isStateBufferUpdated();

	int getStateBufLen();
	int getStateBuffer(DataCapsule *capsule);

	~LedSerialMaster();

private:
	uint8_t state[NUM_LED_PROPS];
	Stream* serial;
	fixedMap<void*> *tokenMap;


};

#endif // !_LEDSERIALMASTER_H

#endif // !PC_TEST