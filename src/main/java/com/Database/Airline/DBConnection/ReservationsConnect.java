package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Ticket;
import com.Database.Airline.Objects.Waitlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationsConnect {
    @Autowired
    private DataSource dataSource;

    public List<Ticket> findByUser(int userId) throws SQLException {
        String sql = """
                    SELECT 
                      ticket_num,
                      userID,
                      flight_num,
                      seat_number,
                      class       AS Uclass,
                      bookingFee,
                      total,
                      purchase_time,
                      goingTo,
                      comingFrom
                    FROM ticket
                    WHERE userID = ?
                    ORDER BY purchase_time DESC
                """;

        List<Ticket> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Ticket(
                            rs.getInt("ticket_num"),
                            rs.getInt("userID"),
                            rs.getString("flight_num"),
                            rs.getInt("seat_number"),
                            rs.getString("Uclass"),
                            rs.getFloat("bookingFee"),
                            rs.getFloat("total"),
                            rs.getTimestamp("purchase_time").toLocalDateTime(),
                            rs.getString("goingTo"),
                            rs.getString("comingFrom")
                    ));
                }
            }
        }
        return out;
    }

    public List<Waitlist> findWaitlistByUser(int userId) throws SQLException {
        String sql = """
                  SELECT 
                    waitlist_id,
                    userID,
                    flight_num,
                    timestamp
                  FROM waitlist
                  WHERE userID = ?
                  ORDER BY timestamp DESC
                """;

        List<Waitlist> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Waitlist(
                            rs.getInt("waitlist_id"),
                            rs.getInt("userID"),
                            rs.getString("flight_num"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    ));
                }
            }
        }
        return out;
    }

    public boolean cancelTicket(int ticketNum) throws SQLException {
        String sql = "DELETE FROM ticket WHERE ticket_num = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketNum);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateReservation(int ticketNum,
                                     String newFlightNum,
                                     String newClass) throws SQLException {
        String sql = "UPDATE ticket " +
                "SET flight_num = ?, class = ? " +
                "WHERE ticket_num = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newFlightNum);
            ps.setString(2, newClass);
            ps.setInt(3, ticketNum);
            return ps.executeUpdate() == 1;
        }
    }

    public Ticket findByTicketNum(int ticketNum) throws SQLException {
        String sql = """
                  SELECT 
                    ticket_num,
                    userID,
                    flight_num,
                    seat_number,
                    class       AS Uclass,
                    bookingFee,
                    total,
                    purchase_time,
                    goingTo,
                    comingFrom
                  FROM ticket
                  WHERE ticket_num = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Ticket(
                        rs.getInt("ticket_num"),
                        rs.getInt("userID"),
                        rs.getString("flight_num"),
                        rs.getInt("seat_number"),
                        rs.getString("Uclass"),
                        rs.getFloat("bookingFee"),
                        rs.getFloat("total"),
                        rs.getTimestamp("purchase_time").toLocalDateTime(),
                        rs.getString("goingTo"),
                        rs.getString("comingFrom")
                );
            }
        }
    }

    public List<Ticket> findAllTickets() throws SQLException {
        String sql = """
                  SELECT 
                    ticket_num,
                    userID,
                    flight_num,
                    seat_number,
                    class       AS Uclass,
                    bookingFee,
                    total,
                    purchase_time,
                    goingTo,
                    comingFrom
                  FROM ticket
                  ORDER BY purchase_time DESC
                """;
        List<Ticket> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Ticket(
                        rs.getInt("ticket_num"),
                        rs.getInt("userID"),
                        rs.getString("flight_num"),
                        rs.getInt("seat_number"),
                        rs.getString("Uclass"),
                        rs.getFloat("bookingFee"),
                        rs.getFloat("total"),
                        rs.getTimestamp("purchase_time").toLocalDateTime(),
                        rs.getString("goingTo"),
                        rs.getString("comingFrom")
                ));
            }
        }
        return out;
    }
    public List<Waitlist> findWaitlistByFlight(String flightNum) throws SQLException {
        String sql = """
            SELECT waitlist_id, userID, flight_num, timestamp
              FROM waitlist
             WHERE flight_num = ?
             ORDER BY timestamp
        """;

        List<Waitlist> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Waitlist(
                            rs.getInt("waitlist_id"),
                            rs.getInt("userID"),
                            rs.getString("flight_num"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    ));
                }
            }
        }
        return out;
    }

    public List<Ticket> findByFlightNum(String flightNum) throws SQLException {
        String sql = """
            SELECT 
              t.ticket_num,
              t.userID,
              t.flight_num,
              t.seat_number,
              t.class      AS Uclass,
              t.bookingFee,
              t.total,
              t.purchase_time,
              t.goingTo,
              t.comingFrom,
              u.fname,
              u.lname
            FROM ticket t
            JOIN users u ON t.userID = u.userID
            WHERE t.flight_num = ?
            ORDER BY t.purchase_time
        """;
        List<Ticket> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket tk = new Ticket(
                            rs.getInt("ticket_num"),
                            rs.getInt("userID"),
                            rs.getString("flight_num"),
                            rs.getInt("seat_number"),
                            rs.getString("Uclass"),
                            rs.getFloat("bookingFee"),
                            rs.getFloat("total"),
                            rs.getTimestamp("purchase_time").toLocalDateTime(),
                            rs.getString("goingTo"),
                            rs.getString("comingFrom")
                    );
                    tk.setGoingTo(rs.getString("fname") + " " + rs.getString("lname"));
                    out.add(tk);
                }
            }
        }
        return out;
    }

    public List<Ticket> findByCustomerName(String name) throws SQLException {
        String sql = """
            SELECT 
              t.ticket_num,
              t.userID,
              t.flight_num,
              t.seat_number,
              t.class      AS Uclass,
              t.bookingFee,
              t.total,
              t.purchase_time,
              t.goingTo,
              t.comingFrom,
              u.fname,
              u.lname
            FROM ticket t
            JOIN users u ON t.userID = u.userID
            WHERE u.fname LIKE ? OR u.lname LIKE ?
            ORDER BY t.purchase_time
        """;
        List<Ticket> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String wildcard = "%" + name + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket tk = new Ticket(
                            rs.getInt("ticket_num"),
                            rs.getInt("userID"),
                            rs.getString("flight_num"),
                            rs.getInt("seat_number"),
                            rs.getString("Uclass"),
                            rs.getFloat("bookingFee"),
                            rs.getFloat("total"),
                            rs.getTimestamp("purchase_time").toLocalDateTime(),
                            rs.getString("goingTo"),
                            rs.getString("comingFrom")
                    );
                    tk.setGoingTo(rs.getString("fname") + " " + rs.getString("lname"));
                    out.add(tk);
                }
            }
        }
        return out;
    }
}
