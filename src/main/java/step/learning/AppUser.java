package step.learning;

import step.learning.dao.UserDAO;
import step.learning.services.database.DataBaseProvider;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AppUser {

    private final Connection connection;
    private final UserDAO userDAO;

    @Inject
    public AppUser(DataBaseProvider provider,
                   UserDAO userDAO) {
        this.connection = provider.getConnection();
        this.userDAO = userDAO;
    }

    public void run() {
        String sql = "CREATE TABLE  IF NOT EXISTS  Users (" +
                "    `id`    CHAR(36)     NOT NULL   COMMENT 'UUID'," +
                "    `login` VARCHAR(32)  NOT NULL," +
                "    `pass`  CHAR(40)     NOT NULL   COMMENT 'SHA-160 hash'," +
                "    `name`  TINYTEXT     NOT NULL," +
                "    PRIMARY KEY(id)" +
                " ) Engine=InnoDB  DEFAULT CHARSET = UTF8" ;

        try( Statement statement = connection.createStatement() ) {
            statement.executeUpdate( sql ) ;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            System.out.println( sql ) ;
            return ;
        }

//        userDAO.add(new User()
//                .setName("Dima")
//                .setLogin("Siinyakov")
//                .setPass("qwerty123"));
    }
}
