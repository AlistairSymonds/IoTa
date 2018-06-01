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

	
	virtual void processCommand(DataCapsule *capsule) = 0;
	virtual void tick() = 0;
	
	virtual int getReponsesRemaining() = 0;
	virtual long getNextMsgDest() = 0;

	virtual int getStateBufLen() = 0;
	virtual int getStateBuffer(uint8_t *buf) = 0;
	

	

private:

};
#endif