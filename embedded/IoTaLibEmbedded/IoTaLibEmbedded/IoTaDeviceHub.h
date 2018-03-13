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
#include "circularBuffer.h"

class IoTaDeviceHub {
	private:
		int numFuncs;
		int maxFuncs;

		IoTaFuncBase **funcs;
		HubInternalFunc * internalHandler;

		CircularBuffer<uint8_t *> broadcastStorage;






	public: 
		IoTaDeviceHub();
		int addFunc(IoTaFuncBase *func);
		int processMessage(uint8_t message[], void *clientToken);
		
		int numResponsesRemaining();
		int getNextResponse(uint8_t*buf,void *clientToken);
		
		int numBroadcastsRemaining();
		int getNextBroadcast(uint8_t*buf);


		void tick();
		int getNumFuncs();


		
};
#endif

