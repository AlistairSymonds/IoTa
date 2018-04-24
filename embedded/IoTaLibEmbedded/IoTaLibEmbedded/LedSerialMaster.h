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


	
	// Inherited via IoTaFuncBase
	virtual int getReponsesRemaining() override;
	virtual long getNextMsgDest() override;

	virtual int getStateBuffer(uint8_t * buf) override;
	int getStateBufLen();

	~LedSerialMaster();

private:
	uint8_t state[NUM_LED_PROPS];
	Stream* serial;
	fixedSet<void*> *tokenMap;
	uint8_t newMsgThisTick = 0;



	

};

#endif // !_LEDSERIALMASTER_H

#endif // !PC_TEST