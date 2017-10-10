#include "SolidColour.h"
#include "LedPropEnum.h"
SolidColour::SolidColour(CHSV * leds, int numleds, uint8_t * state)
{
	animBufPtr = leds;
	statePtr = state;
	SolidColour::numleds = numleds;
}

void SolidColour::advanceAnim()
{
	for (int i = 0; i < numleds; i++) {
		Serial1.println(statePtr[hue]);
		
		animBufPtr[i].h = statePtr[hue];
		animBufPtr[i].s = statePtr[sat];
		animBufPtr[i].v = statePtr[val];
	}
}

SolidColour::~SolidColour()
{
}
