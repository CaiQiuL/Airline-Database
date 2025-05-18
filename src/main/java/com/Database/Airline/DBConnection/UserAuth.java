package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserAuth {

    @Autowired
    private DataSource dataSource;

    public User authenticate(String username, String password) {
        String sql = """
      SELECT u.userID,u.fname,u.lname,u.email,u.phone,
      (r.userID IS NOT NULL) AS isRep,
      (a.userID IS NOT NULL) AS isAdmin
      FROM users u
      LEFT JOIN customer_rep r ON u.userID = r.userID
      LEFT JOIN admin        a ON u.userID = a.userID
      WHERE u.username = ? AND u.password = ?
      """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return new User(
                    username,
                    password,
                    rs.getInt("userID"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getBoolean("isAdmin"),
                    rs.getBoolean("isRep")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
