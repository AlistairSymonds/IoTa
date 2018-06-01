
#pragma once
#ifndef _esp32scope_h
#define _esp32scope_h
#define WORDS_PER_SECTION 120
#define SAMPLE_MEMORY_LENGTH 4096
#define SAMPLE_WORD_LENGTH 2

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "circularBuffer.h"
#include "Pga112SpiController.h"

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

	CircularBuffer<long> *responseLocs;
	//CircularBuffer<std::pair<long, uint8_t>> *sampleReqs;
	uint8_t nextSectionRequested = 0; 
	long clienRequesting = 0; //if minus 0, no section to be returned
	uint16_t state[5];	
	Pga112SpiController * gain;
	hw_timer_t * sampleTimer;
	
};

void sampleFunc();
#endif