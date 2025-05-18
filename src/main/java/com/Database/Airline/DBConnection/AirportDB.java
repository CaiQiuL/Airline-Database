package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Airport;
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
public class AirportDB {
    @Autowired
    private DataSource ds;

    public List<Airport> findAll() throws SQLException {
        String sql = "SELECT airportId, airport_name FROM airport";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Airport> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Airport(
                        rs.getString("airportId"),
                        rs.getString("airport_name")
                ));
            }
            return list;
        }
    }

    public List<Airport> findAllGroped() throws SQLException {
        String sql = "SELECT airport_id, airport_name FROM airport ORDER BY airport_name";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Airport> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Airport(
                        rs.getString("airport_id"),
                        rs.getString("airport_name")
                ));
            }
            return list;
        }
    }


    public Airport findById(String airportId) throws SQLException {
        String sql = "SELECT airportId, airport_name FROM airport WHERE airportId = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, airportId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Airport(
                        rs.getString("airportId"),
                        rs.getString("airport_name")
                );
            }
        }
    }

    public boolean create(Airport a) throws SQLException {
        String sql = "INSERT INTO airport(airportId, airport_name) VALUES(?, ?)";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getAirportId());
            ps.setString(2, a.getAirportName());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(Airport a) throws SQLException {
        String sql = "UPDATE airport SET airport_name = ? WHERE airportId = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getAirportName());
            ps.setString(2, a.getAirportId());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(String airportId) throws SQLException {
        String sql = "DELETE FROM airport WHERE airportId = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, airportId);
            return ps.executeUpdate() == 1;
        }
    }
}
