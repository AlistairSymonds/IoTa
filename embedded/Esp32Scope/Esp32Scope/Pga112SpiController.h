#pragma once
#include <SPI.h>
//this class is provide an interface the physical SPI BUS and the software for 
//controlling the TI PGA112
class Pga112SpiController
{
public:
	Pga112SpiController(SPIClass * spiIn, SPISettings settingsIn);
	int setGain(int gain);
	int getGain();
	int setSpiSettings(SPISettings newSettings);
	~Pga112SpiController();

private:
	SPIClass * spi;
	SPISettings spiSettings;
	int gain;
};

