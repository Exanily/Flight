package org.example;

import org.example.dto.Ticket;
import org.example.service.TicketService;
import org.example.util.ParserJson;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;

    public static void main(String[] args) {
       String path = getPath();
        List<Ticket> tickets = ParserJson.parse(path);
        double averageTime = TicketService.averageFlightTimeInMinutes(tickets);
        printFlightTime((int) averageTime, "Average flight time - ");
        printPercentileValues(tickets, 90);
    }

    private static void printFlightTime(int time, String message) {
        int totalHours = time / MINUTES_IN_HOUR;
        int totalDays = totalHours / HOURS_IN_DAY;
        int minutes = time - totalHours * MINUTES_IN_HOUR;
        int hours = totalHours - totalDays * HOURS_IN_DAY;
        System.out.println(message + totalDays + " days, " + hours + " hours, " + minutes + " minutes");
    }


    public static void printPercentileValues(List<Ticket> tickets, int percentile) {
        printFlightTime((int) TicketService.getPercentile(tickets, percentile), "90th percentile of flight time - ");
    }

    private static String getPath(){
        System.out.println("Enter the absolute path to the file : ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }
}