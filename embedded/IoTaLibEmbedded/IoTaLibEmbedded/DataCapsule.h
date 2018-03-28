#pragma once
#include <stdint.h>

class DataCapsule
{
public:
	DataCapsule(long sourceIn, long destinationIn, int dataSize, uint8_t * dataIn);
	DataCapsule(long sourceIn, long destinationIn, DataCapsule toEncapsulate);
	long getSource();
	long getDestination();
	int copyData(uint8_t *buf);

	int getDataSize();

	
	~DataCapsule();
private:
	long source;
	long destination = 0;
	uint8_t* data;
	int dataSize = 0;
};

