/*
 Name:		Esp32Scope.ino
 Created:	4/22/2018 3:40:48 PM
 Author:	alist
*/

#include <SPI.h>
#include "Pga112SpiController.h"


SPIClass * vspi = NULL;
Pga112SpiController * pga112;
int gain = 1;

void setup() {
	Serial.begin(115200);

	vspi = new SPIClass(VSPI);
	vspi->begin(); //(18, 19, 23, 5);

	pga112 = new Pga112SpiController(vspi, SPISettings(10000, MSBFIRST, SPI_MODE0));

}

// the loop function runs over and over again until power down or reset
void loop() {
	//do nothing
	if (gain != 1) {
		gain = 1;
	}
	else
	{
		gain = 2;
	}

	
	if (!pga112->setGain(gain)) {
		Serial.print("Gain is now ");
		Serial.println(gain);
	}
	delay(1000);
}


