package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Aircraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AircraftDB {
    @Autowired
    DataSource ds;

    public List<Aircraft> findAll() throws SQLException {
        String sql = "SELECT craft_num, operation_days, seats, airlineID FROM aircraft";
        try (var c = ds.getConnection();
             var ps = c.prepareStatement(sql);
             var rs = ps.executeQuery()) {
            var list = new ArrayList<Aircraft>();
            while (rs.next()) {
                list.add(new Aircraft(
                        rs.getString("craft_num"),
                        rs.getString("operation_days"),
                        rs.getInt   ("seats"),
                        rs.getString("airlineID")
                ));
            }
            return list;
        }
    }

    public Aircraft findById(String craftNum) throws SQLException {
        String sql = "SELECT craft_num, operation_days, seats, airlineID FROM aircraft WHERE craft_num=?";
        try (var c = ds.getConnection();
             var ps = c.prepareStatement(sql)) {
            ps.setString(1, craftNum);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Aircraft(
                        rs.getString("craft_num"),
                        rs.getString("operation_days"),
                        rs.getInt   ("seats"),
                        rs.getString("airlineID")
                );
            }
        }
    }

    public boolean create(Aircraft a) throws SQLException {
        String sql = "INSERT INTO aircraft(craft_num,operation_days,seats,airlineID) VALUES(?,?,?,?)";
        try (var c = ds.getConnection();
             var ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getCraftNum());
            ps.setString(2, a.getOperationDays());
            ps.setInt   (3, a.getSeats());
            ps.setString(4, a.getAirlineID());
            return ps.executeUpdate()==1;
        }
    }

    public boolean update(Aircraft a) throws SQLException {
        String sql = "UPDATE aircraft SET operation_days=?, seats=?, airlineID=? WHERE craft_num=?";
        try (var c = ds.getConnection();
             var ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getOperationDays());
            ps.setInt   (2, a.getSeats());
            ps.setString(3, a.getAirlineID());
            ps.setString(4, a.getCraftNum());
            return ps.executeUpdate()==1;
        }
    }

    public boolean delete(String craftNum) throws SQLException {
        String sql = "DELETE FROM aircraft WHERE craft_num=?";
        try (var c = ds.getConnection();
             var ps = c.prepareStatement(sql)) {
            ps.setString(1, craftNum);
            return ps.executeUpdate()==1;
        }
    }
}
