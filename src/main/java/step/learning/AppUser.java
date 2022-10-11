package step.learning;

import step.learning.dao.UserDAO;
import step.learning.entities.User;
import step.learning.services.database.DataBaseProvider;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class AppUser {

    private final Connection connection;
    private final UserDAO userDAO;
    private final Scanner sc;

    @Inject
    public AppUser(DataBaseProvider provider,
                   UserDAO userDAO) {
        this.connection = provider.getConnection();
        this.userDAO = userDAO;
        sc = new Scanner(System.in);
    }

    public void run() {
        checkCreateTable();
        String addNew = "y";

         do {
            System.out.println("Do you want to add new User? y/n");
            addNew = sc.nextLine();

            if(addNew.equals("y")) {
                User newUser = new User();

                newUser.setLogin(askField("Login"));
                newUser.setName(askField("Name"));
                newUser.setPass(askField("Password"));

                userDAO.add(newUser);
            }
        }
        while (addNew.equals("y"));

    }

    private String askField(String fieldName) {
        String res = "";
        while (Objects.equals(res, "")) {
            System.out.println(fieldName + " -> ");
            res = sc.nextLine();

            if (Objects.equals(res, "")) {
                System.out.println(fieldName + " can't be empty");
            }
        }

        return res;
    }

    public void checkCreateTable() {
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
    }
}
