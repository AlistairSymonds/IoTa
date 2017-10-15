#include "Glitter.h"
#include "LedPropEnum.h"



Glitter::Glitter(CHSV * leds, int numleds, uint8_t * state)
{
	animBufPtr = leds;
	statePtr = state;
	Glitter::numleds = numleds;
}

void Glitter::advanceAnim()
{
	int glitCount = numleds / 5;
	for (size_t i = 0; i < glitCount; i++)
	{
		animBufPtr[random16(numleds)] = CHSV(statePtr[hue], statePtr[sat], statePtr[val]);
	}
	
	for (int i = 0; i < numleds; i++) {
		animBufPtr[i] = CHSV(animBufPtr[i].h, animBufPtr[i].s, dim8_video(animBufPtr[i].v));
		if (animBufPtr[i].v < 64) {
			animBufPtr[i].v = random8(60, 90);
		}
	}
	
	
}

Glitter::~Glitter()
{
}
