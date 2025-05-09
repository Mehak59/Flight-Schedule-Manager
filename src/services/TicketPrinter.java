package services;

import java.util.LinkedList;

import models.Passenger;
import models.Flight;

public class TicketPrinter {

    private static final int CONSOLE_WIDTH = 80;

    private static String centerText(String text) {
        int padding = (CONSOLE_WIDTH - text.length()) / 2;
        if (padding < 0)
            padding = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    private static void printLine(char ch) {
        for (int i = 0; i < CONSOLE_WIDTH; i++) {
            System.out.print(ch);
        }
        System.out.println();
    }

    private static void printBoxedLine(String text) {
        int padding = CONSOLE_WIDTH - 4 - text.length(); // 4 for borders and spaces
        if (padding < 0)
            padding = 0;
        System.out.print("|| " + text);
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        System.out.println(" ||");
    }

    public static void printTickets(LinkedList<Passenger> passengers, Flight flight, String flightClass,
            java.util.Queue<String> seatQueue) {
        for (Passenger p : passengers) {
            String seatNumber = seatQueue.poll();
            printLine('=');
            printBoxedLine(centerText("Passenger Ticket"));
            printLine('=');
            printBoxedLine("Passenger Name: " + p.getFirstName() + " " + p.getLastName());
            printBoxedLine("Flight ID: " + flight.getFlightId());
            printBoxedLine("Route: " + flight.getFrom() + " -> " + flight.getTo());
            printBoxedLine("Date: " + flight.getFlightDate());
            printBoxedLine("Class: " + flightClass);
            printBoxedLine("Seat Number: " + (seatNumber != null ? seatNumber : "Assigned at check-in"));
            printBoxedLine("Booking Reference: " + p.getPassengerId().substring(0, 8).toUpperCase());
            printLine('=');
            System.out.println();
        }
    }
}
