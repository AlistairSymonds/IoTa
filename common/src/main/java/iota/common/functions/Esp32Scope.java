package iota.common.functions;

import iota.client.model.EspDevice;
import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.db.DbCol;
import iota.common.definitions.IStateItem;

import java.util.ArrayList;
import java.util.List;

public class Esp32Scope implements IFunction {
    private static final int WORDS_PER_SECTION = 120;
    private static final int SAMPLE_WORD_LENGTH = 2;
    private static final int SAMPLE_MEMORY_LENGTH = 4096;

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


    }

    @Override
    public List<DbCol> getCols() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public short getFuncId() {
        return Constants.FID_32SCOPE;
    }

    @Override
    public int submitMessage(byte[] message) {
        return 0;
    }

    @Override
    public int handleReceivedData(List<Byte> receivedData) {
        if (receivedData.size() == states.length * 2) {
            for (int i = 0; i < states.length; i++) {
                byte[] v = new byte[2];
                v[0] = receivedData.get(2 * i);
                v[1] = receivedData.get((2 * i) + 1);
                states[i] = (IoTaUtil.bytes2Short(v));
            }
        } else if (receivedData.size() == (WORDS_PER_SECTION * SAMPLE_WORD_LENGTH) + 1) {

            samples.takeInNewData(receivedData);
        }
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return new ArrayList<IStateItem>();
    }

    public double getT_s() {
        double basePeriod = states[stateEnum.SAMPLE_RATE_BASE.ordinal()] / 80.0;
        double ts = basePeriod / states[stateEnum.SAMPLE_RATE_DIVISION.ordinal()];
        return ts;
    }

    public List<Double> getScopeData() {
        short data[] = samples.getSampleMemory();
        ArrayList<Double> processedSamples = new ArrayList<>();

        //data is in range 0 - 3.3V in 4096 steps
        //so each step is
        double voltsPerStep = 3.3 / 4096;
        for (int i = 0; i < data.length; i++) {
            processedSamples.add(Integer.toUnsignedLong(data[i]) * voltsPerStep);
        }

        return processedSamples;
    }

    public void updateScopeData() {
        Thread sampleDataUpdateWorker = new Thread(samples);
        sampleDataUpdateWorker.start();
    }


    private enum stateEnum {
        SAMPLE_RATE_DIVISION,
        SAMPLE_RATE_BASE,
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
