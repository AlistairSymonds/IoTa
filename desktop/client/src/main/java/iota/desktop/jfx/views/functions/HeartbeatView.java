package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.desktop.jfx.views.state.DefaultStateDisp;
import iota.client.model.EspDevice;
import iota.common.definitions.Heartbeat;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.IStateItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HeartbeatView extends VBox implements UpdateAbleView {
    private final EspDevice device;
    private List<DefaultStateDisp> stateDisps;
    private IFuncDef funcInstance;

    protected HeartbeatView(EspDevice deviceIn) {
        super();
        device = deviceIn;

        funcInstance = null;
        for(IFuncDef def : device.getFuncs()){
            if(def.getFuncId() == 1){
                funcInstance = def;
                break;
            }
        }


        Button hbeatBtn = new Button("Heartbeat");

        hbeatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                byte[] hbeatbytes = {0, 1,60};
                device.submitMessage( hbeatbytes );
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
