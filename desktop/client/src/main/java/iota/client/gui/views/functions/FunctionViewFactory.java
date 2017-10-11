package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.UnknownFunction;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class FunctionViewFactory {
    public static Pane getFunctionView(EspDevice device, IFuncDef def){

        if(def.getFuncId() == 1){
            return new HeartbeatView(device).getView();
        }




        //fallback
        return new ErrorView(device).getView();
    }

}

