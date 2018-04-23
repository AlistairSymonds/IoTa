#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"
#include "iota_defines.h"
#include <Windows.h>
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

		TEST_METHOD(heartbeat_test) {

			IoTaDeviceHub hub(uuid);
			Heartbeat h;
			hub.addFunc(&h);
			long source = 1;
			uint8_t msg[] = { (uint8_t)FID_HEARTBEAT };

			DataCapsule *cap;
			cap = new DataCapsule(source, uuid, short(FID_HEARTBEAT), 1, msg);
			
			hub.processMessage(cap);
			delete cap;

			hub.tick();

			int capsToBeReturned = hub.numCapsulesRemaining();
			Assert::AreEqual(capsToBeReturned, 1);

			while (hub.numCapsulesRemaining() > 0)
			{
				DataCapsule *txCap;
				hub.getNextOutputCapsule(&txCap);
				Assert::AreEqual(txCap->getSource(), uuid);
				uint8_t *pckt = new uint8_t[txCap->getTcpPacketLength()];
				txCap->createTcpPacket(pckt);
				
				short tcpLen = (pckt[0] << 8 | pckt[1]);
				Assert::AreEqual((short)23, tcpLen);
			}
			
		}
			
	};
}