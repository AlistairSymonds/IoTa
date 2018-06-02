#include "Pga112SpiController.h"
#include <Arduino.h>
#include <SPI.h>




Pga112SpiController::Pga112SpiController(SPIClass * spiIn, SPISettings settingsIn)
{
	//vpsi pin
	this->spi = spiIn;
	// SCLK = 18, MISO = 19, MOSI = 23, SS = 5
	pinMode(5, OUTPUT);
	digitalWrite(5, HIGH);
	this->spiSettings._bitOrder = settingsIn._bitOrder;
	this->spiSettings._clock = settingsIn._clock;
	this->spiSettings._dataMode = settingsIn._dataMode;

	this->gain = 0;
}


void Pga112SpiController::init() {
	this->spi->begin(18,19,23,5);
}


//PGA 112 gain selection, if an invalid selection is made no SPI transaction is attempted and -1 returned
//Valid gain: 1, 2, 4, 8, 16, 32, 64, 128
int IRAM_ATTR Pga112SpiController::setGain(int newGain)
{
	Serial.println(newGain);
	uint16_t msg = 0;
	msg = 0b00101010 << 8;
	switch (newGain)
	{
	case(1):
		msg |= 0b00000001;
		break;
	case(2):
		msg |= 0b00010001;
		break;
	case(4):
		msg |= 0b00100001;
		break;
	case(8):
		msg |= 0b00110001;
		break;
	case(16):
		msg |= 0b01000001;
		break;
	case(32):
		msg |= 0b01010001;
		break;
	case(64):
		msg |= 0b01100001;
		break;
	case(128):
		msg |= 0b01110001;
		break;
	default:
		return -1;
		break;
	}(gain);
	this->gain = newGain;
	digitalWrite(5, LOW);
	spi->beginTransaction(SPISettings(1000, MSBFIRST, SPI_MODE0));
	spi->transfer16(msg);
	spi->endTransaction();
	digitalWrite(5, HIGH);
	Serial.println(msg, BIN);
	

	return 0;
}

int Pga112SpiController::getGain()
{
	return this->gain;
}

int Pga112SpiController::setSpiSettings(SPISettings newSettings)
{
	return 0;
}

Pga112SpiController::~Pga112SpiController()
{
}
