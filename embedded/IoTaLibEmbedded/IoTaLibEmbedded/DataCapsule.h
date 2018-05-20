#pragma once
#include <stdint.h>
//Memory layout
// 0x00 to 0x07 = source
// 0x08 to 0x0F = destination
// 0x10 to 0x12 = id of function associated with message
// 0x13 to 0x14 = size of attached data
// 0x15 to 0x15+ize = data
//total size 8+8+2+2+size of data




class DataCapsule
{
public:
	DataCapsule(long sourceIn, long destinationIn, short funcId, short dataSize, uint8_t * dataIn);

	//copy constructor for generate specific capsules from broadcast template
	DataCapsule(DataCapsule *bcastCap, long newDest);
	//uuid of where message originated from
	long getSource();
	//uuid of physical device recipient
	long getDestination();

	//id of func associated with message
	short getFuncId();

	int copyDataOut(uint8_t *buf);
	short getDestFunc();
	int updateData(int dataSize, uint8_t * dataIn);
	short getDataSize();

	short getTcpPacketLength();
	int createTcpPacket(uint8_t* packet);
	
	~DataCapsule();
private:
	long source;
	long destination = 0;
	short funcId = 0;
	short dataSize = 0;
	uint8_t data[255];
	
};

