#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(HubTest)
	{
		void * tok;
		TEST_METHOD(hub_add) {
			IoTaDeviceHub hub;
			Heartbeat h;
			hub.addFunc(&h);
		}

		TEST_METHOD(hub_lifecycle_one_client) {
			IoTaDeviceHub hub;
			Heartbeat h;
			hub.addFunc(&h);
			uint8_t msg[100];
			msg[0] = 4;
			msg[1] = 0; //should be msb of funcId oops...
			msg[2] = (uint8_t) h.getFuncId();
			msg[3] = (uint8_t) 60;
			hub.processMessage(msg, tok);

			hub.tick();

			uint8_t output[255];
			hub.copyAndFormatResponses(output, tok);

			uint8_t expected[] = {4,0,1,60};
			for (int i = 0; i < 4; i++) {
				char iterStr[] = { 'i','t','e','r',' ',i,0 };
				Assert::AreEqual(expected[i], output[i]);
			}
			
		}
	};
}