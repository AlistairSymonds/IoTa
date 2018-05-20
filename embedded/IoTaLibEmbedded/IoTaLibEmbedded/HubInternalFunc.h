#ifndef _HUBFUNCINTERNAL_H
#define _HUBFUNCINTERNAL_H

#include "IoTaFuncBase.h"
#include "circularBuffer.h"
#include <stdlib.h>


//this class handles communication with the hub class for outside inputs
//as usually the hub itself is designed to be transparent to others
class HubInternalFunc : public IoTaFuncBase {
public:
	HubInternalFunc(IoTaFuncBase ** funcsPtr, int * numFuncs, int maxFuncs);
	short getFuncId();
	void processCommand(DataCapsule *capsule);
	void tick();


	int getReponsesRemaining();
	long getNextMsgDest();

	int getStateBufLen();
	int getStateBuffer(uint8_t *buf);

	~HubInternalFunc();
private:
	IoTaFuncBase * * funcsPtr;
	int * numFuncsPtr;
	CircularBuffer<long> *responseMap;
};
#endif
