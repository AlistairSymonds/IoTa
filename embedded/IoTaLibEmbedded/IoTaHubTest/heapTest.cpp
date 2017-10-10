#include "CppUnitTest.h"
#include "fixedHeap.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;
namespace IoTaHubTest
{
	TEST_CLASS(byteBufTest)
	{
		TEST_METHOD(addAndContains) {
			fixedHeap<int> fh(5);
			fh.add(1);
			Assert::AreEqual(1,fh.contains(1));
		}

		TEST_METHOD(addAndRemove) {
			fixedHeap<int> fh(5);
			fh.add(1);
			Assert::AreEqual(1, fh.contains(1));
			fh.remove(1);
			Assert::AreEqual(0, fh.contains(1));
		}
	};
}