#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"
#include "iota_defines.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(HubTest)
	{
		
		TEST_METHOD(hub_add) {
			IoTaDeviceHub hub;
			Heartbeat h;
			hub.addFunc(&h);
		}

		TEST_METHOD(heartbeat_test) {

			IoTaDeviceHub hub;
			Heartbeat h;
			hub.addFunc(&h);
			long source = 1;
			uint8_t msg[] = { (uint8_t)HEARTBEAT_ID };

			DataCapsule *cap;
			cap = new DataCapsule(source, 2, short(HEARTBEAT_ID), 1, msg);
			
			hub.processMessage(cap);
			delete cap;

			hub.tick();

			int capsToBeReturned = hub.numCapsulesForClient(source);
			Assert::AreEqual(capsToBeReturned, 1);

			for (size_t i = 0; i < capsToBeReturned; i++)
			{

			}
			
		}
			
	};
}