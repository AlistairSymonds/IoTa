package iota.common.functions;

import iota.client.model.EspDevice;
import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.IStateItem;

import java.util.ArrayList;
import java.util.List;

public class Esp32Scope implements IFunction {
    private static final int WORDS_PER_SECTION = 120;
    private static final int SAMPLE_WORD_LENGTH = 2;
    private static final int SAMPLE_MEMORY_LENGTH = 4080;

    private SampleMemory samples;

    private EspDevice device;
    private short[] states;

    public Esp32Scope(EspDevice device) {
        this.device = device;
        samples = new SampleMemory();
        states = new short[stateEnum.values().length];
        for (int i = 0; i < states.length; i++) {
            states[i] = 1;
        }

        DataCapsule getState = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), new ArrayList<Byte>());
        device.submitMessage(getState);


    }


    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public short getFuncId() {
        return Constants.FID_32SCOPE;
    }


    @Override
    public int handleReceivedData(List<Byte> receivedData) {
        if (receivedData.size() == states.length * 2) {
            for (int i = 0; i < states.length; i++) {
                byte[] v = new byte[2];
                v[0] = receivedData.get(2 * i);
                v[1] = receivedData.get((2 * i) + 1);
                states[i] = (IoTaUtil.bytes2Short(v));
                System.out.println(stateEnum.values()[i].toString() + " is now " + IoTaUtil.bytes2Short(v));
            }
        } else if (receivedData.size() == ((WORDS_PER_SECTION * SAMPLE_WORD_LENGTH) + 1)) {

            samples.takeInNewData(receivedData);
        }
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return new ArrayList<IStateItem>();
    }



    public List<Double> getScopeData() {
        short data[] = samples.getSampleMemory();
        short loopedData[] = new short[data.length * 2];
        System.arraycopy(data, 0, loopedData, 0, data.length);
        System.arraycopy(data, 0, loopedData, data.length, data.length);



        //data is in range 0 - 3.3V in 4096 steps
        //so each step is

        //first read pre trigger stuff

        int trigIdx = states[stateEnum.TRIGGERED_SECTION.ordinal()];
        int startIdx = 0;

        if (trigIdx < SAMPLE_MEMORY_LENGTH / 2) {
            startIdx = trigIdx + (SAMPLE_MEMORY_LENGTH / 2);
        } else {
            startIdx = trigIdx - (SAMPLE_MEMORY_LENGTH / 2);
        }

        ArrayList<Double> processedSamples = new ArrayList<>();
        double voltsPerStep = 3.3 / 4096;

        for (int i = startIdx; i < data.length; i++) {
            processedSamples.add(loopedData[i] * voltsPerStep);
        }

        return processedSamples;
    }

    public void updateScopeData() {
        Thread sampleDataUpdateWorker = new Thread(samples);
        sampleDataUpdateWorker.start();
    }

    public int getGain() {
        return this.states[stateEnum.GAIN_VAL.ordinal()];
    }


    public void setGain(short gain) {
        ArrayList<Byte> msg = new ArrayList<>();
        msg.add((byte) stateEnum.GAIN_VAL.ordinal());
        byte[] newVal = IoTaUtil.short2Bytes(gain);
        msg.add(newVal[0]);
        msg.add(newVal[1]);
        DataCapsule newGain = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), msg);
        device.submitMessage(newGain);
    }

    public double getTs() {

        return (1 / 80000000.0) * states[stateEnum.TIMER_PRESCALER.ordinal()] * states[stateEnum.TIMER_ALARM.ordinal()];
    }


    public double setTs(double ts) {
        double t_clk = 1 / 80000000.0;
        //aim to have 100 clks into the sample divider, thus
        //fastest sample rate is

        //t_timer should be 100 smaller than t_s
        double t_timer = ts / 100;
        double clockScaler = t_timer / t_clk;

        double alarmLevel = ts / t_timer;

        short clockScalerDisc = (short) Math.round(clockScaler);
        short alarmLevelDisc = (short) Math.round(alarmLevel);
        {
            byte[] clockScalerBytesArr = IoTaUtil.short2Bytes(clockScalerDisc);
            ArrayList<Byte> scalerBytes = new ArrayList<>();
            scalerBytes.add((byte) stateEnum.TIMER_PRESCALER.ordinal());
            scalerBytes.add(clockScalerBytesArr[0]);
            scalerBytes.add(clockScalerBytesArr[1]);
            DataCapsule scalerCap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), scalerBytes);
            device.submitMessage(scalerCap);
        }

        {

            byte[] alarmBytesArr = IoTaUtil.short2Bytes(alarmLevelDisc);
            ArrayList<Byte> alarmBytes = new ArrayList<>();
            alarmBytes.add((byte) stateEnum.TIMER_ALARM.ordinal());
            alarmBytes.add(alarmBytesArr[0]);
            alarmBytes.add(alarmBytesArr[1]);
            DataCapsule alarmCap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), alarmBytes);
            device.submitMessage(alarmCap);
        }


        return (t_clk * clockScalerDisc) * alarmLevelDisc;


    }

    public boolean isTriggered() {
        return states[stateEnum.IS_TRIGGERED.ordinal()] != 0;
    }

    public void clearTrigger() {
        ArrayList<Byte> clearTrigMsg = new ArrayList<>();
        clearTrigMsg.add((byte) stateEnum.IS_TRIGGERED.ordinal());
        clearTrigMsg.add((byte) 0);
        clearTrigMsg.add((byte) 0);
        DataCapsule clearTrigCap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), clearTrigMsg);
        device.submitMessage(clearTrigCap);
    }

    public double getTriggerLevel() {
        return states[stateEnum.TRIGGER_LEVEL.ordinal()] * (3.3 / 4096);
    }

    public void setTriggerLevel(double level) {
        short levelDisc = (short) (Math.round(level * (4096 / 3.3)));
        ArrayList<Byte> setTrigMsg = new ArrayList<>();
        setTrigMsg.add((byte) stateEnum.TRIGGER_LEVEL.ordinal());
        byte[] val = IoTaUtil.short2Bytes(levelDisc);
        setTrigMsg.add(val[0]);
        setTrigMsg.add(val[1]);
        device.submitMessage(new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), setTrigMsg));
    }

    private enum stateEnum {
        TIMER_PRESCALER,
        TIMER_ALARM,
        GAIN_VAL,
        TRIGGER_LEVEL,
        IS_TRIGGERED,
        TRIGGERED_SECTION
    }

    private class SampleMemory implements Runnable {
        Object staleLock = new Object();
        private short data[] = new short[SAMPLE_MEMORY_LENGTH];
        private boolean isSectionStale[];


        public SampleMemory() {
            isSectionStale = new boolean[SAMPLE_MEMORY_LENGTH / WORDS_PER_SECTION];
            for (int i = 0; i < data.length; i++) {
                data[i] = (short) i;
            }
        }


        private void markAllSectionsStale() {
            for (int i = 0; i < isSectionStale.length; i++) {
                markSectionStale(i, true);
            }
        }

        private synchronized void markSectionStale(int sectionIndex, boolean val) {
            synchronized (staleLock) {
                isSectionStale[sectionIndex] = val;
            }
        }

        private synchronized boolean isSectionStale(int sectionIndex) {
            synchronized (staleLock) {
                return isSectionStale[sectionIndex];
            }
        }

        public short[] getSampleMemory() {
            return data;
        }

        @Override
        public void run() {
            long tstart = System.nanoTime();
            markAllSectionsStale();
            for (int i = 0; i < isSectionStale.length; i++) {
                //EVERYTHING ABOUT THIS IS BAD
                //TODO: start over on the function interface
                ArrayList<Byte> list = new ArrayList<Byte>();
                list.add((byte) i);
                DataCapsule cap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), getFuncId(), list);
                device.submitMessage(cap);
                while (isSectionStale(i)) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            long tFinish = System.nanoTime();
            System.out.println("Updated Sample memory in " + (tFinish - tstart) / 1000000 + " ms");
        }

        public void takeInNewData(List<Byte> receivedData) {

            if (receivedData.size() != WORDS_PER_SECTION * SAMPLE_WORD_LENGTH + 1) {
                throw new IllegalArgumentException("Data len was " + receivedData.size());
            }

            byte[] sectionBytes = new byte[2];
            sectionBytes[0] = receivedData.get(0);
            sectionBytes[1] = 0;
            int sectionIndex = IoTaUtil.bytes2Short(sectionBytes);
            System.out.println("Updating section " + sectionIndex + "bytes: " + receivedData.get(0));
            short[] section = new short[WORDS_PER_SECTION];

            for (int i = 1; i < receivedData.size() - 1; i = i + 1) {
                byte[] b = new byte[2];
                b[1] = receivedData.get(i + 1);
                b[0] = receivedData.get(i);
                section[i / 2] = IoTaUtil.bytes2Short(b);
            }


            for (int i = 0; i < WORDS_PER_SECTION; i++) {
                try {
                    this.data[i + (WORDS_PER_SECTION * sectionIndex)] = section[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(i + " " + sectionIndex);
                    e.printStackTrace();
                    System.exit(1);
                }

            }
            markSectionStale(sectionIndex, false);
        }
    }


}
