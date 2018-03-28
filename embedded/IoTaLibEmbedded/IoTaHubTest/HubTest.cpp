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

			uint8_t msg[100];
			msg[0] = 4;
			msg[1] = (h.getFuncId() << 8);
			msg[2] = (uint8_t)h.getFuncId();
			msg[3] = (uint8_t)60;
			
		}
			
	};
}