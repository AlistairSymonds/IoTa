#include <string>
#include "iota_defines.h"
#include "DummyBroadcaster.h"



DummyBroadcaster::DummyBroadcaster()
{
	for (int i = 0; i < bufferSize; i++) {
		state[i] = i + 10;
	}
}


DummyBroadcaster::~DummyBroadcaster()
{
}

short DummyBroadcaster::getFuncId()
{
	return FID_BROADCAST_DUMMY;
}

void DummyBroadcaster::processCommand(DataCapsule * capsule)
{
}

void DummyBroadcaster::tick()
{
	ticked = 1;
}

int DummyBroadcaster::getReponsesRemaining()
{
	return ticked;
}

long DummyBroadcaster::getNextMsgDest()
{
	return 0;
}

int DummyBroadcaster::getStateBufLen()
{
	return 0;
}

int DummyBroadcaster::getStateBuffer(uint8_t * buf)
{
	memcpy(buf, this->state, bufferSize);
	ticked = 0;
	return 0;
}
