#ifdef ESP32
#include <Arduino.h>
#include "iota_util.h"
#include "Esp32ScopeFunc.h"
#include "iota_defines.h"
#include "samplerStateEnum.h"
#include "circularBuffer.h"

short *sampleMem;
int sampleIndex = 0;

Esp32ScopeFunc::Esp32ScopeFunc()
{
	//originally done via static global allocation, however this lead to corruption :(
	sampleMem =(short *) malloc(SAMPLE_MEMORY_LENGTH*SAMPLE_WORD_LENGTH);
	int t1 = micros();
	state[SAMPLE_RATE_DIVISION] = 1000;
	for (int i = 0; i < SAMPLE_MEMORY_LENGTH; i++) {
		sampleMem[i] = i;
	}
	int t2 = micros();
	Serial.print("Sin fill took ");
	Serial.println(t2 - t1);

	responseLocs = new CircularBuffer<long>(10);

	gain = new Pga112SpiController(new SPIClass(VSPI), SPISettings(10000, MSBFIRST, SPI_MODE0));


	pinMode(LED_BUILTIN, OUTPUT);
	pinMode(34, INPUT);
	sampleTimer = timerBegin(0, 1000000, true);
	timerAlarmWrite(sampleTimer, state[SAMPLE_RATE_DIVISION], true);
	timerAlarmEnable(sampleTimer);
	timerAttachInterrupt(sampleTimer, &sampleFunc, true);
	
}



short Esp32ScopeFunc::getFuncId()
{
	return FID_32SCOPE;
}

void Esp32ScopeFunc::processCommand(DataCapsule *capsule)
{
	uint8_t * dataIn;
	dataIn = new uint8_t[capsule->getDataSize()];

	//requesting to update state
	if (capsule->getDataSize() != 0 && capsule->getDataSize() == 3) {


		switch (dataIn[0])
		{
		case GAIN_VAL:
		{
			int gainValid = gain->setGain(typeConv::bytes2short(&dataIn[1]));
			if (gainValid == 0) {
				state[GAIN_VAL] = typeConv::bytes2short(&dataIn[1]);
			}
			break;
		}
		default:
		break;
		}

		

		responseLocs->add(capsule->getSource());
	}

	//requesting part of sample memory
	else if(capsule->getDataSize() == 1){
		uint8_t val = 0;
		capsule->copyDataOut(&val);
		nextSectionRequested = val;
		clienRequesting = capsule->getSource();
		Serial.print("requested section ");
		Serial.print(val);
		Serial.print(" at adress ");
		Serial.println((int)&sampleMem[val*SAMPLE_WORD_LENGTH]);

	}
	//requesting update on current state
	else if(capsule->getDataSize() == 0)
	{
		responseLocs->add(capsule->getSource());
	}
}

void Esp32ScopeFunc::tick()
{

}

int Esp32ScopeFunc::getReponsesRemaining()
{
	
	int x1 = responseLocs->available();
	if (clienRequesting != 0) {
		x1++;
	}
	
	return x1;
}

long Esp32ScopeFunc::getNextMsgDest()
{
	if (clienRequesting == 0) {
		if (responseLocs->available() > 0) {
			return responseLocs->read();
		}
	}
	else
	{
		return clienRequesting;
	}
}



int Esp32ScopeFunc::getStateBufLen()
{
	if (clienRequesting == 0) {
		return sizeof(state);
	}
	else
	{
		return 1+(WORDS_PER_SECTION*SAMPLE_WORD_LENGTH);
	}
	
}

int Esp32ScopeFunc::getStateBuffer(uint8_t * buffer)
{
	if (clienRequesting == 0) {
		memcpy(buffer, state, getStateBufLen());
	}
	else
	{
		Serial.print("Packing up section ");
		Serial.println(nextSectionRequested);
		buffer[0] = nextSectionRequested;
		memcpy(&buffer[1], &sampleMem[(WORDS_PER_SECTION*nextSectionRequested)], WORDS_PER_SECTION*SAMPLE_WORD_LENGTH);
		
		clienRequesting = 0;
	}
	
	return 1;
}



Esp32ScopeFunc::~Esp32ScopeFunc()
{
	free(sampleMem);
	delete responseLocs;
	delete gain;
}
#endif // ESP32



void IRAM_ATTR sampleFunc()
{
	short val = analogRead(34);
	sampleMem[sampleIndex] = 0;
	sampleIndex++;
	if (sampleIndex >= SAMPLE_MEMORY_LENGTH-20) {
		sampleIndex = 0;
	}
}
