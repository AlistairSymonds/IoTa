/*
 Name:		IoTaLibEmbedded.cpp
 Created:	10/4/2017 10:49:21 PM
 Author:	alist
 Editor:	http://www.visualmicro.com
*/

#include "IoTaDeviceHub.h"
#include <stdlib.h>
#include "HubInternalFunc.h"
 // !HUB_SIZE


#ifdef _WIN32
#include <memory.h>
#endif

#ifdef __arm__
#include <string.h>
#endif

#ifdef ESP8266
#include <string.h>
#endif // 





IoTaDeviceHub::IoTaDeviceHub() {
	maxFuncs = 10;
	numFuncs = 0;
	funcs = (IoTaFuncBase **)malloc(sizeof(IoTaFuncBase*) * maxFuncs);	
	internalHandler = new HubInternalFunc(&numFuncs, maxFuncs);
	addFunc(internalHandler);
}

void IoTaDeviceHub::tick()
{

}

int IoTaDeviceHub::getNumFuncs()
{
	return numFuncs;
}



int IoTaDeviceHub::addFunc(IoTaFuncBase * newFunc)
{
	internalHandler->addFuncId(newFunc->getFuncId());
	if (numFuncs < maxFuncs) {
		funcs[numFuncs] = newFunc;
		numFuncs++;
		return numFuncs;
	}
	else {
		return -1;
	}
}

int IoTaDeviceHub::processMessage(uint8_t message[], void* clientToken)
{
	int funcIndex = -1;
	{
		for (int i = 0; i < numFuncs; i++) {
			if (funcs[i]->getFuncId() == message[2]) {
				uint8_t * cmdStart = message + 3;
				funcs[i]->processCommand(cmdStart, clientToken);
			}
		}
	}
	return funcIndex;
}

int IoTaDeviceHub::copyAndFormatResponses(uint8_t * buf, void * clientToken)
{
	uint8_t msg[255];
	int bytesAdded = 1;
	for (int i = 0; i < numFuncs; i++) {
		if (funcs[i]->needsStateBufferUpdate(clientToken)) {
			short id = funcs[i]->getFuncId();
			msg[bytesAdded] = 0;
			msg[bytesAdded+1] = (uint8_t)id;
			bytesAdded = bytesAdded + 2;

			int len = funcs[i]->getStateBufLen();
			memcpy(msg+bytesAdded, funcs[i]->getStateBuffer(clientToken), len);
			bytesAdded = bytesAdded + len;
		
		}
	}
	msg[0] = bytesAdded;
	memcpy(buf, msg, msg[0]);
	return 0;
}




