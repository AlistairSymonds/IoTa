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

		IoTaFuncBase **funcs;
		HubInternalFunc * internalHandler;
		CircularBuffer<DataCapsule *> * msgStorage;


	public: 
		IoTaDeviceHub();
		int addFunc(IoTaFuncBase *func);
		int processMessage(DataCapsule *capsule);
		
		int numCapsulesForClient(long clientId);
		int getNextOutputCapsule(DataCapsule * capsulePtr);
		
		
		/*
		int numBroadcastsRemaining();
		int getNextBroadcast(uint8_t*buf);
		*/

		void tick();
		int getNumFuncs();


		
};
#endif

