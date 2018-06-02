#ifdef ESP32
#include <Arduino.h>
#include <SPI.h>
#include <driver\adc.h>
#include <esp_attr.h>
#include <freertos\portmacro.h>
#include "iota_util.h"
#include "Esp32ScopeFunc.h"
#include "iota_defines.h"
#include "samplerStateEnum.h"
#include "circularBuffer.h"

DRAM_ATTR uint16_t _scopeState[6];
DRAM_ATTR short sampleMem[SAMPLE_MEMORY_LENGTH];
DRAM_ATTR int sampleIndex = 0;
DRAM_ATTR int sentTrigMsg = 1;
DRAM_ATTR uint DataRecAfterTrig;
DRAM_ATTR uint DataRecBeforeTrig;
DRAM_ATTR short val = 0;

portMUX_TYPE timerMux = portMUX_INITIALIZER_UNLOCKED;
void IRAM_ATTR sampleFunc();

Esp32ScopeFunc::Esp32ScopeFunc()
{
	_scopeState[GAIN_VAL] = 1;
	_scopeState[TIMER_PRESCALER] = 1000;
	_scopeState[TIMER_ALARM] = 1000;
	_scopeState[IS_TRIGGERED] = 0;
	_scopeState[TRIGGER_LEVEL] = 1000;
	_scopeState[TRIGGER_INDEX] = 0;


	
	
	for (int i = 0; i < SAMPLE_MEMORY_LENGTH; i++) {
		sampleMem[i] = i;
	}

	responseLocs = new CircularBuffer<long>(10);
	SPIClass *gainSpi;

	gainSpi = new SPIClass(VSPI);
	
	gain = new Pga112SpiController(gainSpi, SPISettings(10000, MSBFIRST, SPI_MODE0));
	
	
	pinMode(34, INPUT);
	
	adc1_config_width(ADC_WIDTH_12Bit);
	adc1_config_channel_atten(ADC1_CHANNEL_6, ADC_ATTEN_11db);
	


	
	
}

void Esp32ScopeFunc::init() {
	gain->init();
	gain->setGain(1);
	//init sample timer with period of 80Mhz/ rate base (80KHz)
	sampleTimer = timerBegin(0, _scopeState[TIMER_PRESCALER], true);
	//alarm every 1000 periods (800Hz sample rate)
	timerAlarmWrite(sampleTimer, _scopeState[TIMER_ALARM], true);
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
				_scopeState[GAIN_VAL] = typeConv::bytes2short(&dataIn[1]);
			}
			
		}
		break;
		case TIMER_PRESCALER:
		{
			_scopeState[TIMER_PRESCALER] = typeConv::bytes2short(&dataIn[1]);
			
			timerSetDivider(sampleTimer, _scopeState[TIMER_PRESCALER]);
		}
		break;
		case TIMER_ALARM:
		{
			_scopeState[TIMER_ALARM] = typeConv::bytes2short(&dataIn[1]);
			timerAlarmWrite(sampleTimer, _scopeState[TIMER_ALARM], true);
		}
		break;
		case IS_TRIGGERED:
		{
			_scopeState[IS_TRIGGERED] = 0;
			DataRecBeforeTrig = 0;
			DataRecAfterTrig = 0;

		}
		break;
		case TRIGGER_LEVEL:
		{
			_scopeState[TRIGGER_LEVEL] = typeConv::bytes2short(&dataIn[1]);
		}
		break;

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
	if (sentTrigMsg == 0) {
		responseLocs->add(0);
		sentTrigMsg = 1;
	}
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
		return sizeof(_scopeState);
	}
	else
	{
		return 1+(WORDS_PER_SECTION*SAMPLE_WORD_LENGTH);
	}
	
}

int Esp32ScopeFunc::getStateBuffer(uint8_t * buffer)
{
	if (clienRequesting == 0) {
		memcpy(buffer, _scopeState, getStateBufLen());
	}
	else
	{
		buffer[0] = nextSectionRequested;
		memcpy(&buffer[1], &sampleMem[(WORDS_PER_SECTION*nextSectionRequested)], WORDS_PER_SECTION*SAMPLE_WORD_LENGTH);
		
		clienRequesting = 0;
	}
	
	return 1;
}



Esp32ScopeFunc::~Esp32ScopeFunc()
{
	delete responseLocs;
	delete gain;
}
#endif // ESP32



void IRAM_ATTR sampleFunc()
{
	portENTER_CRITICAL(&timerMux);

	val = analogRead(34);
	//val = adc1_get_raw(ADC1_CHANNEL_6);

	if (_scopeState[IS_TRIGGERED] == 0) {
		sampleMem[sampleIndex] = val;
		sampleIndex++;
		DataRecBeforeTrig++;
	}

	if (_scopeState[IS_TRIGGERED] == 1 && DataRecAfterTrig < SAMPLE_MEMORY_LENGTH / 2) {
		sampleMem[sampleIndex] = val;
		sampleIndex++;
		DataRecAfterTrig++;
	}

	

	if (sampleMem[sampleIndex] > _scopeState[TRIGGER_LEVEL] && _scopeState[IS_TRIGGERED] == 0 && DataRecBeforeTrig > SAMPLE_MEMORY_LENGTH/2) {
		_scopeState[TRIGGER_INDEX] = sampleIndex - 1;
		_scopeState[IS_TRIGGERED] = 1;
		DataRecAfterTrig = 0;
		sentTrigMsg = 0;
		Serial.println("TRIG");
	}
	

	if (sampleIndex >= SAMPLE_MEMORY_LENGTH) {
		sampleIndex = 0;
	}

	portEXIT_CRITICAL(&timerMux);
}
