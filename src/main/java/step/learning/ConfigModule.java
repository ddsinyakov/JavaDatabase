package step.learning;

import com.google.inject.AbstractModule;
import step.learning.services.database.DataBaseProvider;
import step.learning.services.database.MySQLProvider;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataBaseProvider.class)
                .to(MySQLProvider.class);
    }
}
