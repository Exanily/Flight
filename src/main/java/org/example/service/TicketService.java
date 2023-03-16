package org.example.service;

import org.example.dto.Ticket;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public static double averageFlightTimeInMinutes(List<Ticket> tickets) {
        return tickets.stream().mapToLong(TicketService::getTimeOfFlight).average().orElseThrow(NullPointerException::new);
    }

    public static long getPercentile(List<Ticket> tickets, double percentile) {
        List<Long> times = new ArrayList<>();
        for (Ticket ticket : tickets) {
            times.add(getTimeOfFlight(ticket));
        }
        times.sort(Long::compareTo);
        int index = (int) Math.ceil((percentile / 100.0) * times.size());
        return times.get(index - 1);
    }

    private static long getTimeOfFlight(Ticket ticket) {
        return ChronoUnit.MINUTES.between(ticket.getDepartureDate().toInstant(), ticket.getArrivalDate().toInstant());
    }
}
