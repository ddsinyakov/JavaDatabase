package step.learning;

import com.google.inject.Guice;

public class Main {
    public static void main(String[] args) {
        try(ConfigModule configModule = new ConfigModule()) {
            Guice.createInjector(configModule)
                    .getInstance(AppUser.class)
                    .run();
        }
    }
}