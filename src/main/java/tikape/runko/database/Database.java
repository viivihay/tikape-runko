package tikape.runko.database;

import java.sql.*;
import java.util.*;


public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    
    
    //tähän jotakin fiksua miten tää saatais lisättyä tohon alla olevaan databaseen
    public void addToDatabase(String nimi) throws Exception{
        Connection connection = getConnection();
        
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO Opiskelija (nimi) VALUES ('"+nimi+"');");
        
    }

    
}
