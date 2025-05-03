package models;

public class Airport {
  private int airportID;
  private String airportName;
  private String location;
  private boolean layoverAirport;

  public Airport(int airportID, String airportName, String location) {
    this.airportID = airportID;
    this.airportName = airportName;
    this.location = location;
    this.layoverAirport = false;
  }

  public Airport(int airportID, String airportName, String location, boolean layoverAirport) {
    this.airportID = airportID;
    this.airportName = airportName;
    this.location = location;
    this.layoverAirport = layoverAirport;
  }

  public int getAirportID() {
    return airportID;
  }

  public void setAirportID(int airportID) {
    this.airportID = airportID;
  }

  public String getAirportName() {
    return airportName;
  }

  public void setAirportName(String airportName) {
    this.airportName = airportName;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean isLayoverAirport() {
    return layoverAirport;
  }

  public void setLayoverAirport(boolean layoverAirport) {
    this.layoverAirport = layoverAirport;
  }
}
