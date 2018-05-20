#pragma once
#include <driver\timer.h>
#include "samplerStateEnum.h"
#include <Arduino.h>
class sampler
{
	//sample rate
	//amplifier gain
	//trigger level
	//is triggered

public:
	sampler();
	int setState(samplerStateEnum item, short val);
	short * getDataBufferStart;
	int getCurrentSample();
	boolean isDataComplete();
	int resetDataBuffer();

	~sampler();
	static sampler * instance;

private:
	hw_timer_t * sampleTimer;

	static const int maxSamples = 2048;
	short data[maxSamples];
	int currentSample = 0;
	
	boolean triggered = false;
	short state[10];


	void timerFunc();
};

