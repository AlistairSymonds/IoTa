package iota.desktop.jfx.views.functions;

import com.sun.istack.internal.NotNull;
import iota.common.functions.Esp32Scope;
import iota.common.functions.Heartbeat;
import iota.common.functions.IFunction;
import iota.common.functions.LightingFunction.Lighting;
import javafx.scene.Node;

public class FunctionViewFactory {
    @NotNull
    public static Node getFunctionView(IFunction function) {

        if (function instanceof Heartbeat) {
            return new HeartbeatView((Heartbeat) function);
        } else if (function instanceof Lighting) {
            return new LightingView((Lighting) function);
        } else if (function instanceof Esp32Scope) {
            return new Esp32ScopeView((Esp32Scope) function);
        }

        //
        return new UnknownView(function);


    }

}

