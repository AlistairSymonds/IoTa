#pragma once
#include "LedAnimBase.h"
#include "FastLED.h"
class RainbowAnim : public LedAnimBase
{
public:
	RainbowAnim(CHSV * leds, int numleds, uint8_t * state);
	void advanceAnim();
	~RainbowAnim();
private:
	CHSV * animBufPtr;
	uint8_t * statePtr;
	int numleds = 0;

};

