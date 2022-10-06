package step.learning;

import step.learning.services.database.DataBaseProvider;

import javax.inject.Inject;
import java.sql.*;
import java.util.Random;

public class App {

    private final DataBaseProvider provider;
    private Connection dbConnection;

    @Inject
    public App(DataBaseProvider provider) {
        this.provider = provider;
    }

    public void run() {
        String sql;

        // region create connection

        dbConnection = provider.connect();

        // turns off program if connection didn't resolve
        if (dbConnection == null) {
            System.out.println("Some error occurred");
            return;
        }

        // endregion

        // region create table

        // sql query to create new table
//        sql = "CREATE TABLE IF NOT EXISTS randoms (" +
//                "id BIGINT PRIMARY KEY," +
//                "num INT NOT NULL," +
//                "str VARCHAR(64) NULL" +
//                ") Engine=InnoDB DEFAULT CHARSET = UTF8";
//
//        executeNoResult(sql);

        // endregion

        // region insert data

        Random rnd = new Random();
        int randInt = rnd.nextInt();
        String randStr = "Str " + randInt;

        // query to insert some data into db table
//        sql = String.format(
//                "INSERT INTO randoms VALUES" +
//                "(UUID_SHORT(), %d, '%s')", randInt, randStr);
//
//        executeNoResult(sql);

        // endregion

        // region insert with prepared statement

        // to be safe of sql injection
//        sql = "INSERT INTO randoms VALUES (UUID_SHORT(), ?, ?);";
//
//        try (PreparedStatement prep = dbConnection.prepareStatement(sql)) {
//            for(int i = 0; i < 10; i++ ){
//                randInt = rnd.nextInt();
//                randStr = "Str " + randInt;
//
//                prep.setInt(1, randInt);
//                prep.setString(2, randStr);
//
//                prep.executeUpdate();
//            }
//        }
//        catch (SQLException ex) {
//            System.out.println("Query error: " + ex.getMessage());
//        }

        // endregion

        // region select

//        sql = "SELECT r.id, r.num, r.str FROM randoms AS r";
//
//        try (Statement statement = dbConnection.createStatement()) {
//            ResultSet res = statement.executeQuery(sql);
//
//            while (res.next()) {
//                System.out.printf("%d; %d; %s; %n",
//                        res.getLong(1),
//                        res.getLong("num"),
//                        res.getString(3));
//            }
//        }
//        catch (SQLException ex) {
//            System.out.println("Query error: " + ex.getMessage());
//        }

        // endregion

        // region select

        System.out.println("Positive:");
        sql = "SELECT r.id, r.num, r.str FROM randoms AS r WHERE r.num > 0";
        showTable(sql, ConsoleColors.GREEN);

        System.out.println("Negative:");
        sql = "SELECT r.id, r.num, r.str FROM randoms AS r WHERE r.num < 0";
        showTable(sql, ConsoleColors.RED);

        // endregion

        // close connection with database
        provider.closeConnection();
    }

    /**
     * Method to resolve SQL queries
     * @param query String that contains query body
     */
    public void executeNoResult(String query) {
        try (Statement statement = dbConnection.createStatement()) {
           statement.executeUpdate(query);
        }
        catch (SQLException ex) {
           System.out.println("Query error: " + ex.getMessage());
        }
    }

    public void showTable(String sql, String color) {
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet res = statement.executeQuery(sql);

            while (res.next()) {
                System.out.printf("%d; " + color + "%d; " + ConsoleColors.RESET + "%s; %n",
                        res.getLong(1),
                        res.getLong("num"),
                        res.getString(3));
            }
        }
        catch (SQLException ex) {
            System.out.println("Query error: " + ex.getMessage());
        }
    }
}
