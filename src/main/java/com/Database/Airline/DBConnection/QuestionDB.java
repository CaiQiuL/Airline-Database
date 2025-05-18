package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionDB {

    @Autowired
    private DataSource dataSource;

    public List<Questions> findAllQuestions() throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             ORDER BY timestamp DESC
        """;
        List<Questions> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Questions(
                        rs.getInt("messageID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getString("answer"),
                        rs.getString("question"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return out;
    }

    public List<Questions> findByCustomer(int customerId) throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             WHERE customerID = ?
             ORDER BY timestamp DESC
        """;
        List<Questions> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Questions(
                            rs.getInt("messageID"),
                            rs.getInt("customerID"),
                            rs.getInt("repID"),
                            rs.getString("answer"),
                            rs.getString("question"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    ));
                }
            }
        }
        return out;
    }

    public List<Questions> findUnanswered() throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             WHERE answer IS NULL
             ORDER BY timestamp ASC
        """;
        List<Questions> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Questions(
                        rs.getInt("messageID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getString("answer"),
                        rs.getString("question"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return out;
    }

    public void postQuestion(int customerId, String questionText) throws SQLException {
        String sql = "INSERT INTO questions (customerID, question, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setString(2, questionText);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

    public void answerQuestion(int messageID, int repID, String answerText) throws SQLException {
        String sql = "UPDATE questions SET answer = ?, repID = ? WHERE messageID = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, answerText);
            ps.setInt(2, repID);
            ps.setInt(3, messageID);
            ps.executeUpdate();
        }
    }
    public Questions findByMessageID(int messageID) throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             WHERE messageID = ?
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageID);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No question with ID=" + messageID);
                }
                return new Questions(
                        rs.getInt("messageID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getString("answer"),
                        rs.getString("question"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
            }
        }
    }

    public List<Questions> findAll() throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             ORDER BY timestamp DESC
        """;
        List<Questions> out = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Questions(
                        rs.getInt("messageID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getString("answer"),
                        rs.getString("question"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                ));
            }
        }
        return out;
    }

    public Questions findById(int messageID) throws SQLException {
        String sql = """
            SELECT messageID, customerID, repID, question, answer, timestamp
              FROM questions
             WHERE messageID = ?
        """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, messageID);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Questions(
                        rs.getInt("messageID"),
                        rs.getInt("customerID"),
                        rs.getInt("repID"),
                        rs.getString("answer"),
                        rs.getString("question"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
            }
        }
    }

    public boolean updateAnswer(int messageID, String answerText, int repID) throws SQLException {
        String sql = """
            UPDATE questions
               SET answer = ?,
                   repID  = ?
             WHERE messageID = ?
        """;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, answerText);
            ps.setInt   (2, repID);
            ps.setInt   (3, messageID);
            return ps.executeUpdate() == 1;
        }
    }
}
