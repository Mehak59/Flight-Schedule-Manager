package services;

import java.io.*;
import java.util.*;

public class SeatService {

    private static final int ROWS = 6;
    private static final int COLUMNS = 4;
    private static final String SEAT_FILE = "data/seats.txt";

    public char[][] initializeSeatMap() {
        char[][] seatMap = new char[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                seatMap[i][j] = 'O';
            }
        }
        return seatMap;
    }

    public void displaySeatMap(char[][] seatMap) {
        System.out.println("\nSeat Map:");
        System.out.print("  ");
        char seatAlphabet = 'A';
        for (int j = 0; j < COLUMNS; j++) {
            System.out.print((char) (seatAlphabet) + " ");
            seatAlphabet++;
        }
        System.out.println();
        for (int i = 0; i < ROWS; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(seatMap[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String getSeatNumber(Scanner scanner) {
        String seatNumber = "";
        while (seatNumber.isEmpty()) {
            System.out.print("Enter seat number (e.g., 1A): ");
            seatNumber = scanner.nextLine().trim().toUpperCase();
            if (!isValidSeatNumber(seatNumber)) {
                System.out.println("Invalid seat number format.  Please use format like 1A, 2B, etc.");
                seatNumber = "";
            }
        }
        return seatNumber;
    }

    private boolean isValidSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) {
            return false;
        }
        try {
            int row = Integer.parseInt(seatNumber.substring(0, seatNumber.length() - 1));
            char column = seatNumber.charAt(seatNumber.length() - 1);

            if (row <= 0 || row > ROWS) {
                return false;
            }
            if (column < 'A' || column >= 'A' + COLUMNS) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean bookSeat(char[][] seatMap, String seatNumber) {
        try {
            int row = Integer.parseInt(seatNumber.substring(0, seatNumber.length() - 1)) - 1;
            int col = seatNumber.charAt(seatNumber.length() - 1) - 'A';

            if (seatMap[row][col] == 'O') {
                seatMap[row][col] = 'X';
                return true;
            } else {
                System.out.println("Seat " + seatNumber + " is already booked.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid seat number: " + seatNumber);
            return false;

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid seat number: " + seatNumber);
            return false;
        }
    }

    public void saveSeatBookings(int flightId, char[][] seatMap) {
        Map<Integer, char[][]> flightSeatMap = loadSeatBookings();
        flightSeatMap.put(flightId, seatMap);
        try (PrintWriter writer = new PrintWriter(new FileWriter(SEAT_FILE, false))) {
            for (Map.Entry<Integer, char[][]> entry : flightSeatMap.entrySet()) {
                int id = entry.getKey();
                char[][] map = entry.getValue();
                writer.print(id + ",");
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLUMNS; j++) {
                        writer.print(map[i][j]);
                        if (i != ROWS - 1 || j != COLUMNS - 1) {
                            writer.print(",");
                        }
                    }
                }
                writer.println();
            }
            System.out.println("Seat bookings saved for flight " + flightId);
        } catch (IOException e) {
            System.out.println("Error saving seat bookings: " + e.getMessage());
        }
    }

    public Map<Integer, char[][]> loadSeatBookings() {
        Map<Integer, char[][]> flightSeatMap = new HashMap<>();
        File file = new File(SEAT_FILE);
        if (!file.exists()) {
            return flightSeatMap;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    try {
                        int flightId = Integer.parseInt(parts[0]);
                        char[][] seatMap = new char[ROWS][COLUMNS];
                        int index = 1;
                        for (int i = 0; i < ROWS; i++) {
                            for (int j = 0; j < COLUMNS; j++) {
                                if (index < parts.length && parts[index].length() > 0) {
                                    seatMap[i][j] = parts[index].charAt(0);
                                }
                                index++;
                            }
                        }
                        flightSeatMap.put(flightId, seatMap);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid flight ID format in data file. Skipping entry.");
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading seat bookings: " + e.getMessage());
        }
        return flightSeatMap;
    }

    public char[][] getOrCreateSeatMap(int flightId) {
        Map<Integer, char[][]> flightSeatMap = loadSeatBookings();

        if (flightSeatMap.containsKey(flightId)) {
            return flightSeatMap.get(flightId);
        } else {
            char[][] newSeatMap = initializeSeatMap();
            flightSeatMap.put(flightId, newSeatMap);
            return newSeatMap;
        }
    }
}
