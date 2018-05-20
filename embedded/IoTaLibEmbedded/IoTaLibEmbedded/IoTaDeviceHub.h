/*
 Name:		IoTaLibEmbedded.h
 Created:	10/4/2017 10:49:21 PM
 Author:	alist
 Editor:	http://www.visualmicro.com
*/

#ifndef _IoTaDeviceHub_h
#define _IoTaDeviceHub_h

#include <stdint.h>
#include "IoTaFuncBase.h"
#include "HubInternalFunc.h"
#include "DataCapsule.h"
#include "circularBuffer.h"

class IoTaDeviceHub {
	private:
		int numFuncs;
		int maxFuncs;
		long _uuid;
		IoTaFuncBase **funcs;
		HubInternalFunc * internalHandler;
		CircularBuffer<DataCapsule *> * msgStorage;


	public: 
		IoTaDeviceHub(long uuid, size_t maxFuncs);
		int addFunc(IoTaFuncBase *func);
		int processMessage(DataCapsule *capsule);
		
		int numCapsulesRemaining();
		int getNextOutputCapsule(DataCapsule **capsulePtr);
		

		void tick();
		int getNumFuncs();


		
};
#endif

