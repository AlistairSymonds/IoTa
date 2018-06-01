#include <string.h>
#include "iota_util.h"
#include "DataCapsule.h"



DataCapsule::DataCapsule(long sourceIn, long destinationIn, short funcId, short dataSize, uint8_t * dataIn)
{
	this->destination = destinationIn;
	this->source = sourceIn;
	this->funcId = funcId;
	if (dataSize <= 255) {

		memcpy(this->data, dataIn, dataSize);
		this->dataSize = dataSize;
	}
}

//Used for transparentally generating specifically addressed capsules from broadcast capsule
DataCapsule::DataCapsule(DataCapsule *bcastCap, long newDest)
{
	
	this->destination = newDest;
	this->source = bcastCap->source;
	this->funcId = bcastCap->funcId;
	this->dataSize = bcastCap->dataSize;

	if (this->dataSize <= 256) {
		memcpy(this->data, bcastCap->data, dataSize);
		this->dataSize = dataSize;
	}

}
long DataCapsule::getSource()
{
	return source;
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

short DataCapsule::getFuncId()
{
	return this->funcId;
}

int DataCapsule::copyDataOut(uint8_t * buf)
{
	memcpy(buf, this->data, this->dataSize);
	return dataSize;
}

short DataCapsule::getDestFunc()
{
	return this->funcId;
}

int DataCapsule::updateData(int dataSize, uint8_t * dataIn)
{
	if (dataSize <= 255) {
		memcpy(this->data, dataIn, dataSize);
		this->dataSize = dataSize;
	}

	return 1;
}

short DataCapsule::getDataSize() {
	return dataSize;
}

short DataCapsule::getTcpPacketLength()
{
	return 2 + 20 + dataSize;
}

int DataCapsule::createTcpPacket(uint8_t * packet)
{
	short packetLength = getTcpPacketLength();
	typeConv::short2bytes(packetLength, packet);
	typeConv::long2bytes(this->getSource(), &packet[2]);
	typeConv::long2bytes(this->getDestination(), &packet[10]);
	typeConv::short2bytes(this->getDestFunc(), &packet[18]);
	typeConv::short2bytes(this->getDataSize(), &packet[20]);
	memcpy(&packet[22], this->data, this->getDataSize());

	return packetLength;
}


DataCapsule::~DataCapsule()
{
}
