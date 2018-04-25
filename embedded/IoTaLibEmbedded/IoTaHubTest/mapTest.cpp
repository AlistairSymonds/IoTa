

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

			TEST_METHOD(put) {
				CrtCheckMemory chk;
				iota::Map<long, void*> m(100);
				long id = 1234;
				long value = 567;
				m.put(id, &value);
				Assert::AreEqual(1, m.contains(id));
				Assert::AreEqual(1, m.size());
			}

			TEST_METHOD(get) {
				iota::Map<long, void*> m(100);
				long id = 1234;
				long value = 567;
				m.put(id, &value);
				Assert::AreEqual(1, m.contains(id));
				Assert::AreEqual(1, m.size());

				long *foundVal = (long*) m.get(id);
				Assert::AreEqual(value, *foundVal);
				Assert::AreEqual(&value, foundVal);

			}

			TEST_METHOD(getDoesntExist) {
				iota::Map<long, void*> m(100);
				long id = 1234;
				m.put(2, &id);
				Assert::AreEqual(0, m.contains(id));
				Assert::AreEqual(1, m.size());

				void *foundVal = m.get(id);
				Assert::AreEqual((void *)NULL, foundVal);

			}
			
	};

	
}