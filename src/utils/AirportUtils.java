package utils;

import models.Airport;
import java.io.*;
import java.util.*;

public class AirportUtils {
    public static List<Airport> loadAirports(String filePath) {
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 3) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }
                int airportID = Integer.parseInt(data[0]);
                String airportName = data[1];
                String location = data[2];

                if (airportName.isEmpty() || location.isEmpty()) {
                    System.out.println("Invalid airport data in line: " + line);
                    continue;
                }

                Airport airport = new Airport(airportID, airportName, location);
                airports.add(airport);
            }
        } catch (IOException e) {
            System.out.println("Error reading airport data: " + e.getMessage());
        }
        return airports;
    }
}