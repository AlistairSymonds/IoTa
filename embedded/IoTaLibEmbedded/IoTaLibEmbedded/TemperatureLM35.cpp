#include "iota_defines.h"
#include "TemperatureLM35.h"

//#include <Arduino.h>


TemperatureLM35::TemperatureLM35(int adc_pin)
{
	pinNum = adc_pin;
	fh = new fixedHeap<void*>(10);
	temp_mCelsius = 1000;
}

short TemperatureLM35::getFuncId()
{
	return TEMPERATURE_ID;
}

void TemperatureLM35::processCommand(uint8_t command[], void * clientToken)
{
	if (command[0] == TEMPERATURE_ID) {
		fh->add(clientToken);
	}
}

void TemperatureLM35::tick()
{
	temp_mCelsius = 700;
}

int TemperatureLM35::getStateBufLen()
{
	return 4;
}

uint8_t * TemperatureLM35::getStateBuffer(void * clientToken)
{
	return (uint8_t * )&temp_mCelsius;
}

int TemperatureLM35::needsStateBufferUpdate(void * clientToken)
{
	return fh->contains(clientToken);
}


TemperatureLM35::~TemperatureLM35()
{
}
