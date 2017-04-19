package controller;

/**
 * FlightChangeController.java - The Object that controls the currently active/selected flight.  Will notify all
 * listeners anytime the selected/active flight is changed.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public interface FlightChangeController {
    void addListener( FlightChangeListener fcl );
    void notifyListeners();
}
