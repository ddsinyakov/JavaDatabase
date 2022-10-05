package step.learning;

import step.learning.services.database.DataBaseProvider;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    private final DataBaseProvider provider;

    @Inject
    public App(DataBaseProvider provider) {
        this.provider = provider;
    }

    public void run() {

        Connection dbConnection = provider.connect();

        String sql = "CREATE TABLE IF NOT EXISTS randoms (" +
                "id BIGINT PRIMARY KEY," +
                "num INT NOT NULL," +
                "str VARCHAR(64) NULL" +
                ") Engine=InnoDB DEFAULT CHARSET = UTF8";

        try {
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException ex) {
            System.out.println("Query error: " + ex.getMessage());
            return;
        }

        System.out.println("Query ok");

        provider.closeConnection();
    }
}
