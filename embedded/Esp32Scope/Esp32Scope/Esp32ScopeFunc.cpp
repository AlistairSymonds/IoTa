#ifdef ESP32


#include "Esp32ScopeFunc.h"
#include "iota_defines.h"

Esp32ScopeFunc::Esp32ScopeFunc()
{


	//initialise PGA interface on HSPi
	// MOSI = 13
	// cs = 15
	// sclk = 14
	// 
	//
	
}

short Esp32ScopeFunc::getFuncId()
{
	return FID_32SCOPE;
}

void Esp32ScopeFunc::processCommand(DataCapsule *capsule)
{

}

void Esp32ScopeFunc::tick()
{
}

int Esp32ScopeFunc::getReponsesRemaining()
{
	return 0;
}

long Esp32ScopeFunc::getNextMsgDest()
{
	return 0;
}



int Esp32ScopeFunc::getStateBufLen()
{
	return 0;
}

int Esp32ScopeFunc::getStateBuffer(uint8_t * buffer)
{
	return 0;
}

Esp32ScopeFunc::~Esp32ScopeFunc()
{
}
#endif // ESP32