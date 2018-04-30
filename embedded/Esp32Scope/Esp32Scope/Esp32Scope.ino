/*
 Name:		Esp32Scope.ino
 Created:	4/22/2018 3:40:48 PM
 Author:	alist
*/
#include <SPI.h>
// the setup function runs once when you press reset or power the board


int debugPin = 35;
int debugPinVal = 1;

SPIClass * vspi = NULL;
SPIClass * hspi = NULL;

hw_timer_t * sampleTimer = NULL; 
void setup() {
	Serial.begin(9600);

	vspi = new SPIClass(3);
	hspi = new SPIClass(2);
	//set up slave select pins as outputs
	pinMode(5, OUTPUT);
	pinMode(15, OUTPUT);
	//clock miso mosi ss
	vspi->begin(); //(18, 19, 23, 5);
	hspi->begin(); //(14, 12, 13, 15);

	pinMode(debugPin, OUTPUT);
	sampleTimer = timerBegin(0, 80, true);
	timerAlarmWrite(sampleTimer, 1000000, true);
	timerAlarmEnable(sampleTimer);
	timerAttachInterrupt(sampleTimer, &timerFunction, true);
}

// the loop function runs over and over again until power down or reset
void loop() {
	//do nothing
	setGain();
	getADC();
	delay(100);
}

void setGain() {
	byte cmdType = 0b0010101;
	byte cmdData = 0b00010010;

	vspi->beginTransaction(SPISettings(100000, MSBFIRST, SPI_MODE0));
	vspi->transfer(cmdType);
	vspi->transfer(cmdData);
	vspi->endTransaction();
}

void getADC() {
	byte stuff = 0b11001100;
	hspi->beginTransaction(SPISettings(100000, MSBFIRST, SPI_MODE0));
	hspi->transfer(stuff);
	hspi->endTransaction();
}

void IRAM_ATTR timerFunction() {
	//portENTER_CRITICAL_ISR(&sampleTimer);
	
	
	//portEXIT_CRITICAL_ISR(&sampleTimer);
}