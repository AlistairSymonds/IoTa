#pragma once
#include "LedAnimBase.h"
class Glitter : public LedAnimBase {
public:
	Glitter(CHSV * leds, int numleds, uint8_t * state);
	void advanceAnim();
	~Glitter();
private:
	CHSV * animBufPtr;
	uint8_t * statePtr;
	int numleds = 0;
};

