package step.learning;

import step.learning.services.database.DataBaseProvider;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    private final DataBaseProvider provider;
    private Connection dbConnection;

    @Inject
    public App(DataBaseProvider provider) {
        this.provider = provider;
    }

    public void run() {

        dbConnection = provider.connect();

        // turns off program if connection didn't resolve
        if (dbConnection == null) {
            System.out.println("Some error occurred");
            return;
        }

        // sql query to create new table
        String sql = "CREATE TABLE IF NOT EXISTS randoms (" +
                "id BIGINT PRIMARY KEY," +
                "num INT NOT NULL," +
                "str VARCHAR(64) NULL" +
                ") Engine=InnoDB DEFAULT CHARSET = UTF8";

        // executeNoResult(sql);

        // execute query with no result
        try {
           Statement statement = dbConnection.createStatement();
           statement.executeUpdate(sql);
        }
        catch (SQLException ex) {
           System.out.println("Query error: " + ex.getMessage());
        }

        // query to insert some data into db table
        sql = "INSERT INTO randoms(id, num, str) VALUES" +
            "(1, 12, 'Twelve')," +
            "(2, 24, 'Twenty Four')," +
            "(3, 56, 'Fifty Six')";

        executeNoResult(sql);

        System.out.println("Query ok");

        // close connection with database
        provider.closeConnection();
    }

    /**
     * Method to resolve SQL queries
     * @param query String that contains query body
     */
    public void executeNoResult(String query) {
        try {
           Statement statement = dbConnection.createStatement();
           statement.executeUpdate(query);
        }
        catch (SQLException ex) {
           System.out.println("Query error: " + ex.getMessage());
        }
    }
}
