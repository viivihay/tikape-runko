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

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("CREATE TABLE Keskustelu (id integer PRIMARY KEY, nimi varchar(255));");
        
        return lista;
    }

    public void addToDatabase(String nimi) throws Exception {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO Opiskelija (nimi) VALUES ('" + nimi + "');");
    }
    public void addToKeskustelu(String viesti, int id) throws Exception {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO Keskustelu (nimi) VALUES ('" + viesti + "');");
    }

}
