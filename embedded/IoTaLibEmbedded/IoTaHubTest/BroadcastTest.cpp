#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"
#include "DummyBroadcaster.h"
#include "iota_defines.h"
using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(HubTest)
	{
		long uuid = 123456789;
		TEST_METHOD(hub_add) {
			IoTaDeviceHub hub(uuid,10);
			Heartbeat h;
			hub.addFunc(&h);
		}

		TEST_METHOD(dummyBroadcast) {

			IoTaDeviceHub hub(uuid, 10);
			Heartbeat h;
			hub.addFunc(&h);
			DummyBroadcaster db;
			hub.addFunc(&db);

			hub.tick();
		
			Assert::AreEqual(1, hub.numCapsulesRemaining());
			DataCapsule *txCap;
			hub.getNextOutputCapsule(&txCap);

			Assert::AreEqual(0, hub.numCapsulesRemaining());

			Assert::AreEqual(0L, txCap->getDestination());
			
			//convert it capsule for delivery to clientID = 9999;
			long newDest = 9999;
			DataCapsule *specificCap;
			specificCap = new DataCapsule(txCap, newDest);
			Assert::AreEqual(txCap->getFuncId(), specificCap->getFuncId());
			
			

			
			
			
		}
			
	};
}