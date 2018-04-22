#ifndef _HUBFUNCINTERNAL_H
#define _HUBFUNCINTERNAL_H

#include "IoTaFuncBase.h"
#include "fixedMap.h"
#include <stdlib.h>


//this class handles communication with the hub class for outside inputs
//as usually the hub itself is designed to be transparent to others
class HubInternalFunc : public IoTaFuncBase {
public:
	HubInternalFunc(int * numFuncs, int maxFuncs);
	short getFuncId();
	void processCommand(DataCapsule *capsule);
	void tick();


	int isStateBufferUpdated(long clientId);
	int isStateBufferUpdated();

	int getStateBufLen();
	int getStateBuffer(DataCapsule *capsule);

	void addFuncId(uint8_t id);

	~HubInternalFunc();
private:
	uint8_t * funcIds;
	int * numFuncsPtr;
	fixedMap<long> *map;

};
#endif
