package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightDB {
    @Autowired private DataSource ds;

    public List<Flight> findAll() throws SQLException {
        String sql = """
            SELECT flight_num,isDomestic,arrival_time,depart_time,
                   origin,destination,num_Stops,airlineID,
                   craft_num,airportId,airportId_dest,
                   airtime,price
              FROM flight
             ORDER BY depart_time
        """;
        var out = new ArrayList<Flight>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Flight(
                        rs.getString("flight_num"),
                        rs.getBoolean("isDomestic"),
                        rs.getTimestamp("arrival_time").toLocalDateTime(),
                        rs.getTimestamp("depart_time" ).toLocalDateTime(),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt   ("num_Stops"),
                        rs.getString("airlineID"),
                        rs.getString("craft_num"),
                        rs.getString("airportId"),
                        rs.getString("airportId_dest"),
                        rs.getInt   ("airtime"),
                        rs.getFloat ("price")
                ));
            }
        }
        return out;
    }

    public Flight findById(String flightNum) throws SQLException {
        String sql = """
            SELECT flight_num,isDomestic,arrival_time,depart_time,
                   origin,destination,num_Stops,airlineID,
                   craft_num,airportId,airportId_dest,
                   airtime,price
              FROM flight
             WHERE flight_num = ?
        """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Flight(
                        rs.getString("flight_num"),
                        rs.getBoolean("isDomestic"),
                        rs.getTimestamp("arrival_time").toLocalDateTime(),
                        rs.getTimestamp("depart_time" ).toLocalDateTime(),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt   ("num_Stops"),
                        rs.getString("airlineID"),
                        rs.getString("craft_num"),
                        rs.getString("airportId"),
                        rs.getString("airportId_dest"),
                        rs.getInt   ("airtime"),
                        rs.getFloat ("price")
                );
            }
        }
    }

    public boolean create(Flight f) throws SQLException {
        String sql = """
            INSERT INTO flight (
              flight_num,
              isDomestic,
              arrival_time,
              depart_time,
              origin,
              destination,
              num_Stops,
              airlineID,
              craft_num,
              airportId,
              airportId_dest,
              price
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString (1, f.getFlightNum());
            ps.setBoolean(2, f.getisDomestic());
            ps.setTimestamp(3, Timestamp.valueOf(f.getArrivalTime()));
            ps.setTimestamp(4, Timestamp.valueOf(f.getDepartTime()));
            ps.setString (5, f.getOrigin());
            ps.setString (6, f.getDestination());
            ps.setInt    (7, f.getNumStops());
            ps.setString (8, f.getAirlineID());
            ps.setString (9, f.getCraftNum());
            ps.setString (10, f.getAirportId());
            ps.setString (11, f.getAirportIdDest());
            ps.setFloat  (12, f.getPrice());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Flight f) throws SQLException {
        String sql = """
            UPDATE flight SET
              isDomestic     = ?,
              arrival_time   = ?,
              depart_time    = ?,
              origin         = ?,
              destination    = ?,
              num_Stops      = ?,
              airlineID      = ?,
              craft_num      = ?,
              airportId      = ?,
              airportId_dest = ?,
              price          = ?
            WHERE flight_num = ?
        """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, f.getisDomestic());
            ps.setTimestamp(2, Timestamp.valueOf(f.getArrivalTime()));
            ps.setTimestamp(3, Timestamp.valueOf(f.getDepartTime()));
            ps.setString (4, f.getOrigin());
            ps.setString (5, f.getDestination());
            ps.setInt    (6, f.getNumStops());
            ps.setString (7, f.getAirlineID());
            ps.setString (8, f.getCraftNum());
            ps.setString (9, f.getAirportId());
            ps.setString (10, f.getAirportIdDest());
            ps.setFloat  (11, f.getPrice());
            ps.setString (12, f.getFlightNum());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(String flightNum) throws SQLException {
        String sql = "DELETE FROM flight WHERE flight_num = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, flightNum);
            return ps.executeUpdate()==1;
        }
    }
    public List<Flight> findByOrigin(String airportId) throws SQLException {
        String sql = """
      SELECT flight_num, isDomestic, arrival_time, depart_time,
             origin, destination, num_Stops, airlineID,
             craft_num, airportId, airportId_dest, airtime, price
      FROM flight
      WHERE airportId = ?
      ORDER BY depart_time
    """;
        List<Flight> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, airportId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRowToFlight(rs));
                }
            }
        }
        return out;
    }

    public List<Flight> findByDestination(String airportIdDest) throws SQLException {
        String sql = """
      SELECT flight_num, isDomestic, arrival_time, depart_time,
             origin, destination, num_Stops, airlineID,
             craft_num, airportId, airportId_dest, airtime, price
      FROM flight
      WHERE airportId_dest = ?
      ORDER BY arrival_time
    """;
        List<Flight> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, airportIdDest);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(mapRowToFlight(rs));
                }
            }
        }
        return out;
    }

    private Flight mapRowToFlight(ResultSet rs) throws SQLException {
        return new Flight(
                rs.getString("flight_num"),
                rs.getBoolean("isDomestic"),
                rs.getTimestamp("arrival_time").toLocalDateTime(),
                rs.getTimestamp("depart_time").toLocalDateTime(),
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
