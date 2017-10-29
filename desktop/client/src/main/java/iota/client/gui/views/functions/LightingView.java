package iota.client.gui.views.functions;

import iota.client.gui.views.UpdateAbleView;
import iota.client.gui.views.state.DefaultStateDisp;
import iota.client.model.EspDevice;
import iota.common.IoTaUtil;
import iota.common.definitions.IFuncDef;
import iota.common.definitions.IStateItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class LightingView extends GridPane implements UpdateAbleView {
    private final EspDevice device;
    private List<DefaultStateDisp> stateDisps;
    private IFuncDef funcInstance;
    private Button sendIt;
    private List<TextField> newVals;


    protected LightingView(EspDevice deviceIn) {
        super();
        this.device = deviceIn;

        funcInstance = null;
        for (IFuncDef def : device.getFuncs()) {
            if (def.getFuncId() == 2) {
                funcInstance = def;
                break;
            }
        }

        Text t = new Text("Lights");
        super.getChildren().add(t);
        stateDisps = new ArrayList<>();


        for (IStateItem state : funcInstance.getStateItems()) {
            stateDisps.add(new DefaultStateDisp(state));
        }
        for (int i = 0; i < stateDisps.size(); i++) {
            super.add(stateDisps.get(i), 0, i + 1);
        }

        newVals = new ArrayList<>();
        for (int i = 0; i < stateDisps.size(); i++) {
            TextField field = new TextField();
            newVals.add(field);
            super.add(field, 1, i + 1);
        }


        sendIt = new Button("Send it");
        sendIt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (int i = 0; i < stateDisps.size(); i++) {
                    String val = newVals.get(i).getCharacters().toString();
                    if (!val.equals("")) {
                        System.out.println(val);

                        try {
                            byte[] bytes = IoTaUtil.hexStringToByteArray(val);
                            byte[] msg = {0, 2, 4, 0, 0, 0};
                            msg[4] = (byte) i;
                            msg[5] = bytes[0];
                            device.submitMessage(msg);
                            newVals.get(i).setPromptText(val);
                            newVals.get(i).clear();
                        } catch (IllegalArgumentException e1) {
                            newVals.get(i).setPromptText(val + " is invalid, input hex!");
                            newVals.get(i).clear();
                        }


                    }
                }
            }
        });
        super.add(sendIt, 0, stateDisps.size() + 2);



    }

    @Override
    public void updateView() {
        for (DefaultStateDisp d : stateDisps) {
            d.updateView();
        }
    }
}
