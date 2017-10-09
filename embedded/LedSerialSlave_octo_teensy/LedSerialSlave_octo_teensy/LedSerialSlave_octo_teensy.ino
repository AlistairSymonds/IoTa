/*
 Name:		LedSerialSlave_octo_teensy.ino
 Created:	10/8/2017 10:23:45 AM
 Author:	alist
*/

//fast led stuff
#define USE_OCTOWS2811
#include<OctoWS2811.h>
#include<FastLED.h>

#define NUM_LEDS_PER_STRIP 15
#define NUM_STRIPS 1

CHSV animBuf[NUM_STRIPS * NUM_LEDS_PER_STRIP];
CRGB frameBuf[NUM_STRIPS * NUM_LEDS_PER_STRIP];


#include "LedPropEnum.h"
uint8_t state[NUM_LED_PROPS];


elapsedMillis frame_period_counter;
unsigned int frame_period_millis;

elapsedMillis anim_period_counter;
unsigned int anim_period_millis;


#include <stdlib.h>


#include "LedAnimBase.h"
#include "SolidColour.h"

LedAnimBase * programs[5];

void processSerial();

void setup() {
	Serial.begin(9600);
	for (int i = 0; i < NUM_LED_PROPS; i++) {
		state[i] = 0;
	}
	state[hue] = 128;
	state[val] = 255;
	state[sat] = 0;
	state[fps] = 10;

	SolidColour * scPtr = new SolidColour(animBuf, state);
	programs[0] = scPtr;

	Serial1.begin(9600);
	LEDS.addLeds<OCTOWS2811>(frameBuf, NUM_LEDS_PER_STRIP);
}

// the loop function runs over and over again until power down or reset

int frameReady = 0;
int pos = 0;
void loop() {
	//check serial
	frame_period_millis = (1000 / state[fps]) ;

	while (Serial1.available() > 0) {
		processSerial();
	}


	if (anim_period_counter > anim_period_millis) {
		memcpy(frameBuf, animBuf, NUM_STRIPS * NUM_LEDS_PER_STRIP);
		anim_period_counter = 0;
	}

	if (frame_period_counter > frame_period_millis) {
		frame_period_counter = 0;
		LEDS.show();
		Serial.println(frame_period_millis);
		frameReady = 0;
	}
}

void processSerial()
{
	uint8_t msgLen = Serial1.read();
	uint8_t msgBuf[255];
	Serial1.readBytes(msgBuf + 1, msgLen);
	
	if (msgBuf[1] == 0)  //set command
	{
		state[msgBuf[2]] = msgBuf[3];
	}
	else if (msgBuf[1] == 1) //get command
	{
		if (msgBuf[2] == 1) {
			Serial1.write(state, NUM_LED_PROPS);
		}
	}
}
