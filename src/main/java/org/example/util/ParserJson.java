package org.example.util;

import org.example.dto.Ticket;
import org.example.dto.TimeZoneCity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ParserJson {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yy'T'HH:mmX");

    public static List<Ticket> parse(String path) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject ticketsJson = (JSONObject) parser.parse(getJsonFile(path));
            JSONArray jsonArray = (JSONArray) ticketsJson.get("tickets");
            return parseTickets(jsonArray);
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
        ticket.setOrigin((String) ticketJson.get("origin"));
        ticket.setOriginName((String) ticketJson.get("origin_name"));
        ticket.setDestination((String) ticketJson.get("destination"));
        ticket.setDestinationName((String) ticketJson.get("destination_name"));
        ticket.setDepartureDate(getDate(ticketJson, "departure_date", "departure_time", TimeZoneCity.VVO.getValue()));
        ticket.setArrivalDate(getDate(ticketJson, "arrival_date", "arrival_time", TimeZoneCity.TLV.getValue()));
        ticket.setCarrier((String) ticketJson.get("carrier"));
        ticket.setStops((Long) ticketJson.get("stops"));
        ticket.setStops((Long) ticketJson.get("price"));
        return ticket;
    }

    private static Calendar getDate(JSONObject ticketJson, String date, String time, String timeZone) {
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(SDF.parse(ticketJson.get(date) + "T" + ticketJson.get(time) + timeZone));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return calendar;
    }
}
