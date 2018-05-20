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





IoTaDeviceHub::IoTaDeviceHub(long uuid, size_t maxFuncs) {
	_uuid = uuid;
	this->maxFuncs = maxFuncs;
	numFuncs = 0;
	//funcs = (IoTaFuncBase **)malloc(sizeof(IoTaFuncBase*) * maxFuncs);	
	funcs = (new IoTaFuncBase* [maxFuncs]);
	msgStorage = new CircularBuffer<DataCapsule *>(100);
	
	internalHandler = new HubInternalFunc(funcs, &numFuncs, maxFuncs);
	addFunc(internalHandler);
}

void IoTaDeviceHub::tick()
{
	//process all attached functions 
	for (int i = 0; i < numFuncs; i++) {
		funcs[i]->tick();

		while (funcs[i]->getReponsesRemaining() > 0)
		{
			long dest = funcs[i]->getNextMsgDest();
			int len = funcs[i]->getStateBufLen();
			uint8_t data[255];
			funcs[i]->getStateBuffer(data);
			
			DataCapsule *cap;

			cap = new DataCapsule(this->_uuid, dest, funcs[i]->getFuncId(), len, data);
			msgStorage->add(cap);
		}
	}

	//gather responses into msgStorage
	

}

int IoTaDeviceHub::getNumFuncs()
{
	return numFuncs;
}



int IoTaDeviceHub::addFunc(IoTaFuncBase * newFunc)
{
	if (numFuncs < maxFuncs-1) {
		funcs[numFuncs] = newFunc;
		numFuncs++;
		return numFuncs;
	}
	else {
		return -1;
	}
}

int IoTaDeviceHub::processMessage(DataCapsule *capsule)
{	

	int funcIndex = -1;
	{
		for (int i = 0; i < numFuncs; i++) {
			if (funcs[i]->getFuncId() == capsule->getFuncId()) {
				funcs[i]->processCommand(capsule);
			}
		}
	}
	return funcIndex;
}

int IoTaDeviceHub::numCapsulesRemaining()
{
	return this->msgStorage->available();
}

int IoTaDeviceHub::getNextOutputCapsule(DataCapsule **capsulePtr)
{

	*capsulePtr = msgStorage->read();
	return 0;
}





