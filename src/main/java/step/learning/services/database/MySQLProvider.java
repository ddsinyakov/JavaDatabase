package step.learning.services.database;

import com.mysql.cj.jdbc.Driver;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class MySQLProvider
        implements DataBaseProvider{

    private Connection connection;
    private Driver mysqlDriver;

    @Override
    public Connection connect() {


        // access to database
        try {
            mysqlDriver = new Driver();
            DriverManager.registerDriver(mysqlDriver);
        }
        catch (SQLException ex) {
            System.out.println("Driver ini error: " + ex.getMessage());
            return null;
        }

        // get database connection
        String connectionString = "jdbc:mysql://localhost:3306/javaTest?useUnicode=true&characterEncoding=UTF-8";
        try { connection = DriverManager.getConnection(connectionString, "dima", "qwerty"); }
        catch (SQLException ex) {
            System.out.println("DB Connection error: " + ex.getMessage());
            return null;
        }

        return connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void closeConnection() {
        // close existing connection
        try {
            DriverManager.deregisterDriver(mysqlDriver);
            connection.close();
        }
        catch (SQLException ignored) { }
    }
}
