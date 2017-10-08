#include "CppUnitTest.h"
#include "circularBuffer.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(byteBufTest)
	{
		TEST_METHOD(insertAndRead) {
			CircularBuffer<int> cbb(5);
			cbb.add(1);
			cbb.add(2);
			Assert::AreEqual(1, cbb.read());
			Assert::AreEqual(2, cbb.read());
		}

		TEST_METHOD(testAvailableCount) {
			CircularBuffer<int> cbb(10);
			Assert::AreEqual(0, cbb.available());
			cbb.add(4);
			Assert::AreEqual(1, cbb.available());
			Assert::AreEqual(4, cbb.read());
			Assert::AreEqual(0, cbb.available());
		}

		TEST_METHOD(overfillAndRead) {
			CircularBuffer<int> cbb(5);
			for (int i = 0; i < 10; i++) {
				cbb.add(i);
			}
			Assert::AreEqual(5, cbb.available());
			int k = 5;
			while (cbb.available() > 0)
			{
				Assert::AreEqual(k, cbb.read());
				k++;
			}
		}
	};
}