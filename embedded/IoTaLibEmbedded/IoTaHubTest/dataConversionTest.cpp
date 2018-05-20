
#include <stdio.h>
#include "CppUnitTest.h"
#include "iota_util.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(dataConversionTest)
	{
	public:

		TEST_METHOD(short2bytesTest) {
			short testVal = 255;
			uint8_t dest[2];
			typeConv::short2bytes(testVal, dest);
			Assert::AreEqual((uint8_t)255, dest[0]);
			Assert::AreEqual((uint8_t)0, dest[1]);
		}

		TEST_METHOD(backAndForthTest) {
			short testVal = 0x0FAB;
			uint8_t intermmediary[2];
			typeConv::short2bytes(testVal, intermmediary);
			Assert::AreEqual(testVal, typeConv::bytes2short(intermmediary));

		}

		TEST_METHOD(bytes2shorttest) {
			uint8_t bytes[2];
			bytes[0] = 2;
			bytes[1] = 0;
			Assert::AreEqual((short)2, typeConv::bytes2short(bytes));
		}



	};


}