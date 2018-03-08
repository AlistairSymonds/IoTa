#pragma once
#ifndef _IoTaFuncBase_h
#define _IoTaFuncBase_h
#include <stdint.h>

class IoTaFuncBase
{
public:
	virtual short getFuncId() = 0;
	virtual void processCommand(uint8_t command[],  void* clientToken) = 0;
	virtual void tick() = 0;
	
	virtual int getStateBufLen() = 0;
	virtual uint8_t * getStateBuffer(void * clientToken) = 0;
	virtual int needsStateBufferUpdate(void* clientToken) = 0;

	

private:

};
#endif