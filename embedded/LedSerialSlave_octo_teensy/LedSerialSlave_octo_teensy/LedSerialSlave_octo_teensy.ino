/*
 Name:		LedSerialSlave_octo_teensy.ino
 Created:	10/8/2017 10:23:45 AM
 Author:	alist
*/

// the setup function runs once when you press reset or power the board




#include "LedPropEnum.h"
uint8_t state[NUM_LED_PROPS];
elapsedMillis frame_period_counter;
int frame_period_millis;


//fast led stuff
#define USE_OCTOWS2811
#include<OctoWS2811.h>
#include<FastLED.h>

#define NUM_LEDS_PER_STRIP 15
#define NUM_STRIPS 1

CHSV leds_hsv[NUM_STRIPS * NUM_LEDS_PER_STRIP];
CRGB leds[NUM_STRIPS * NUM_LEDS_PER_STRIP];



void setup() {
	Serial.begin(9600);
	for (int i = 0; i < NUM_LED_PROPS; i++) {
		state[i] = 0;
	}
	state[hue] = 128;
	state[val] = 255;
	state[sat] = 255;
	state[fps] = 60;

	LEDS.addLeds<OCTOWS2811>(leds, NUM_LEDS_PER_STRIP);
}

// the loop function runs over and over again until power down or reset

int frameReady = 0;
int pos = 0;
void loop() {
	//check serial
	frame_period_millis = (1000 / state[fps]) ;
	
	if (!frameReady) {
		for (int i = 0; i < 15; i++) {
			leds_hsv[i].v = leds_hsv[i].v / 2;
		}
		leds_hsv[pos].h = state[hue];
		leds_hsv[pos].s = state[sat];
		leds_hsv[pos].v = state[val];
		pos = pos + 1;
		if (pos == 15) {
			pos = 0;
		}

		for (int i = 0; i < 15; i++) {
			leds[i] = CHSV(leds_hsv[i].h, leds_hsv[i].s, leds_hsv[i].v);
		}


		
		frameReady = 1;
	}

	if (frame_period_counter > frame_period_millis) {
		frame_period_counter = 0;
		LEDS.show();
		Serial.println(frame_period_millis);
		frameReady = 0;
	}
}

