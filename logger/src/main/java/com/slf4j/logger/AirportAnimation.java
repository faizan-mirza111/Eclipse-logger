package com.slf4j.logger;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.image.Image;



public class AirportAnimation extends AirportController{
	 private static final Logger logger = LoggerFactory.getLogger(AirportController.class);
	 

    public void animateLanding(String flightNumber, int position, Runnable onComplete) {
    	try {
            ImageView plane = new ImageView(new Image(getClass().getResourceAsStream("plane.png")));
            plane.setFitWidth(100);
            plane.setFitHeight(89);
            Label label = new Label(flightNumber);
            label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");
            planeMap.put(flightNumber, plane);
            labelMap.put(flightNumber, label);
            AirportView.animationPane.getChildren().addAll(plane, label);

            double xPosition = AirportConstants.INITIAL_X_POSITION + (MAX_FLIGHTS - 1 - position) * AirportConstants.SPACING_X;
            double yPosition = AirportConstants.INITIAL_Y_POSITION;

            plane.setTranslateX(xPosition);
            plane.setTranslateY(yPosition);
            label.setTranslateX(xPosition);
            label.setTranslateY(yPosition + plane.getFitHeight() + 5);

            TranslateTransition planeTransition = new TranslateTransition(Duration.seconds(3), plane);
            planeTransition.setToX(xPosition);
            planeTransition.setToY(yPosition);
            planeTransition.setOnFinished(e -> onComplete.run());
            planeTransition.play();

            TranslateTransition labelTransition = new TranslateTransition(Duration.seconds(3), label);
            labelTransition.setToX(xPosition);
            labelTransition.setToY(yPosition + plane.getFitHeight() + 5);
            labelTransition.play();
            logger.info("Animating landing for flight {} at position {}.", flightNumber, position);
        } catch (Exception e) {
            logger.error("Error animating landing for flight {}: ", flightNumber, e);
        }
    }

    
	public void animateDeparture(ImageView plane, Label label, Runnable onComplete) {
        try {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(3), plane);
            transition.setFromX(plane.getTranslateX());
            transition.setToX( AirportView.animationPane.getWidth());
            transition.setOnFinished(e -> {
                AirportView.animationPane.getChildren().removeAll(plane, label);
                planeMap.remove(plane);
                labelMap.remove(label);
                onComplete.run();
            });
            transition.play();
            logger.info("Animating departure for plane with ID {}.", plane.getId());
        } catch (Exception e) {
            logger.error("Error animating departure: ", e);
        }
    }

    public void displayWaitingPlane(String flightNumber) {
    	try {
            ImageView waitingPlane = new ImageView(new Image(getClass().getResourceAsStream("fighter6.png")));
            waitingPlane.setFitWidth(100);
            waitingPlane.setFitHeight(89);
            Label waitingLabel = new Label(flightNumber);
            waitingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: red;");
            waitingPlaneMap.put(flightNumber, waitingPlane);
            waitingLabelMap.put(flightNumber, waitingLabel);
            AirportView.waitingPane.getChildren().addAll(waitingPlane, waitingLabel);

            double xPosition = AirportConstants.WAITING_X_POSITION + (waitingQueue.size() - 1) *AirportConstants.WAITING_SPACING_X;
            double yPosition = AirportConstants.WAITING_Y_POSITION;

            waitingPlane.setTranslateX(xPosition);
            waitingPlane.setTranslateY(yPosition);

            waitingLabel.setTranslateX(xPosition);
            waitingLabel.setTranslateY(yPosition + waitingPlane.getFitHeight() + 5);

            // Add the flying animation for waiting planes
            Circle path = new Circle(xPosition, yPosition, 20);
            PathTransition flyingTransition = new PathTransition(Duration.seconds(2), path, waitingPlane);
            flyingTransition.setCycleCount(PathTransition.INDEFINITE);
            flyingTransition.play();
            flyingTransitions.put(flightNumber, flyingTransition);
            logger.info("Displayed waiting plane {} with flying animation.", flightNumber);
        } catch (Exception e) {
            logger.error("Error displaying waiting plane {}: ", flightNumber, e);
        }
    }

    public void removeWaitingPlane(String flightNumber) {
        // ... implementation ...
    	 try {
             ImageView waitingPlane = waitingPlaneMap.get(flightNumber);
             Label waitingLabel = waitingLabelMap.get(flightNumber);
             if (waitingPlane != null && waitingLabel != null) {
            	 AirportView.waitingPane.getChildren().removeAll(waitingPlane, waitingLabel);
                 waitingPlaneMap.remove(flightNumber);
                 waitingLabelMap.remove(flightNumber);
                 logger.info("Removed waiting plane {}.", flightNumber);
             }

             // Stop the timer
             Timeline timer = waitingTimers.remove(flightNumber);
             if (timer != null) {
                 timer.stop();
                 logger.info("Stopped waiting timer for flight {}.", flightNumber);
             }
         } catch (Exception e) {
             logger.error("Error removing waiting plane {}: ", flightNumber, e);
         }
    }

    public void startWaitingTimer(String flightNumber) {
        // ... implementation ...
    	 try {
             Timeline timer = new Timeline(new KeyFrame(Duration.seconds(18), event -> {
                 // If the plane is still in the waiting queue, it crashes
                 if (waitingQueue.contains(flightNumber)) {
                     waitingQueue.remove(flightNumber);
                     removeWaitingPlane(flightNumber);
                     crashedFlights.add(flightNumber);
                     AirportView. statusLabel.setText("Flight " + flightNumber + " crashed after waiting for so long!");
                     AirportView.statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
                     showExplosion();
                     logger.error("Flight {} crashed after waiting for too long.", flightNumber);
                 }
             }));
             timer.setCycleCount(1);
             timer.play();
             waitingTimers.put(flightNumber, timer);
             logger.info("Started waiting timer for flight {}.", flightNumber);
         } catch (Exception e) {
             logger.error("Error starting waiting timer for flight {}: ", flightNumber, e);
         }
    }

    public void showExplosion() {
        // ... implementation ...
    	 try {
             ImageView explosion = new ImageView(new Image(getClass().getResourceAsStream("blasting.png")));
             explosion.setFitWidth(100);
             explosion.setFitHeight(100);
             AirportView.waitingPane.getChildren().add(explosion);

             TranslateTransition transition = new TranslateTransition(Duration.seconds(2), explosion);
             transition.setFromX(700); // Position for the explosion, adjust as needed
             transition.setToX(750); // End position for the explosion
             transition.setOnFinished(e ->  AirportView.waitingPane.getChildren().remove(explosion));
             transition.play();
             logger.info("Explosion animation triggered.");
         } catch (Exception e) {
             logger.error("Error showing explosion: ", e);
         }
    }

    public void repositionFlights() {
        // ... implementation ...
    	 try {
             List<String> flightList = new ArrayList<>(flights);
             repositionNextFlight(flightList, 0);
         } catch (Exception e) {
             logger.error("Error repositioning flights: ", e);
         }
    }
    private void repositionNextFlight(List<String> flightList, int currentIndex) {
        try {
            if (currentIndex >= flightList.size()) {
                return;
            }

            String flightNumber = flightList.get(currentIndex);
            ImageView plane = planeMap.get(flightNumber);
            Label label = labelMap.get(flightNumber);

            double xPosition = AirportConstants.INITIAL_X_POSITION + (MAX_FLIGHTS - 1 - currentIndex) * AirportConstants.SPACING_X;
            double yPosition = AirportConstants.INITIAL_Y_POSITION;

            TranslateTransition planeTransition = new TranslateTransition(Duration.seconds(0.75), plane);
            planeTransition.setToX(xPosition);
            planeTransition.setToY(yPosition);
            planeTransition.setOnFinished(e -> repositionNextFlight(flightList, currentIndex + 1));
            planeTransition.play();

            TranslateTransition labelTransition = new TranslateTransition(Duration.seconds(0.75), label);
            labelTransition.setToX(xPosition);
            labelTransition.setToY(yPosition + plane.getFitHeight() + 5);
            labelTransition.play();
            logger.info("Repositioning flight {} to new coordinates (x: {}, y: {}).", flightNumber, xPosition, yPosition);
        } catch (Exception e) {
            logger.error("Error repositioning flight {}: ", flightList.get(currentIndex), e);
        }
    }

    public void moveDownNextFlightsSequentially(List<String> flightList, int departingFlightIndex, int currentIndex) {
        // ... implementation ...
    	try {
            if (currentIndex >= departingFlightIndex) {
                ImageView plane = planeMap.get(flightList.get(departingFlightIndex));
                Label label = labelMap.get(flightList.get(departingFlightIndex));
                animateDeparture(plane, label, () -> {
                    flights.remove(flightList.get(departingFlightIndex));
                    landedFlights.remove(flightList.get(departingFlightIndex));
                    repositionFlights();
                    reLandFlightsSequentially(() -> {
                        // Only after re-landing, add waiting planes if space is available
                        Platform.runLater(this::processWaitingPlanes);
                    });
                });
                return;
            }

            String flight = flightList.get(currentIndex);
            ImageView plane = planeMap.get(flight);
            Label label = labelMap.get(flight);

            TranslateTransition moveDownTransition = new TranslateTransition(Duration.seconds(1), plane);
            moveDownTransition.setByY(AirportConstants.MOVE_DOWN_DISTANCE);
            moveDownTransition.setOnFinished(e -> {
                AirportView.animationPane.getChildren().removeAll(plane, label);
                planeMap.remove(flight);
                labelMap.remove(flight);

                flights.remove(flight);
                landedFlights.remove(flight);
                reLandQueue.add(flight);

                moveDownNextFlightsSequentially(flightList, departingFlightIndex, currentIndex + 1);
            });
            moveDownTransition.play();
            logger.info("Flight {} moved down for departure.", flight);
        } catch (Exception e) {
            logger.error("Error moving down flights sequentially: ", e);
        }
    }

    public void reLandFlightsSequentially(Runnable onComplete) {
        // ... implementation ...
    	 try {
             if (reLandQueue.isEmpty()) {
                 onComplete.run();
                 return;
             }

             String flightNumber = reLandQueue.poll();
             landPlane(flightNumber, () -> reLandFlightsSequentially(onComplete));
         } catch (Exception e) {
             logger.error("Error re-landing flights sequentially: ", e);
         }
    }

    public void processWaitingPlanes() {
        // ... implementation ...
    	try {
            if (flights.size() < MAX_FLIGHTS && !waitingQueue.isEmpty()) {
                String nextWaitingFlight = waitingQueue.poll();
                removeWaitingPlane(nextWaitingFlight);
                landPlane(nextWaitingFlight);
                logger.info("Processing waiting plane {}.", nextWaitingFlight);
            }
        } catch (Exception e) {
            logger.error("Error processing waiting planes: ", e);
        }
    }
    private void landPlane(String flightNumber) {
        landPlane(flightNumber, () -> {});
    }

    private void landPlane(String flightNumber, Runnable onComplete) {
        try {
            flights.add(flightNumber);
            landedFlights.add(flightNumber);
            statusLabel.setText("Flight " + flightNumber + " landed successfully.");
            animateLanding(flightNumber, flights.size() - 1, onComplete);
            flightNumberInput.clear();
            logger.info("Flight {} landed successfully.", flightNumber);
        } catch (Exception e) {
            logger.error("Error landing plane {}: ", flightNumber, e);
        }
    }
}