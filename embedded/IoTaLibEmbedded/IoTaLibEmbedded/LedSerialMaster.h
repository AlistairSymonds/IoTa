
#ifdef memes




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
	int copyStateBuf(uint8_t *buf, void * clientToken);
	int needsStateBufferUpdate(void* clientToken);

	~LedSerialMaster();

private:
	static const int stateCount = 8;
	uint8_t state[stateCount];
	Stream* serial;
	fixedHeap<void*> *fh;


};

#endif // !_LEDSERIALMASTER_H
#endif
