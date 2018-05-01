package iota.desktop.jfx.views.functions;

import iota.client.model.EspDevice;
import iota.common.functions.IFunction;
import javafx.scene.Node;

public class FunctionViewFactory {
    public static Node getFunctionView(EspDevice device, IFunction def) {

        if(def.getFuncId() == 1){
            return new HeartbeatView(device);
        } else if (def.getFuncId() == 2) {
            return new LightingView(device);
        }




        //fallback
        return new ErrorView(device);
    }

}

