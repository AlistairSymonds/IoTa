#pragma once
#ifndef _heartbeat_h
#define _heartbeat_h

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "circularBuffer.h"


class Heartbeat: public IoTaFuncBase {
public:
	Heartbeat();
	Heartbeat(int size);
	short getFuncId();
	void processCommand(DataCapsule *capsule);
	void tick();

	
	int getReponsesRemaining();
	long getNextMsgDest();

	int getStateBufLen();
	int getStateBuffer(uint8_t *buf);

	~Heartbeat();

	

	
private:
	CircularBuffer<long> *responseMap;
	uint8_t state[1];
};
#endif
