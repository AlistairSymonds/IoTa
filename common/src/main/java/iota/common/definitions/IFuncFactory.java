package iota.common.definitions;

import iota.common.Constants;

public class IFuncFactory {
    public static IFuncDef getInstanceById(short funcId){


        if (funcId == Constants.FID_HEARTBEAT) {
            return new Heartbeat();
        } else if (funcId == Constants.FID_LEDS) {
            return new Lighting();
        }

        return new UnknownFunction(funcId);
    }
}
