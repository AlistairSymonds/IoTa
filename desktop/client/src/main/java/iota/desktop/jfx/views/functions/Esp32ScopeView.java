package iota.desktop.jfx.views.functions;

import iota.client.UpdateAbleView;
import iota.common.functions.Esp32Scope;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.util.List;

public class Esp32ScopeView extends GridPane implements UpdateAbleView {
    private Esp32Scope funcInstance;
    private Button reqDataBtn;
    private LineChart<Number, Number> waveform;
    private NumberAxis voltageAx;
    private NumberAxis timeAx;


    protected Esp32ScopeView(Esp32Scope funcInstance) {
        super();

        this.funcInstance = funcInstance;


        Text t = new Text("Scope");
        super.getChildren().add(t);


        reqDataBtn = new Button("Get sample Data");
        reqDataBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                funcInstance.updateScopeData();
            }
        });

        super.add(reqDataBtn, 0, 0);

        timeAx = new NumberAxis();
        voltageAx = new NumberAxis();
        waveform = new LineChart<>(timeAx, voltageAx);

        //waveform.getStylesheets().add("Scope.css");

        waveform.setAnimated(false);
        super.add(waveform, 1, 0);

        Button redrawBtn = new Button("Redraw Graph");
        redrawBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                redrawChart();
            }
        });
        super.add(redrawBtn, 0, 2);


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
        double ts = funcInstance.getT_s();
        int step = 1;
        int maxPts = 10000;
        if (maxPts < v.size()) {
            step = v.size() / maxPts;
        }

        for (int i = 0; i < t.length; i = i + step) {
            t[i] = i * ts;
        }
        for (int i = 0; i < v.size(); i = i + step) {
            wf.getData().add(new XYChart.Data<>(t[i], v.get(i)));
        }

        waveform.getData().clear();
        waveform.getData().add(wf);
    }

    @Override
    public void updateView() {


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
