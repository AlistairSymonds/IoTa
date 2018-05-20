
#include <stdio.h>
#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "MemCheck.h"
#include "heartbeat.h"
#include "DummyBroadcaster.h"
#include "iota_defines.h"
#include "iota_util.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(HubInternalFuncTest)
	{
	public:

		TEST_METHOD(getAttchedFuncIds) {
			long source = 99;
			long dest = 245;

			IoTaDeviceHub hub(dest, 10);

			Heartbeat hbeat;
			hub.addFunc(&hbeat);


			
			short id = FID_HUB;

			uint8_t data[2];
			typeConv::short2bytes(FID_HUB, data);
			
			
			CrtCheckMemory chk;

			DataCapsule c(source, dest,id,2,data);

			hub.processMessage(&c);

			Assert::AreEqual(0, hub.numCapsulesRemaining());
			hub.tick();

			Assert::AreEqual(1, hub.numCapsulesRemaining());

			DataCapsule *returnCap;
			hub.getNextOutputCapsule(&returnCap);

			Assert::AreEqual(source, returnCap->getDestination());
			Assert::AreEqual((short)FID_HUB, returnCap->getDestFunc());

			uint8_t *dataOut;
			
			dataOut = new uint8_t[returnCap->getDataSize()];
			returnCap->copyDataOut(dataOut);

			Assert::AreEqual((short) 4, returnCap->getDataSize());
			
			short testVal = typeConv::bytes2short(dataOut);
			Assert::AreEqual((short)FID_HUB, testVal);

			testVal = typeConv::bytes2short(&dataOut[2]);
			Assert::AreEqual((short)FID_HEARTBEAT, testVal);


			Assert::AreEqual(source, returnCap->getDestination());
			Assert::AreEqual(dest, returnCap->getSource());
			Assert::AreEqual((short)FID_HUB, returnCap->getFuncId());

			Assert::AreEqual(0, hub.numCapsulesRemaining());
			
			

		}

		

	};


}