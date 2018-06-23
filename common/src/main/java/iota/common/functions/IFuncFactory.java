package iota.common.functions;

import iota.client.model.EspDevice;
import iota.common.Constants;
import iota.common.functions.LightingFunction.Lighting;

public class IFuncFactory {
    public static IFunction getInstanceById(EspDevice device, short funcId) {


        if (funcId == Constants.FID_HEARTBEAT) {
            return new Heartbeat(device);
        } else if (funcId == Constants.FID_LEDS) {
            return new Lighting(device);
        } else if (funcId == Constants.FID_HUB) {
            return new HubInternal(device);
        } else if (funcId == Constants.FID_32SCOPE) {
            return new Esp32Scope(device);
        }

        return new UnknownFunction(device, funcId);
    }
}
