package com.slf4j.logger;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AirportView {

    public static Label statusLabel;
    private ListView<String> landedFlightsListView;
    private TextField flightNumberInput;
    public static Pane animationPane;
    public static Pane waitingPane;

    public AirportView(Label statusLabel, ListView<String> landedFlightsListView, TextField flightNumberInput, Pane animationPane, Pane waitingPane) {
        AirportView.statusLabel = statusLabel;
        this.landedFlightsListView = landedFlightsListView;
        this.flightNumberInput = flightNumberInput;
        AirportView.animationPane = animationPane;
        AirportView.waitingPane = waitingPane;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public ListView<String> getLandedFlightsListView() {
        return landedFlightsListView;
    }

    public TextField getFlightNumberInput() {
        return flightNumberInput;
    }

    public Pane getAnimationPane() {
        return animationPane;
    }

    public Pane getWaitingPane() {
        return waitingPane;
    }
}