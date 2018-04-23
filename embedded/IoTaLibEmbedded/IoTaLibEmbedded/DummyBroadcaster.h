#pragma once
#include "IoTaFuncBase.h"
class DummyBroadcaster :
	public IoTaFuncBase
{
public:
	DummyBroadcaster();

	~DummyBroadcaster();

	// Inherited via IoTaFuncBase
	virtual short getFuncId() override;
	virtual void processCommand(DataCapsule * capsule) override;
	virtual void tick() override;
	virtual int getReponsesRemaining() override;
	virtual long getNextMsgDest() override;
	virtual int getStateBufLen() override;
	virtual int getStateBuffer(uint8_t * buf) override;

private:
	int ticked = 0;
	int bufferSize = 15;
	uint8_t state[15];
};

