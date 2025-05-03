package utils;

import models.Flight;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class FlightUtils {

    public static List<Flight> loadFlights(String filename) {
        List<Flight> flights = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;

                String[] data = line.split(",");

                try {
                    int flightID = Integer.parseInt(data[0].trim());
                    LocalDate flightDate = LocalDate.parse(data[1].trim());
                    String source = data[2].trim();
                    String destination = data[3].trim();
                    String departureTime = data[4].trim();
                    String arrivalTime = data[5].trim();
                    int capacity = Integer.parseInt(data[6].trim());
                    int economyFare = Integer.parseInt(data[7].trim());
                    int businessFare = Integer.parseInt(data[8].trim());
                    int firstClassFare = Integer.parseInt(data[9].trim());
                    int isSpecial = Integer.parseInt(data[10].trim());
                    String status = data[11].trim();
                    String checkInTime = data[12].trim();
                    String layover = data[13].trim();
                    String layoverTime = data[14].trim();

                    Flight flight = new Flight(flightID, flightDate, source, destination, departureTime,
                            arrivalTime, capacity, economyFare, businessFare, firstClassFare,
                            isSpecial, status, checkInTime, layover, layoverTime);

                    flights.add(flight);

                } catch (Exception e) {
                    System.out.println("Parsing error in line: " + line);
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading flight file: " + filename);
            e.printStackTrace();
        }

        return flights;
    }
}
