package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import iota.common.definitions.Heartbeat;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.IStateItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HeartbeatView implements IFunctionView {
    private final EspDevice device;

    public HeartbeatView(EspDevice deviceIn) {
        device = deviceIn;
    }

    @Override
    public Pane getView() {
        VBox pane = new VBox();


        IFuncDef funcInstance = null;
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

        if(funcInstance instanceof Heartbeat){
            for (IStateItem state : funcInstance.getStateItems()){
                pane.getChildren().add(new Text("State of " + state.getName() + " is " + state.getVal()));
            }
        }


        return pane;
    }
}
