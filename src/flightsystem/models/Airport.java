package flightsystem.models;

public class Airport {
    private int airportID;
    private String airportName;
    private String location;

    public Airport(int airportID, String airportName, String location){
        this.airportID = airportID;
        this.airportName = airportName;
        this.location = location;
  }   
   
  public int getairportID(){
    return airportID;
  }

  public void setairportID(int airportID){
    this.airportID = airportID;
  }

  public String getairportName(){
    return airportName;
  }

  public void setairportName(String airportName){
    this.airportName = airportName;
  }

  public String getlocation(){
    return location;
  }

  public void setlocation(String location){
    this.location = location;
  }
}

