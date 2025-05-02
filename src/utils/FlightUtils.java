package utils;

import models.Flight;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FlightUtils {
    public static List<Flight> loadFlights(String filePath) {
        List<Flight> flights = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = parseCSVLine(line);
                LocalDate flightDate = LocalDate.parse(data[1], formatter);
                Flight flight = new Flight(
                        Integer.parseInt(data[0]),
                        flightDate,
                        data[2], data[3],
                        data[4], data[5],
                        Integer.parseInt(data[6]),
                        Double.parseDouble(data[7]),
                        Double.parseDouble(data[8]),
                        Double.parseDouble(data[9]),
                        Integer.parseInt(data[10]),
                        data[11]
                );
                flights.add(flight);
            }
        } catch (IOException e) {
            System.out.println("Error reading flight data: " + e.getMessage());
        }
        return flights;
    }


    private static String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean quote = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                quote = !quote;
            } else if (c == ',' && !quote) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens.toArray(new String[0]);
    }
}
