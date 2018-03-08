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
	//int t0 = micros();
	int glitCount = random16(0,numleds*(state[density]/255));
	for (size_t i = 0; i < glitCount; i++)
	{
		animBufPtr[random16(numleds)] = CHSV(statePtr[hue], statePtr[sat], statePtr[val]);
	}
	
	for (int i = 0; i < numleds; i++) {
		animBufPtr[i] = CHSV(animBufPtr[i].h, animBufPtr[i].s, dim8_raw(animBufPtr[i].v));
		if (animBufPtr[i].v < 90) {
			animBufPtr[i].v = random8(100, 120);
		}
	}
	//Serial.println(micros() - t0);
	//approx 150 us with 400leds
}

Glitter::~Glitter()
{
}
