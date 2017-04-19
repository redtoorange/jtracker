package model;

/**
 * FlightChangeListener.java - An object that is interest in changes that are made to the currently active/selected
 * flight.
 *
 * @author Andrew McGuiness
 * @version 18/Apr/2017
 */
public interface FlightChangeListener {
    void flightChanged( Flight flight );
}
