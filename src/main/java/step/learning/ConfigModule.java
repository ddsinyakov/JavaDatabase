package step.learning;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import step.learning.services.database.DataBaseProvider;
import step.learning.services.database.MySQLProvider;
import step.learning.services.hash.HashService;
import step.learning.services.hash.Sha1HashService;

import java.sql.SQLException;

public class ConfigModule
        extends AbstractModule
        implements AutoCloseable{
    @Override
    protected void configure() {
        bind(HashService.class)
                .to(Sha1HashService.class);
    }

    MySQLProvider dbProvider;

    @Provides
    public DataBaseProvider getDataBaseProvider() throws SQLException {
        if (dbProvider == null) {
            dbProvider = new MySQLProvider();
            dbProvider.connect();
        }

        return dbProvider;
    }

    public void close() {
        dbProvider.closeConnection();
        System.out.println("Connection closed.");
    }
}
