#include <string.h>

#include "DataCapsule.h"



DataCapsule::DataCapsule(long sourceIn, long destinationIn, int dataSize, uint8_t * dataIn)
{
	this->destination = destinationIn;
	this->source = sourceIn;
	this->data = nullptr;
	if (dataSize <= 255) {
		memcpy(this->data, dataIn, dataSize);
		this->dataSize = dataSize;
	}
}
/*
DataCapsule::DataCapsule(long sourceIn, long destinationIn, DataCapsule cap) {
	this->destination = destinationIn;
	this->source = sourceIn;
	long oldSource = cap.getSource();
	long oldDest = cap.getDestination();
	uint8_t * oldData;

}
*/
long DataCapsule::getDestination()
{
	return this->destination;
}

int DataCapsule::copyData(uint8_t * buf)
{
	memcpy(this->data, buf, this->dataSize);
	return dataSize;
}

int DataCapsule::getDataSize() {
	return dataSize;
}


DataCapsule::~DataCapsule()
{
}
