#include "CppUnitTest.h"
#include "fixedSet.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;
namespace IoTaHubTest
{
	TEST_CLASS(byteBufTest)
	{
		TEST_METHOD(addAndContains) {
			fixedSet<int> fh(5);
			fh.add(1);
			Assert::AreEqual(1,fh.contains(1));
		}

		TEST_METHOD(addAndRemove) {
			fixedSet<int> fh(5);
			fh.add(1);
			Assert::AreEqual(1, fh.contains(1));
			fh.remove(1);
			Assert::AreEqual(0, fh.contains(1));
		}
	};
}