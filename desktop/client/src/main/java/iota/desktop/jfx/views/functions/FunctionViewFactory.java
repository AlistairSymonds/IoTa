package iota.desktop.jfx.views.functions;

import iota.common.functions.*;
import javafx.scene.Node;

public class FunctionViewFactory {
    public static Node getFunctionView(IFunction function) {

        if (function instanceof Heartbeat) {
            return new HeartbeatView((Heartbeat) function);
        } else if (function instanceof LightingView) {
            return new LightingView((Lighting) function);
        } else if (function instanceof Esp32Scope) {
            return new Esp32ScopeView((Esp32Scope) function);
        }

        //
        if (function instanceof UnknownFunction) {
            return new UnknownView((UnknownFunction) function);
        }


        return null;
    }

}

