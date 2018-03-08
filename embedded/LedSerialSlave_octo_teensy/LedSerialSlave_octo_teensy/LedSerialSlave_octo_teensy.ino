/*
 Name:		LedSerialSlave_octo_teensy.ino
 Created:	10/8/2017 10:23:45 AM
 Author:	alist
*/

//fast led stuff
#define USE_OCTOWS2811
#include<OctoWS2811.h>
#include<FastLED.h>

#define NUM_LEDS_PER_STRIP 400
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
#include "RainbowAnim.h"
#include "Glitter.h"

LedAnimBase * programs[5];

void processSerial();

void setup() {
	Serial.begin(9600);
	for (int i = 0; i < NUM_LED_PROPS; i++) {
		state[i] = 0;
	}
	state[hue] = 38;
	state[brightness] = 255;
	state[sat] = 225;
	state[val] = 239;
	state[fps] = 60;
	state[density] = 


	state[pid] = 2;
	
	state[anim_hz] = 10;

	programs[0] = new SolidColour(animBuf, NUM_STRIPS * NUM_LEDS_PER_STRIP, state);
	programs[1] = new RainbowAnim(animBuf, NUM_STRIPS * NUM_LEDS_PER_STRIP, state);
	programs[2] = new Glitter(animBuf, NUM_STRIPS * NUM_LEDS_PER_STRIP, state);

	Serial1.begin(9600);
	LEDS.addLeds<OCTOWS2811>(frameBuf, NUM_LEDS_PER_STRIP);
}

// the loop function runs over and over again until power down or reset

int frameReady = 0;
int pos = 0;
void loop() {
	//check serial
	frame_period_millis = (1000 / state[fps]) ;
	anim_period_millis = (1000 / state[anim_hz]);

	while (Serial1.available() > 0) {
		processSerial();
	}

	LEDS.setBrightness(state[brightness]);

	if (anim_period_counter > anim_period_millis) {
		programs[state[pid]]->advanceAnim();

		state[hue] = state[hue] + state[delta_hue];
		state[sat] = state[sat] + state[delta_sat];
		state[val] = state[val] + state[delta_val];


		//memcpy doesn't work here due to the byte representation's being different. oops		
		for (int i = 0; i < NUM_STRIPS * NUM_LEDS_PER_STRIP; i++) {
			frameBuf[i].setHSV(animBuf[i].h, animBuf[i].s, animBuf[i].v);
		}
		anim_period_counter = 0;
	}

	if (frame_period_counter > frame_period_millis) {
		frame_period_counter = 0;
		LEDS.show();
		frameReady = 0;
	}
}

void processSerial()
{
	uint8_t msgLen = Serial1.read();
	uint8_t msgBuf[255];
	msgBuf[0] = msgLen;
	Serial1.readBytes(msgBuf + 1, msgLen);
	
	//echo everything recieved over hardware serial through usb serial
	for (int i = 0; i < msgBuf[0]; i++) {
		Serial.print(msgBuf[i]);
		Serial.print(" ");
	}
	Serial.println();
	

	if (msgBuf[1] == 0)  //set command
	{
		state[msgBuf[2]] = msgBuf[3];
		
		
		Serial1.write(state, NUM_LED_PROPS);
		Serial.println("Sent state");
	}
	else if (msgBuf[1] == 1) //get command
	{
		if (msgBuf[2] == 1) {
			Serial1.write(state, NUM_LED_PROPS);
		}
	}
}
