#pragma once
#include <stdint.h>
namespace typeConv {
	short bytes2short(uint8_t * bytes);

	void short2bytes(short val, uint8_t *outputLoc);


	long bytes2long(uint8_t * bytes);



	void long2bytes(long val, uint8_t *outputLoc);
}


