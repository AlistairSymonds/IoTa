#include "RainbowAnim.h"
#include "LedPropEnum.h"
#include <FastLED.h>



RainbowAnim::RainbowAnim(CHSV * leds, int numleds, uint8_t * state)
{
	animBufPtr = leds;
	statePtr = state;
	RainbowAnim::numleds = numleds;
}

void RainbowAnim::advanceAnim()
{	
	fill_rainbow(animBufPtr, numleds, statePtr[hue], 15);
}

RainbowAnim::~RainbowAnim()
{
}
