#include "stdafx.h"
#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{		
	TEST_CLASS(HeartbeatInHub)
	{
		public:
			IoTaDeviceHub hub;
			Heartbeat hb;
			int fakeClient = 0;
			TEST_METHOD(addFunc) {
				hub.addFunc(&hb);

				uint8_t msg[] = { (uint8_t)60 };
				hub.processMessage(msg, &fakeClient);
				/*
				uint8_t* outputBuffer;
				outputBuffer = hub.getResponses(&fakeClient);
				
				uint8_t expected = 60;
				Assert::AreEqual(expected,outputBuffer[0]);
				*/
			}
			
	};

	TEST_CLASS(HeartbeatTest)
	{
	public:
		
		TEST_METHOD(getFuncId)
		{
			Heartbeat hb;
			void* tok = &hb;
			short expected = 1;
			short actual = hb.getFuncId();
			Assert::AreEqual(expected, actual);
		}

		TEST_METHOD(Heartbeat_Need)
		{
			Heartbeat hb;
			void* tok = &hb;
			uint8_t beatCmd[] = { (uint8_t)60 };
			hb.processCommand(beatCmd,tok);

			uint8_t response = 255;
			uint8_t *responsePtr = &response;
			if (hb.needsStateBufferUpdate(tok)) {
				hb.copyStateBuf(responsePtr, tok);
			}

			Assert::AreEqual((uint8_t)60, response);
		}

		TEST_METHOD(Heartbeat_NoNeed) {
			Heartbeat hb;
			void* tok = &hb;
			uint8_t beatCmd[] = { (uint8_t)60 };
			hb.processCommand(beatCmd, tok);

			uint8_t response = 255;
			uint8_t *responsePtr = &response;
			int x;
			hb.copyStateBuf(responsePtr, &x);
			

			Assert::AreEqual((uint8_t)60, response);
		}

		TEST_METHOD(Heartbeat_noRespReady) {
			Heartbeat hb;
			void* tok = &hb;
			Assert::AreEqual(0,hb.needsStateBufferUpdate(tok));

		}

	};
}