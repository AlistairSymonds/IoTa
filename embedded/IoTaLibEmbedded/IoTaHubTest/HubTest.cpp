#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"
#include "TemperatureLM35.h"
#include "iota_defines.h"

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
			hub.getResponse(output, tok);

			uint8_t expected[] = {4,0,1,60};
			for (int i = 0; i < 4; i++) {
				char iterStr[] = { 'i','t','e','r',' ',i,0 };
				Assert::AreEqual(expected[i], output[i]);
			}
			
		}


		TEST_METHOD(hub_interleaved_requests) {
			IoTaDeviceHub hub;
			Heartbeat h;
			TemperatureLM35 tmp35(35);

			hub.addFunc(&h);
			hub.addFunc(&tmp35);

			uint8_t msg[100];
			msg[0] = 4;
			msg[1] = 0; //should be msb of funcId oops...
			msg[2] = (uint8_t)h.getFuncId();
			msg[3] = (uint8_t)60;
			hub.processMessage(msg, tok);

			uint8_t msg2[4];
			msg2[0] = 4;
			msg2[1] = 0;
			msg2[2] = tmp35.getFuncId();
			msg2[3] = tmp35.getFuncId();

			hub.processMessage(msg2, tok);

			hub.tick();

			uint8_t output[255];
			hub.getResponse(output, tok);

			uint8_t expected[] = { 10,0,1,60 };
			for (int i = 0; i < 4; i++) {
				char iterStr[] = { 'i','t','e','r',' ',i,0 };
				Assert::AreEqual(expected[i], output[i]);
			}
			//this works, the problem is in the byte ordering of the int
			//out of the temp sensor
			}


		TEST_METHOD(hub_nothing_requested) {
			IoTaDeviceHub hub;
			Heartbeat h;
			TemperatureLM35 tmp35(35);

			hub.addFunc(&h);
			hub.addFunc(&tmp35);

			hub.tick();

			uint8_t output[3];
			hub.getResponse(output, tok);

			Assert::AreEqual((uint8_t)1, output[0]);
		}

		TEST_METHOD(hub_get_attached_funcs) {
			IoTaDeviceHub hub;
			Heartbeat h;
			TemperatureLM35 tmp35(35);

			hub.addFunc(&h);
			hub.addFunc(&tmp35);

			uint8_t msg[4];
			msg[0] = 4;
			msg[1] = 0;
			msg[2] = (uint8_t) HUB_ID;
			msg[3] = (uint8_t)HUB_ID;
			hub.processMessage(msg, tok);

			hub.tick();

			uint8_t output[10];
			hub.getResponse(output, tok);

			Assert::AreEqual((uint8_t)6, output[0]);
			Assert::AreEqual((uint8_t)0, output[1]);
			Assert::AreEqual((uint8_t)255, output[2]);
			Assert::AreEqual((uint8_t)255, output[3]);
			Assert::AreEqual((uint8_t)h.getFuncId(), output[4]);
			Assert::AreEqual((uint8_t)tmp35.getFuncId(), output[5]);
		}
	};
}