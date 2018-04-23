#include "DummyBroadcaster.h"



DummyBroadcaster::DummyBroadcaster()
{
}


DummyBroadcaster::~DummyBroadcaster()
{
}

short DummyBroadcaster::getFuncId()
{
	return 0;
}

void DummyBroadcaster::processCommand(DataCapsule * capsule)
{
}

void DummyBroadcaster::tick()
{
}

int DummyBroadcaster::getReponsesRemaining()
{
	return 0;
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
	return 0;
}
