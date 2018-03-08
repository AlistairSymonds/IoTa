#pragma once
#include "IoTaFuncBase.h"
#include "fixedMap.h"
class TemperatureLM35 :
	public IoTaFuncBase
{
public:
	TemperatureLM35(int pinNum);
	short getFuncId();
	void processCommand(uint8_t command[], void* clientToken);
	void tick();

	int getStateBufLen();
	uint8_t * getStateBuffer(void * clientToken);
	int needsStateBufferUpdate(void* clientToken);
	~TemperatureLM35();
private:
	int pinNum = 0;
	int temp_mCelsius;
	fixedMap<void *> *fh;
};

