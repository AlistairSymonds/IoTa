package iota.client.gui.views.functions;

import iota.client.model.EspDevice;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class DebugView implements IFunctionView {

    private final EspDevice device;
    private VBox box;


    public DebugView(EspDevice deviceIn) {
        this.device = deviceIn;
        box = new VBox();


        TextField textField = new TextField();

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                device.submitMessage(hexStringToByteArray(textField.getCharacters().toString()));
            }
        });


        box.getChildren().add(textField);
        box.getChildren().add(sendBtn);
    }

    //from stack overflow
    //https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public Pane getView() {

        return box;
    }

    @Override
    public void updateView() {

    }

}
