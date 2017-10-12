package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import iota.common.definitions.IFuncDef;

public class FunctionViewFactory {
    public static IFunctionView getFunctionView(EspDevice device, IFuncDef def) {

        if(def.getFuncId() == 1){
            return new HeartbeatView(device);
        }




        //fallback
        return new ErrorView(device);
    }

}

