package org.example.util;

import org.example.dto.Ticket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ParserJson {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy'T'HH:mmX");

    public static List<Ticket> parse(String path) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray ticketsJson = (JSONArray) parser.parse(getJsonFile(path));
            return parseTickets(ticketsJson);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Ticket> parseTickets(JSONArray ticketsJson) {
        List<Ticket> tickets = new ArrayList<>();
        ticketsJson.forEach(ticketJson -> {
            JSONObject ticketJsonObject = (JSONObject) ticketJson;
            tickets.add(parseTicketObject(ticketJsonObject));
        });
        return tickets;
    }

    private static String getJsonFile(String paths) {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(paths));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }

    private static Ticket parseTicketObject(JSONObject ticketJson) {
        Ticket ticket = new Ticket();
        ticket.setFrom((String) ticketJson.get("from"));
        ticket.setTo((String) ticketJson.get("to"));
        ticket.setDepartureDate(getDate(ticketJson, "departure_date", "departure_time", "time_zone_from"));
        ticket.setArrivalDate(getDate(ticketJson, "arrival_date", "arrival_time", "time_zone_to"));
        return ticket;
    }

    private static Calendar getDate(JSONObject ticketJson, String date, String time, String timeZone) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(SDF.parse(ticketJson.get(date) + "T" + ticketJson.get(time) + ticketJson.get(timeZone)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return calendar;
    }
}
