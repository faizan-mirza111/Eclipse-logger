package com.slf4j.logger;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AirportController {

    private static final Logger logger = LoggerFactory.getLogger(AirportController.class);

    @FXML
	protected TextField flightNumberInput;
    @FXML
	public Label statusLabel;
    @FXML
    private ListView<String> landedFlightsListView;
    @FXML
    private Pane animationPane;
    @FXML
    private Pane waitingPane;

    private AirportModel model;
    public static final int MAX_FLIGHTS = 5;
    public Queue<String> flights = new LinkedList<>();
    public ObservableList<String> landedFlights = FXCollections.observableArrayList();
    public Map<String, ImageView> planeMap = new HashMap<>();
    public Map<String, Label> labelMap = new HashMap<>();
    public Queue<String> waitingQueue = new LinkedList<>();
    public Map<String, ImageView> waitingPlaneMap = new HashMap<>();
    public Map<String, Label> waitingLabelMap = new HashMap<>();
    public Map<String, Timeline> waitingTimers = new HashMap<>();
    public Set<String> crashedFlights = new HashSet<>();
    public Queue<String> reLandQueue = new LinkedList<>();
    public Map<String, PathTransition> flyingTransitions = new HashMap<>();

    public AirportController() {
        model = new AirportModel();
    }

    @FXML
    public void initialize() {
        try {
            landedFlightsListView.setItems(model.getLandedFlights());
            logger.info("Airport Controller initialized. Landed flights list view set.");
        } catch (Exception e) {
            logger.error("Error initializing Airport Controller: ", e);
        }
    }

    @FXML
    public void handleLandFlight() {
        try {
            String flightNumber = flightNumberInput.getText().trim();
            model.landFlight(flightNumber);
        } catch (Exception e) {
            logger.error("Error handling landing flight: ", e);
        }
    }

    @FXML
    public void handleDepartFlight() {
        try {
            String flightNumber = flightNumberInput.getText().trim();
            model.departFlight(flightNumber);
        } catch (Exception e) {
            logger.error("Error handling departure flight: ", e);
        }
    }
}