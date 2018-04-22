#pragma once
#ifndef _IoTaFuncBase_h
#define _IoTaFuncBase_h
#include <stdint.h>
#include "DataCapsule.h"


class IoTaFuncBase
{
public:
	//get the funcId
	virtual short getFuncId() = 0;

	//submit a command and unique identifier of who sent it (ie, memory address of
	//client in memory)
	virtual void processCommand(DataCapsule *capsule) = 0;
	virtual void tick() = 0;
	
	virtual int isStateBufferUpdated(long clientId) = 0;
	virtual int isStateBufferUpdated() = 0;

	virtual int getStateBufLen() = 0;
	virtual int getStateBuffer(DataCapsule *capsule) = 0;
	

	

private:

};
#endif