#pragma once
#include "iota_util.h"
namespace typeConv {
	short bytes2short(uint8_t * bytes) {
		short val = 0;
		
		val = bytes[1] << 8 | bytes[0];
		
		return val;
	}

	void short2bytes(short val, uint8_t *outputLoc) {
		for (int i = 0; i < 2; i++) {
			outputLoc[i] = 0xFF & (val >> (i * 8));
		}
	}


	long bytes2long(uint8_t * bytes) {
		long val = 0;
		for (int i = 0; i < 8; i++)
		{
			val |= (0xFF & bytes[i]) << (8 * i);
		}
		return val;
	}



	void long2bytes(long val, uint8_t *outputLoc) {
		for (int i = 0; i < 8; i++) {
			outputLoc[i] = 0xFF & (uint8_t)(val >> (i * 8));
		}
	}
}


