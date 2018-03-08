#pragma once
#ifndef _heartbeat_h
#define _heartbeat_h

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "fixedMap.h"


class Heartbeat: public IoTaFuncBase {
public:
	Heartbeat();
	Heartbeat(int size);
	short getFuncId();
	void processCommand(uint8_t command[],  void* clientToken);
	void tick();

	int getStateBufLen();
	uint8_t * getStateBuffer(void * clientToken);
	int needsStateBufferUpdate(void* clientToken);
	~Heartbeat();
private:
	fixedMap<void *> *fh;
	uint8_t state[1];
};
#endif
