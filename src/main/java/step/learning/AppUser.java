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
        menu();
    }

    private void menu() {
        String ans = "";

        while(!ans.equals("0")) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            ans = sc.nextLine();

            switch (ans) {
                case "1" -> regUser();
                case "2" -> authUser();
            }
        }
    }

    private void authUser() {
        System.out.print("Login -> ");
        String login = sc.nextLine();
        System.out.print("Password -> ");
        String pass = sc.nextLine();

        User res = userDAO.getUserByCredentials(login, pass);

        if (res == null) {
            System.out.println("ACCESS DENIED");
            return;
        }

        System.out.println("Hello, " + res.getName());
    }

    public void regUser() {
        User newUser = new User();

        // region login

        while (true) {
            System.out.print("Login -> ");
            newUser.setLogin(sc.nextLine());

            if (Objects.equals(newUser.getLogin(), "")) {
                System.out.println("Login can't be empty");
                continue;
            }

            if (userDAO.isLoginUsed(newUser.getLogin())) {
                System.out.println("Current login already exists");
                continue;
            }

            break;
        }

        // endregion

        // region name

        while (true) {
            System.out.print("Name -> ");
            newUser.setName(sc.nextLine());

            if (Objects.equals(newUser.getName(), "")) {
                System.out.println("Name can't be empty");
                continue;
            }

            break;
        }

        // endregion

        // region password

        while (true) {
            System.out.print("Password -> ");
            newUser.setPass(sc.nextLine());

            if (Objects.equals(newUser.getPass(), "")) {
                System.out.println("Password can't be empty");
                continue;
            }

            System.out.print("Repeat password -> ");
            String repeatPassword = sc.nextLine();

            if (!repeatPassword.equals(newUser.getPass())) {
                System.out.println("Passwords are different");
                continue;
            }

            break;
        }

        // endregion

        userDAO.add(newUser);
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
