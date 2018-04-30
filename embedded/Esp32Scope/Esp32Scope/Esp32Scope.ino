/*
 Name:		Esp32Scope.ino
 Created:	4/22/2018 3:40:48 PM
 Author:	alist
*/
#include <SPI.h>
// the setup function runs once when you press reset or power the board
void setup() {
	Serial.begin(9600);
	pinMode(5, OUTPUT);
	digitalWrite(5, HIGH);
	SPI.begin(14, 12, 13, 5);
}

// the loop function runs over and over again until power down or reset
void loop() {
	Serial.println("Sending command");
	setGain();
	delay(1);
	
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