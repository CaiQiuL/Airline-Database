package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.MonthlySales;
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
public class SalesDB {
    @Autowired
    private DataSource ds;

    public List<MonthlySales> getMonthlySales() throws SQLException {
        String sql = """
            SELECT 
              YEAR(purchase_time) AS yr,
              MONTH(purchase_time) AS mo,
              SUM(total)           AS sales
            FROM ticket
            GROUP BY YEAR(purchase_time), MONTH(purchase_time)
            ORDER BY YEAR(purchase_time), MONTH(purchase_time)
        """;
        List<MonthlySales> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new MonthlySales(
                        rs.getInt   ("yr"),
                        rs.getInt   ("mo"),
                        rs.getFloat("sales")
                ));
            }
        }
        return out;
    }
}
