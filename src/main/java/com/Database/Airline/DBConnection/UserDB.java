package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDB {
    @Autowired
    private DataSource ds;

    public List<User> findAll() throws SQLException {
        String sql = """
            SELECT 
              u.userID,
              u.username,
              u.fname,
              u.lname,
              u.email,
              u.phone,
              (r.userID IS NOT NULL) AS isRep,
              (a.userID IS NOT NULL) AS isAdmin
            FROM users u
            LEFT JOIN customer_rep r ON u.userID = r.userID
            LEFT JOIN admin a        ON u.userID = a.userID
            ORDER BY u.userID
        """;
        List<User> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new User(
                        rs.getString("username"),
                        null,
                        rs.getInt("userID"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("isAdmin"),
                        rs.getBoolean("isRep")
                ));
            }
        }
        return out;
    }

    public User findById(int userId) throws SQLException {
        String sql = """
            SELECT 
              u.userID,
              u.username,
              u.password,
              u.fname,
              u.lname,
              u.email,
              u.phone,
              (r.userID IS NOT NULL) AS isRep,
              (a.userID IS NOT NULL) AS isAdmin
            FROM users u
            LEFT JOIN customer_rep r ON u.userID = r.userID
            LEFT JOIN admin a        ON u.userID = a.userID
            WHERE u.userID = ?
        """;
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("userID"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("isAdmin"),
                        rs.getBoolean("isRep")
                );
            }
        }
    }

    public boolean create(User u, boolean isRep, boolean isAdmin) throws SQLException {
        String insertUser = """
            INSERT INTO users (username,password,fname,lname,email,phone,isRep,isAdmin)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getPassword());
                ps.setString(3, u.getFname());
                ps.setString(4, u.getLname());
                ps.setString(5, u.getEmail());
                ps.setString(6, u.getPhoneNum());
                ps.setBoolean(7, u.getRep());
                ps.setBoolean(8, u.getAdmin());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No userID generated");
                    int newId = keys.getInt(1);
                    if (isRep) {
                        try (PreparedStatement pr = c.prepareStatement(
                                "INSERT INTO customer_rep (userID) VALUES (?)")) {
                            pr.setInt(1, newId);
                            pr.executeUpdate();
                        }
                    }
                    if (isAdmin) {
                        try (PreparedStatement pa = c.prepareStatement(
                                "INSERT INTO admin (userID) VALUES (?)")) {
                            pa.setInt(1, newId);
                            pa.executeUpdate();
                        }
                    }
                }
            }
            c.commit();
            return true;
        }
    }

    public boolean update(User u, boolean isRep, boolean isAdmin) throws SQLException {
        String updateUser = """
            UPDATE users
               SET username = ?, password = ?, fname = ?, lname = ?, email = ?, phone = ?, isRep = ?, isAdmin = ?
             WHERE userID = ?
        """;
        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(updateUser)) {
                ps.setString(1, u.getUsername());
                ps.setString(2, u.getPassword());
                ps.setString(3, u.getFname());
                ps.setString(4, u.getLname());
                ps.setString(5, u.getEmail());
                ps.setString(6, u.getPhoneNum());
                ps.setBoolean(7, isRep);
                ps.setBoolean(8, isAdmin);
                ps.setInt(9, u.getUserID());
                ps.executeUpdate();
            }
            try (PreparedStatement del = c.prepareStatement(
                    "DELETE FROM customer_rep WHERE userID = ?")) {
                del.setInt(1, u.getUserID());
                del.executeUpdate();
            }
            if (isRep) {
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO customer_rep (userID) VALUES (?)")) {
                    ins.setInt(1, u.getUserID());
                    ins.executeUpdate();
                }
            }
            try (PreparedStatement del = c.prepareStatement(
                    "DELETE FROM admin WHERE userID = ?")) {
                del.setInt(1, u.getUserID());
                del.executeUpdate();
            }
            if (isAdmin) {
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO admin (userID) VALUES (?)")) {
                    ins.setInt(1, u.getUserID());
                    ins.executeUpdate();
                }
            }
            c.commit();
            return true;
        }
    }

    public boolean delete(int userId) throws SQLException {
        String deleteRep = "DELETE FROM customer_rep WHERE userID = ?";
        String deleteAdmin = "DELETE FROM admin        WHERE userID = ?";
        String deleteUser = "DELETE FROM users        WHERE userID = ?";

        try (Connection c = ds.getConnection()) {
            c.setAutoCommit(false);
            try (
                    PreparedStatement psRep = c.prepareStatement(deleteRep);
                    PreparedStatement psAdmin = c.prepareStatement(deleteAdmin);
                    PreparedStatement psUser = c.prepareStatement(deleteUser)
            ) {
                psRep.setInt(1, userId);
                psRep.executeUpdate();
                psAdmin.setInt(1, userId);
                psAdmin.executeUpdate();
                psUser.setInt(1, userId);
                int affected = psUser.executeUpdate();
                c.commit();
                return affected == 1;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            }
        }
    }
}
