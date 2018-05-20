
#include <stdio.h>
#include <stdlib.h>
#include "CppUnitTest.h"
#include "iota_util.h"

using namespace Microsoft::VisualStudio::CppUnitTestFramework;

namespace IoTaHubTest
{
	TEST_CLASS(dataConversionTest)
	{
	public:

		TEST_METHOD(short2bytesTest) {
			short testVal = 1;
			uint8_t dest[2];
			typeConv::short2bytes(testVal, dest);
			Assert::AreEqual((uint8_t)1, dest[0]);
			Assert::AreEqual((uint8_t)0, dest[1]);
		}

		TEST_METHOD(backAndForthTest) {
			short testVal = 0x0FAB;
			uint8_t intermmediary[2];
			typeConv::short2bytes(testVal, intermmediary);
			Assert::AreEqual((uint8_t)0x0F,intermmediary[1]);
			Assert::AreEqual((uint8_t)0xAB, intermmediary[0]);
			Assert::AreEqual(testVal, typeConv::bytes2short(intermmediary));

		}

		TEST_METHOD(bytes2shorttest) {
			uint8_t bytes[2];
			bytes[0] = 2;
			bytes[1] = 0;
			Assert::AreEqual((short)2, typeConv::bytes2short(bytes));
		}

		TEST_METHOD(fiftyRandomLongs) {
			for (int i = 0; i < 50; i++) {
				long val = llrand();
				uint8_t bytes[8];
				typeConv::long2bytes(val, bytes);
				Assert::AreEqual(typeConv::bytes2long(bytes), val);

			}
		}

		unsigned long llrand() {
			unsigned long r = 0;

			for (int i = 0; i < 5; ++i) {
				r = (r << 15) | (rand() & 0x7FFF);
			}

			return r & 0xFFFFFFFFFFFFFFFFUL;
		}



	};


}