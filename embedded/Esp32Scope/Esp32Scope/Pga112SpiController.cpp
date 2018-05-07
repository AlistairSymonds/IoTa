#include "Pga112SpiController.h"

#include <SPI.h>




Pga112SpiController::Pga112SpiController(SPIClass * spiIn, SPISettings settingsIn)
{
	this->spi = spiIn;
	spi->setHwCs(true);
	this->spiSettings = settingsIn;
	this->gain = -1;
}





//PGA 112 gain selection, if an invalid selection is made no SPI transaction is attempted and -1 returned
//Valid gain: 1, 2, 4, 8, 16, 32, 64, 128
int Pga112SpiController::setGain(int gain)
{
	uint8_t msg[2];
	msg[0] = 0b00101010;
	switch (gain)
	{
	case(1):
		msg[1] = 0b00000001;
		break;
	case(2):
		msg[1] = 0b00010001;
		break;
	case(4):
		msg[1] = 0b00100001;
		break;
	case(8):
		msg[1] = 0b00110001;
		break;
	case(16):
		msg[1] = 0b01000001;
		break;
	case(32):
		msg[1] = 0b01010001;
		break;
	case(64):
		msg[1] = 0b01100001;
		break;
	case(128):
		msg[1] = 0b01110001;
		break;
	default:
		return -1;
		break;
	}(gain);

	this->gain = gain;
	spi->beginTransaction(spiSettings);
	spi->writeBytes(msg, 2);
	spi->endTransaction();
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
