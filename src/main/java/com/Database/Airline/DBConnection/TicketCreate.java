package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Flight;
import com.Database.Airline.Objects.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class TicketCreate {

    @Autowired
    DataSource dataSource;
    @Autowired
    FlightSearch flightSearch;

    private static final float BOOKING_FEE_RATE = 0.10f;

    public List<Ticket> bookFlights(
            int userId,
            String flightType,
            String outboundFlight,
            String returnFlight,
            String flightClass
    ) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(createSingleTicket(userId, outboundFlight, flightClass, "outbound"));
        if ("roundtrip".equalsIgnoreCase(flightType) && returnFlight != null) {
            tickets.add(createSingleTicket(userId, returnFlight, flightClass, "return"));
        }
        populateWaitlist();
        return tickets;
    }

    private Ticket createSingleTicket(
            int userId,
            String flightNum,
            String flightClass,
            String check
    ) throws SQLException {
        Flight f = getFlightByNumber(flightNum);
        if (f == null) {
            throw new SQLException("Flight not found: " + flightNum);
        }
        float price      = f.getPrice();
        float bookingFee = price * BOOKING_FEE_RATE;
        float total      = price + bookingFee;
        int seatNumber = ThreadLocalRandom.current().nextInt(1, 101);
        String sql = "INSERT INTO ticket " +
                "(userID, flight_num, class, purchase_time, price, bookingFee, total, goingTo, comingFrom, seat_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedKey;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt     (1, userId);
            ps.setString  (2, flightNum);
            ps.setString  (3, flightClass);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setFloat   (5, price);
            ps.setFloat   (6, bookingFee);
            ps.setFloat   (7, total);
            ps.setString  (8, f.getDestination());
            ps.setString  (9, f.getOrigin());
            ps.setInt     (10, seatNumber);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedKey = keys.getInt(1);
                } else {
                    throw new SQLException("Failed to obtain ticket_num");
                }
            }
        }

        return new Ticket(
                generatedKey,
                userId,
                flightNum,
                seatNumber,
                flightClass,
                bookingFee,
                total,
                LocalDateTime.now(),
                f.getDestination(),
                f.getOrigin()
        );
    }

    private Flight getFlightByNumber(String flightNum) throws SQLException {
        String sql = "SELECT * FROM flight WHERE flight_num = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Flight(
                        rs.getString("flight_num"),
                        rs.getBoolean("isDomestic"),
                        rs.getTimestamp("depart_time").toLocalDateTime(),
                        rs.getTimestamp("arrival_time").toLocalDateTime(),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt("num_Stops"),
                        rs.getString("airlineID"),
                        rs.getString("craft_num"),
                        rs.getString("airportId"),
                        rs.getString("airportId_dest"),
                        rs.getInt("airtime"),
                        rs.getFloat("price")
                );
            }
        }
    }
    private void populateWaitlist() throws SQLException {
        String sql = """
      INSERT INTO waitlist (userID, flight_num, timestamp)
      SELECT x.userID, x.flight_num, NOW()
      FROM (
        SELECT
          t.userID,
          t.flight_num,
          ROW_NUMBER() OVER (
            PARTITION BY t.flight_num
            ORDER BY t.purchase_time
          ) AS rn,
          a.seats AS capacity
        FROM ticket   t
        JOIN flight   f ON t.flight_num = f.flight_num
        JOIN aircraft a ON f.craft_num  = a.craft_num
      ) AS x
      WHERE x.rn > x.capacity
    """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
}
