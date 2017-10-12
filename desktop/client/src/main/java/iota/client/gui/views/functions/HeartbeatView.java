package iota.client.gui.views.functions;

import iota.client.gui.views.DefaultStateDisp;
import iota.client.model.EspDevice;
import iota.common.definitions.Heartbeat;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.IStateItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HeartbeatView implements IFunctionView {
    private final EspDevice device;
    private List<DefaultStateDisp> stateDisps;
    private IFuncDef funcInstance;

    protected HeartbeatView(EspDevice deviceIn) {
        device = deviceIn;
    }


    @Override
    public Pane getView() {
        VBox pane = new VBox();


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

        pane.getChildren().add(hbeatBtn);
        stateDisps = new ArrayList<>();

        if(funcInstance instanceof Heartbeat){
            for (IStateItem state : funcInstance.getStateItems()){
                stateDisps.add(new DefaultStateDisp(state));
            }
        }
        pane.getChildren().addAll(stateDisps);

        return pane;
    }

    @Override
    public void updateView() {

        for (DefaultStateDisp s : stateDisps) {
            s.updateView();
        }
    }
}
