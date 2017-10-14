#ifdef ESP8266
#include "LedPropEnum.h"
#include"IoTaFuncBase.h"
#ifndef _LEDSERIALMASTER_H
#define _LEDSERIALMASTER_H

#include <Stream.h>
#include "fixedHeap.h"

 




class LedSerialMaster: public IoTaFuncBase
{
public:
	LedSerialMaster(Stream* stream, int maxTokens);
	
	short getFuncId();
	void processCommand(uint8_t command[], void* clientToken);
	void tick();

	int getStateBufLen();
	uint8_t * getStateBuffer(void * clientToken);
	int needsStateBufferUpdate(void* clientToken);

	~LedSerialMaster();

private:
	uint8_t state[NUM_LED_PROPS];
	Stream* serial;
	fixedHeap<void*> *fh;


};

#endif // !_LEDSERIALMASTER_H

#endif // !PC_TEST