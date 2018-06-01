package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.definitions.IStateItem;
import iota.common.functions.Heartbeat;
import iota.desktop.jfx.views.state.DefaultStateDisp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class HeartbeatView extends VBox implements UpdateAbleView {

    private List<DefaultStateDisp> stateDisps;
    private Heartbeat funcInstance;

    protected HeartbeatView(Heartbeat funcInstance) {
        super();
        this.funcInstance = funcInstance;




        Button hbeatBtn = new Button("Heartbeat");

        hbeatBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

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
