package step.learning.services.database;

import java.sql.Connection;

public interface DataBaseProvider {
    Connection connect();
    Connection getConnection();
    void closeConnection();
}
