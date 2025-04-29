package models;

public class Baggage {
    private int baggageId;
    private int passengerId;
    private double weight;
    private String status;
    public Baggage(int baggageId,int passengerId,double weight,String status)
    {
        this.baggageId=baggageId;
        this.passengerId=passengerId;
        this.weight=weight;
        this.status=status;
    }
    public int getBaggageId(){
        return baggageId;
    }
    public void setBaggageId(int baggageId) {
        this.baggageId = baggageId;
    }
    public int getPassengerId(){
        return passengerId;
    }
    public void setPassengerId(int passengerId)
    {
        this.passengerId=passengerId;
    }
    public Double getWeight(){
        return weight;
    }
    public void setWeight(double weight)
    {
        this.weight=weight;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    
}

