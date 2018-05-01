package iota.common.functions;

import iota.common.Constants;

public class IFuncFactory {
    public static IFunction getInstanceById(short funcId) {


        if (funcId == Constants.FID_HEARTBEAT) {
            return new Heartbeat();
        } else if (funcId == Constants.FID_LEDS) {
            return new Lighting();
        } else if (funcId == Constants.FID_HUB) {
            return new HubInternal();
        }

        return new UnknownFunction(funcId);
    }
}
