/*
 Name:		Esp32Scope.ino
 Created:	4/22/2018 3:40:48 PM
 Author:	alist
*/
#include <SPI.h>
// the setup function runs once when you press reset or power the board


int debugPin = 35;
int debugPinVal = 1;

hw_timer_t * sampleTimer = NULL; 
void setup() {
	Serial.begin(9600);
	pinMode(5, OUTPUT);
	digitalWrite(5, HIGH);
	SPI.begin(14, 12, 13, 5);

	pinMode(debugPin, OUTPUT);
	sampleTimer = timerBegin(0, 80, true);
	timerAlarmWrite(sampleTimer, 1000000, true);
	timerAlarmEnable(sampleTimer);
	timerAttachInterrupt(sampleTimer, &timerFunction, true);
}

// the loop function runs over and over again until power down or reset
void loop() {
	//do nothing

}

void setGain() {
	byte cmdType = 0b0010101;
	byte cmdData = 0b00010010;
	SPI.beginTransaction(SPISettings(10000, MSBFIRST, SPI_MODE0));

	digitalWrite(5, LOW);
	delayMicroseconds(100);
	SPI.transfer(cmdType);
	SPI.transfer(cmdData);
	delayMicroseconds(100);
	digitalWrite(5, HIGH);

	SPI.endTransaction();
}

void IRAM_ATTR timerFunction() {
	//portENTER_CRITICAL_ISR(&sampleTimer);
	Serial.println("Interrupt!");
	digitalWrite(debugPin, debugPinVal);
	if (debugPinVal == 1) {
		debugPinVal = 0;
	}
	else
	{
		debugPinVal = 1;
	}
	//portEXIT_CRITICAL_ISR(&sampleTimer);
}