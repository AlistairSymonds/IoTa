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
			IoTaDeviceHub hub(uuid);
			Heartbeat h;
			hub.addFunc(&h);
		}

		TEST_METHOD(dummyBroadcast) {

			IoTaDeviceHub hub(uuid);
			Heartbeat h;
			hub.addFunc(&h);
			DummyBroadcaster db;
			hub.addFunc(&db);

			hub.tick();

			

			
			
			
		}
			
	};
}