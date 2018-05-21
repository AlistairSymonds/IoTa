package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.client.model.EspDevice;
import iota.client.network.DataCapsule;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.IStateItem;
import iota.common.functions.Heartbeat;
import iota.common.functions.IFunction;
import iota.desktop.jfx.views.state.DefaultStateDisp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HeartbeatView extends VBox implements UpdateAbleView {
    private final EspDevice device;
    private List<DefaultStateDisp> stateDisps;
    private IFunction funcInstance;

    protected HeartbeatView(EspDevice deviceIn) {
        super();
        device = deviceIn;

        funcInstance = null;
        for (IFunction def : device.getFuncs()) {
            if(def.getFuncId() == 1){
                funcInstance = def;
                break;
            }
        }


        Button hbeatBtn = new Button("Heartbeat");

        hbeatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ArrayList<Byte> heartbeatBytes = new ArrayList<>();
                heartbeatBytes.add((byte) Constants.FID_HEARTBEAT);
                device.submitMessage(new DataCapsule(IoTaUtil.getHostId(), device.getId(), funcInstance.getFuncId(), heartbeatBytes));
                funcInstance.submitMessage(new byte[1]);
            }
        });

        super.getChildren().add(hbeatBtn);
        stateDisps = new ArrayList<>();

        if(funcInstance instanceof Heartbeat){
            for (IStateItem state : funcInstance.getStateItems()){
                stateDisps.add(new DefaultStateDisp(state));
            }
        }
        super.getChildren().addAll(stateDisps);
        updateView();

    }

    @Override
    public void updateView() {
        for (DefaultStateDisp s : stateDisps) {
            s.updateView();
        }
    }
}
