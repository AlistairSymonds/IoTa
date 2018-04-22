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
	void processCommand(DataCapsule *capsule);
	void tick();

	
	int isStateBufferUpdated(long clientId);
	int isStateBufferUpdated();

	int getStateBufLen();
	int getStateBuffer(DataCapsule *capsule);

	~Heartbeat();

	

	
private:
	fixedMap<long> *fh;
	uint8_t state[1];
};
#endif
