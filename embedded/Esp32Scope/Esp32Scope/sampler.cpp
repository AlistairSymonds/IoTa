#include <driver\timer.h>
#include "sampler.h"
#include "samplerStateEnum.h"



sampler::sampler()
{
	if (instance != NULL) {
		instance = this;
	}
	else {
		delete instance;
		instance = this;
	}
	



}

int sampler::setState(samplerStateEnum item, short val)
{	
	if (item == SAMPLE_RATE_DIVISION || item == SAMPLE_RATE_BASE) {
		state[item] = val;

		sampleTimer = timerBegin(0, state[SAMPLE_RATE_BASE], true);
		timerAlarmWrite(sampleTimer, state[SAMPLE_RATE_DIVISION], true);
		timerAlarmEnable(sampleTimer);
		//timerAttachInterrupt(sampleTimer, &(this->timerFunc), true);
	}
	
	return 0;
}


int sampler::getCurrentSample()
{
	return currentSample;
}

boolean sampler::isDataComplete()
{
	return boolean();
}

int sampler::resetDataBuffer()
{
	return 0;
}

sampler::~sampler()
{
}

void IRAM_ATTR sampler::timerFunc()
{
	short newVal = analogRead(34);
	data[currentSample] = newVal;

	if (data[currentSample] > state[TRIGGER_LEVEL]) {
		state[IS_TRIGGERED] = true;
	}
	currentSample++;
	
	if (state[IS_TRIGGERED]) {
		if (currentSample > maxSamples) {
			currentSample = 0;
		}
	}
	else {
		if (currentSample > (maxSamples/2)) {
			currentSample = 0;
		}
	}
	Serial.println("sample");
}

//timer function
/*
if(not triggered){
	data[currentSample] = analogRead();
}

if(data[currentSample] > triggerVal){
	triggered = true;

currentSample++;

if(triggered){
	if(currentSample > maxSamples){
		currentSample = 0;
	}
} else {
	if(currentSample > maxSamples/2){
		currentSample = 0;
	}

}
*/