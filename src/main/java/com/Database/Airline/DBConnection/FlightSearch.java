package com.Database.Airline.DBConnection;

import com.Database.Airline.Objects.Flight;
import com.Database.Airline.Objects.FlightTables;
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
public class FlightSearch {

    @Autowired
    DataSource dataSource;

    public List<Flight> locateFlight(String airportId, String airportId_dest) throws SQLException {
        String sql = "Select * from flight Where airportId = ? and airportId_dest = ?";
        List<Flight>numFLight = new ArrayList<>();
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setString(1,airportId);
            preparedStatement.setString(2,airportId_dest);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    numFLight.add(new Flight(
                            resultSet.getString("flight_num"),
                            resultSet.getBoolean("isDomestic"),
                            resultSet.getTimestamp("depart_time").toLocalDateTime(),
                            resultSet.getTimestamp("arrival_time").toLocalDateTime(),
                            resultSet.getString("origin"),
                            resultSet.getString("destination"),
                            resultSet.getInt("num_Stops"),
                            resultSet.getString("airlineID"),
                            resultSet.getString("craft_num"),
                            resultSet.getString("airportId"),
                            resultSet.getString("airportId_dest"),
                            resultSet.getInt("airtime"),
                            resultSet.getFloat("price")));
                }
            }
        }
        return numFLight;
    }
    public FlightTables searchFlights(
            String flightType,
            String origin,
            String destination
    ) throws SQLException {
        List<Flight> outbound = locateFlight(origin, destination);

        if ("roundtrip".equalsIgnoreCase(flightType)) {
            List<Flight> inbound = locateFlight(destination, origin);
            return new FlightTables(outbound, inbound);
        } else {
            return new FlightTables(outbound, List.of());
        }
    }

}
