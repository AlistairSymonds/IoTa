package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.client.model.EspDevice;
import iota.common.IoTaUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class DebugView extends VBox implements UpdateAbleView {

    private final EspDevice device;
    private VBox box;


    public DebugView(EspDevice deviceIn) {
        super();
        this.device = deviceIn;



        TextField textField = new TextField();

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    device.submitMessage(IoTaUtil.hexStringToByteArray(textField.getCharacters().toString()));
                } catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                }
            }
        });


        super.getChildren().add(textField);
        super.getChildren().add(sendBtn);
    }

    @Override
    public void updateView() {

    }

}
