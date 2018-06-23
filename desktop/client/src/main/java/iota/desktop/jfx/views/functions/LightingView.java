package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.functions.LightingFunction.AnimationList;
import iota.common.functions.LightingFunction.Lighting;
import iota.common.functions.LightingFunction.LightingStateEnum;
import iota.desktop.jfx.views.state.DefaultStateDisp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.util.List;

public class LightingView extends GridPane implements UpdateAbleView {

    private List<DefaultStateDisp> stateDisps;
    private Lighting funcInstance;
    private final ColorPicker colourSelector;
    private ComboBox<AnimationList> programSelector;

    protected LightingView(Lighting funcInstance) {
        super();
        this.funcInstance = funcInstance;
        colourSelector = new ColorPicker();
        colourSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                funcInstance.setState(LightingStateEnum.hue, (byte) (colourSelector.getValue().getHue() * 255));
                funcInstance.setState(LightingStateEnum.sat, (byte) (colourSelector.getValue().getSaturation() * 255));
                funcInstance.setState(LightingStateEnum.value, (byte) (colourSelector.getValue().getBrightness() * 255));
            }
        });
        super.getChildren().add(colourSelector);


        programSelector = new ComboBox<>();
        for (AnimationList anim : funcInstance.getAnimationList()) {
            programSelector.getItems().add(anim);
        }
        programSelector.valueProperty().addListener(new ChangeListener<AnimationList>() {
            @Override
            public void changed(ObservableValue<? extends AnimationList> observable, AnimationList oldValue, AnimationList newValue) {
                funcInstance.setAnimation(newValue);
            }
        });
        super.getChildren().add(programSelector);





        /*
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
                            //byte[] msg = {0, 2, 4, 0, 0, 0};
                            //msg[4] = (byte) i;
                            //msg[5] = bytes[0];

                            List<Byte> msg = new ArrayList<>();
                            msg.add((byte) i);
                            msg.add(bytes[0]);


                            DataCapsule cap = new DataCapsule(IoTaUtil.getHostId(), device.getId(), funcInstance.getFuncId(), msg);
                            device.submitMessage(cap);
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
        */


    }

    @Override
    public void updateView() {

    }
}
