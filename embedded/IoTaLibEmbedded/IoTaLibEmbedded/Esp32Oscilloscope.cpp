#ifdef ESP32
#include <driver\spi_common.h>
#include <driver\spi_master.h>


#include "Esp32Oscilloscope.h"

Esp32Oscilloscope::Esp32Oscilloscope()
{


	//initialise PGA interface on HSPi
	// MOSI = 13
	// cs = 15
	// sclk = 14
	// 
	//
	spi_bus_config_t pgaPins={
		.mosi_io_num = 13,
		.sclk_io_num = 15,

		.miso_io_num = -1,
		.quadwp_io_num = 1,
		.quadhd_io_num = -1,
		.max_transfer_sz = 32

	};
}

short Esp32Oscilloscope::getFuncId()
{
	return 32;
}

void Esp32Oscilloscope::processCommand(DataCapsule capsule)
{
}

void Esp32Oscilloscope::tick()
{
}

int Esp32Oscilloscope::isStateBufferUpdated(void * clientToken)
{
	return 0;
}

int Esp32Oscilloscope::isStateBufferUpdated()
{
	return 0;
}

int Esp32Oscilloscope::getStateBufLen()
{
	return 0;
}

int Esp32Oscilloscope::getStateBuffer(uint8_t * buffer)
{
	return 0;
}

Esp32Oscilloscope::~Esp32Oscilloscope()
{
}
#endif // ESP32