/*
 Name:		IoTaLibEmbedded.cpp
 Created:	10/4/2017 10:49:21 PM
 Author:	alist
 Editor:	http://www.visualmicro.com
*/

#include "IoTaDeviceHub.h"
#include <stdlib.h>
 // !HUB_SIZE




IoTaDeviceHub::IoTaDeviceHub() {
	maxFuncs = 10;
	funcs = (IoTaFuncBase **)malloc(sizeof(IoTaFuncBase*) * maxFuncs);
	
}

void IoTaDeviceHub:: tick()
{
}

int IoTaDeviceHub::addFunc(IoTaFuncBase * newFunc)
{
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
	for (int i = 0; i < numFuncs; i++) {
		if (funcs[i]->getFuncId() == message[0]) {
			funcs[i]->processCommand(message + 1, clientToken);
		}
	}
	return funcIndex;
}

uint8_t * IoTaDeviceHub::getResponses(void * clientToken)
{
	return nullptr;
}



