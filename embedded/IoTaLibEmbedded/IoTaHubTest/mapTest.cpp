

#include "CppUnitTest.h"
#include "IoTaDeviceHub.h"
#include "heartbeat.h"
#include "MemCheck.h"
#include "Map.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{	
	TEST_CLASS(MapTest)
	{
		public:
			
			TEST_METHOD(constructor) {
				CrtCheckMemory chk;
				iota::Map<long, void*> m(100);
				
			}
			
	};

	
}