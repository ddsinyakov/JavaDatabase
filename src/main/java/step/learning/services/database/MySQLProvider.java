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
    public Connection connect() throws SQLException {
        // access to database
        mysqlDriver = new Driver();
        DriverManager.registerDriver(mysqlDriver);

        // get database connection
        String connectionString = "jdbc:mysql://localhost:3306/javaTest?useUnicode=true&characterEncoding=UTF-8";
        connection = DriverManager.getConnection(connectionString, "dima", "qwerty");

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
