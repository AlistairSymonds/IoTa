package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.functions.Esp32Scope;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class Esp32ScopeView extends GridPane implements UpdateAbleView {
    private Esp32Scope funcInstance;
    private Button reqDataBtn;
    private LineChart<Number, Number> waveform;
    private NumberAxis voltageAx;
    private NumberAxis timeAx;

    private HBox sampleRateContainer;
    private Text sampleRate;

    private HBox triggerContainer;
    private Text triggerStatus;
    private Button updateTriggerBtn;
    private TextField newTriggerLevel;
    private Button clearTrigBtn;


    private HBox gainContainer;
    private Text gainText;
    private ComboBox<Integer> gainVals;

    private FlowPane controlContainer;

    protected Esp32ScopeView(Esp32Scope funcInstance) {
        super();

        this.funcInstance = funcInstance;
        controlContainer = new FlowPane();
        controlContainer.setVgap(8);
        controlContainer.setHgap(4);
        Text t = new Text("Scope");
        super.getChildren().add(t);


        reqDataBtn = new Button("Get sample Data");
        reqDataBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                funcInstance.updateScopeData();
            }
        });

        controlContainer.getChildren().add(reqDataBtn);

        timeAx = new NumberAxis();
        voltageAx = new NumberAxis();
        waveform = new SpeedLineChart<>(timeAx, voltageAx);

        waveform.setAnimated(false);
        waveform.getStylesheets().add("Scope.css");
        super.add(waveform, 1, 0);

        Button redrawBtn = new Button("Redraw Graph");
        redrawBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                redrawChart();
            }
        });
        super.add(redrawBtn, 0, 0);


        //GAIN
        gainContainer = new HBox();

        gainText = new Text("Unitialised");
        gainContainer.getChildren().add(gainText);

        gainVals = new ComboBox<>();
        gainVals.getItems().add(1);
        gainVals.getItems().add(2);
        gainVals.getItems().add(4);
        gainVals.getItems().add(8);
        gainVals.getItems().add(16);
        gainVals.getItems().add(32);
        gainVals.getItems().add(64);
        gainVals.getItems().add(128);
        gainVals.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                funcInstance.setGain(newValue.shortValue());
            }
        });
        gainContainer.getChildren().add(gainVals);

        controlContainer.getChildren().add(gainContainer);
        //SAMPLE RATE

        sampleRateContainer = new HBox();
        sampleRate = new Text("Uinitialised");
        sampleRateContainer.getChildren().add(sampleRate);

        ComboBox<Double> sampleRateSelector = new ComboBox<>();
        sampleRateSelector.getItems().add(1 / (1 * Math.pow(10, -5)));
        sampleRateSelector.getItems().add(1 / (2 * Math.pow(10, -5)));
        sampleRateSelector.getItems().add(1 / (5 * Math.pow(10, -5)));

        sampleRateSelector.getItems().add(1 / (1 * Math.pow(10, -4)));
        sampleRateSelector.getItems().add(1 / (2 * Math.pow(10, -4)));
        sampleRateSelector.getItems().add(1 / (5 * Math.pow(10, -4)));

        sampleRateSelector.getItems().add(1 / (1 * Math.pow(10, -3)));
        sampleRateSelector.getItems().add(1 / (2 * Math.pow(10, -3)));
        sampleRateSelector.getItems().add(1 / (5 * Math.pow(10, -3)));


        sampleRateSelector.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                System.out.println(funcInstance.setTs(1 / newValue));

            }
        });

        sampleRateContainer.getChildren().add(sampleRateSelector);
        controlContainer.getChildren().add(sampleRateContainer);


        //TRIGGER LEVEL
        triggerContainer = new HBox();

        triggerStatus = new Text("Uninitialised");
        triggerContainer.getChildren().add(triggerStatus);
        clearTrigBtn = new Button("Clear Trigger");
        clearTrigBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                funcInstance.clearTrigger();
            }
        });
        triggerContainer.getChildren().add(clearTrigBtn);

        newTriggerLevel = new TextField();
        newTriggerLevel.setPromptText("Enter trigger level (0-3.3)");
        triggerContainer.getChildren().add(newTriggerLevel);

        updateTriggerBtn = new Button("Update Trigger Level");
        updateTriggerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    double val = Double.parseDouble(newTriggerLevel.getText());
                    funcInstance.setTriggerLevel(val);
                } catch (Exception e) {
                    newTriggerLevel.setPromptText("Invalid trigger value");
                }

            }
        });
        triggerContainer.getChildren().add(updateTriggerBtn);


        controlContainer.getChildren().add(triggerContainer);
        for (Node n : controlContainer.getChildren()) {
            n.setStyle("-fx-border-color: #2b35b4");
        }
        super.add(controlContainer, 0, 1, 2, 1);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.NEVER);
        super.getColumnConstraints().add(column1);

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        super.getColumnConstraints().add(column2);
    }

    private void redrawChart() {
        XYChart.Series<Number, Number> wf = new XYChart.Series();
        List<Double> v = funcInstance.getScopeData();
        //generate time indexes
        double[] t = new double[v.size()];
        double ts = funcInstance.getTs();
        int step = 1;
        int maxPts = 10000;
        if (maxPts < v.size()) {
            step = v.size() / maxPts;
        }

        for (int i = 0; i < t.length; i = i + step) {
            t[i] = (i * ts) - (ts * (t.length / 2));
        }
        for (int i = 0; i < v.size(); i = i + step) {
            wf.getData().add(new XYChart.Data<>(t[i], v.get(i)));
        }

        waveform.getData().clear();
        waveform.getData().add(wf);
    }

    @Override
    public void updateView() {
        sampleRate.setText("Sample rate: " + String.valueOf(1 / funcInstance.getTs()) + "[sps]");
        triggerStatus.setText("Trig lvl: " + funcInstance.getTriggerLevel() + "[V], status: " + funcInstance.isTriggered());
        gainText.setText("Gain: " + funcInstance.getGain() + "[x]");

        //redrawChart();
    }


    private class SpeedLineChart<X, Y> extends LineChart<X, Y> {
        public SpeedLineChart(Axis<X> numberAxis, Axis<Y> numberAxis2) {
            super(numberAxis, numberAxis2);
        }

        @Override
        protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {
            //lol
        }
    }


}
