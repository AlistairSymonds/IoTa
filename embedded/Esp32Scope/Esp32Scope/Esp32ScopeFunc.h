
#pragma once
#ifndef _esp32scope_h
#define _esp32scope_h

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "fixedSet.h"


class Esp32ScopeFunc : public IoTaFuncBase {
public:
	Esp32ScopeFunc();

	short getFuncId();
	void processCommand(DataCapsule *capsule);
	void tick();


	int getReponsesRemaining();
	long getNextMsgDest();

	int getStateBufLen();
	int getStateBuffer(uint8_t *buf);

	~Esp32ScopeFunc();




private:
	fixedSet<void *> *fh;
	uint8_t state[1];
};
#endif