#pragma once
#include "LedAnimBase.h"
#include "FastLED.h"
class SolidColour : public LedAnimBase
{
public:
	SolidColour(CHSV * animBuf, int numleds, uint8_t * state);
	void advanceAnim();
	~SolidColour();
private:
	CHSV * animBufPtr;
	uint8_t * statePtr;
	int numleds = 0;
};

