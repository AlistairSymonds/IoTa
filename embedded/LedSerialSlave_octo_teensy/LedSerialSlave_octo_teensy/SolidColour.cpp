#include "SolidColour.h"





SolidColour::SolidColour(CHSV * leds, uint8_t * state)
{
	animBufPtr = leds;
	statePtr = state;
}

void SolidColour::advanceAnim()
{

}

SolidColour::~SolidColour()
{
}
