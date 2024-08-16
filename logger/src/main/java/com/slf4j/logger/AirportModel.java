package com.slf4j.logger;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.util.*;

public class AirportModel extends AirportController {

   
    AirportAnimation obj=new AirportAnimation();
    public ObservableList<String> getLandedFlights() {
        return landedFlights;
    }

    public void landFlight(String flightNumber) {
        if (flights.contains(flightNumber)) {
            // Flight number already exists
            return;
        }

        if (flights.size() >= MAX_FLIGHTS) {
            waitingQueue.add(flightNumber);
            obj.displayWaitingPlane(flightNumber);
            obj.startWaitingTimer(flightNumber);
        } else {
            landFlight(flightNumber);
        }
    }

    public void departFlight(String flightNumber) {
        if (!flights.contains(flightNumber)) {
            // Flight not found
            return;
        }

        List<String> flightList = new ArrayList<>(flights);
        int index = flightList.indexOf(flightNumber);

        ImageView plane = planeMap.get(flightNumber);
        Label label = labelMap.get(flightNumber);

        if (index == 0) {
            // Flight 1 departs normally
            obj.animateDeparture(plane, label, () -> {
                flights.remove(flightNumber);
                landedFlights.remove(flightNumber);
                obj.repositionFlights();
                obj.processWaitingPlanes();
            });
        } else {
            // Move all flights landed before the departing plane down one by one
            obj.moveDownNextFlightsSequentially(flightList, index, 0);
        }
    }

    // ... other methods ...
}