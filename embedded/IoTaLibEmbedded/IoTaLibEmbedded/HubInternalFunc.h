#ifndef _HUBFUNCINTERNAL_H
#define _HUBFUNCINTERNAL_H

#include "IoTaFuncBase.h"
#include "fixedMap.h"
#include <stdlib.h>

class HubInternalFunc : public IoTaFuncBase {
public:
	HubInternalFunc(int * numFuncs, int maxFuncs);
	short getFuncId();
	void processCommand(uint8_t command[], void* clientToken);
	void tick();

	int getStateBufLen();
	uint8_t * getStateBuffer(void * clientToken);
	int needsStateBufferUpdate(void* clientToken);

	void addFuncId(uint8_t id);

	~HubInternalFunc();
private:
	uint8_t * funcIds;
	int * numFuncsPtr;
	fixedMap<void *> *fh;

};
#endif
