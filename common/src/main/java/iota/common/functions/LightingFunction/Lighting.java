package iota.common.functions.LightingFunction;

import iota.client.model.EspDevice;
import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.IStateItem;
import iota.common.functions.IFunction;

import java.util.ArrayList;
import java.util.List;

public class Lighting implements IFunction {
    private byte[] stateBuffer;
    private EspDevice device;

    private AnimationList currentAnimation;

    public Lighting(EspDevice device) {
        this.device = device;
        stateBuffer = new byte[LightingStateEnum.values().length];
        currentAnimation = AnimationList.Glitter;
    }

    @Override
    public String getDisplayName() {
        return "LightingFunction";
    }

    @Override
    public short getFuncId() {
        return Constants.FID_LEDS;
    }


    @Override
    public int handleReceivedData(List<Byte> receivedData) {
        if (receivedData.size() == 2) {
            if (receivedData.get(0) == LightingStateEnum.PID.ordinal()) {
                currentAnimation = AnimationList.values()[receivedData.get(0)];
            }
            stateBuffer[receivedData.get(0)] = receivedData.get(1);
        }
        return 0;
    }

    @Override
    public List<IStateItem> getStateItems() {
        return new ArrayList<>();
    }


    public byte getState(LightingStateEnum val) {
        return stateBuffer[val.ordinal()];
    }

    public void setState(LightingStateEnum selectedState, byte newVal) {
        ArrayList<Byte> msgBytes = new ArrayList<>();
        msgBytes.add((byte) selectedState.ordinal());
        msgBytes.add(newVal);

        DataCapsule cap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), msgBytes);
        device.submitMessage(cap);
    }

    public AnimationList[] getAnimationList() {
        return AnimationList.values();
    }

    public AnimationList getCurrentAnimation() {
        return currentAnimation;
    }

    public void setAnimation(AnimationList anim) {
        ArrayList<Byte> msgBytes = new ArrayList<>();
        msgBytes.add((byte) LightingStateEnum.PID.ordinal());
        msgBytes.add((byte) anim.ordinal());

        DataCapsule cap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), this.getFuncId(), msgBytes);
        device.submitMessage(cap);
    }

}
