package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.CustomerRevenue;
import com.Database.Airline.Objects.FlightActivity;
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
public class ReportDB {
    @Autowired
    private DataSource ds;

    public double revenueByFlight(String flightNum) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total),0) AS rev FROM ticket WHERE flight_num = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("rev") : 0.0;
            }
        }
    }

    public double revenueByAirline(String airlineId) throws SQLException {
        String sql = """
                    SELECT COALESCE(SUM(t.total),0) AS rev
                      FROM ticket t
                      JOIN flight f ON t.flight_num = f.flight_num
                     WHERE f.airlineID = ?
                """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, airlineId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("rev") : 0.0;
            }
        }
    }

    public double revenueByCustomer(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total),0) AS rev FROM ticket WHERE userID = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("rev") : 0.0;
            }
        }
    }

    public CustomerRevenue findTopCustomer() throws SQLException {
        String sql = """
                  SELECT
                    u.userID,
                    u.fname,
                    u.lname,
                    SUM(t.total) AS totalRev
                  FROM ticket t
                  JOIN users u ON t.userID = u.userID
                  GROUP BY u.userID, u.fname, u.lname
                  ORDER BY totalRev DESC
                  LIMIT 1
                """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            return new CustomerRevenue(
                    rs.getInt("userID"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    rs.getFloat("totalRev")
            );
        }
    }

    public List<FlightActivity> findMostActiveFlights(int topN) throws SQLException {
        String sql = """
            SELECT flight_num,
                   COUNT(*) AS ticketsSold
              FROM ticket
             GROUP BY flight_num
             ORDER BY ticketsSold DESC
             LIMIT ?
        """;
        List<FlightActivity> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, topN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new FlightActivity(
                            rs.getString("flight_num"),
                            rs.getInt("ticketsSold")
                    ));
                }
            }
        }
        return out;
    }
}
