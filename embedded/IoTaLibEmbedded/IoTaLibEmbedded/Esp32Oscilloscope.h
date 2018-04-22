#pragma once
#ifndef _esp32scope_h
#define _esp32scope_h

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "fixedMap.h"


class Esp32Oscilloscope : public IoTaFuncBase {
public:
	Esp32Oscilloscope();

	short getFuncId();
	void processCommand(DataCapsule capsule);
	void tick();


	int isStateBufferUpdated(void* clientToken);
	int isStateBufferUpdated();

	int getStateBufLen();
	int getStateBuffer(uint8_t * buffer);

	~Esp32Oscilloscope();




private:
	fixedMap<void *> *fh;
	uint8_t state[1];
};
#endif