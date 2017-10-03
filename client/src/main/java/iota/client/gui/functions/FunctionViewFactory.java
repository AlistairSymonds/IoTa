package iota.client.gui.functions;

import iota.client.model.EspDevice;
import iota.common.definitions.IFuncDef;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class FunctionViewFactory {
    public static List<IFunctionView> getFuncViews(EspDevice device) {
        List<IFunctionView> viewList = new ArrayList<>();
        //for
        return viewList;
    }

    private static Pane getPaneForFunc(EspDevice device, IFuncDef funcDef) {
        if (funcDef.getTableName().equals("lighting")) {
            return new Lighting(device).getView();
        }

        return new ErrorView(device).getView();
    }
}

