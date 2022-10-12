package step.learning.dao;

import step.learning.entities.User;
import step.learning.services.database.DataBaseProvider;
import step.learning.services.hash.HashService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Singleton
public class UserDAO {

    private final Connection connection ;
    private final HashService hashService;

    @Inject
    public UserDAO( DataBaseProvider provider,
                    HashService hashService) {
        this.connection = provider.getConnection();
        this.hashService = hashService;
    }

    /**
     * Inserts user in DB `Users` table
     * @param user data to insert
     * @return `id` of new record or null if fails
     */
    public String add( User user ) {

        // generate id for new entry
        String id = UUID.randomUUID().toString() ;

        String salt = hashService.hash(UUID.randomUUID().toString());

        String passHash = this.hashPassword(user.getPass(), salt);

        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`, `salt`) VALUES(?,?,?,?,?)" ;

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString(1, id);
            prep.setString(2, user.getLogin());
            prep.setString(3, passHash);
            prep.setString(4, user.getName());
            prep.setString(5, salt);
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }

        return id ;
    }


    /**
     * Checks User table for login given
     * @param login value to look for
     * @return true if login is in table
     */
    public boolean isLoginUsed(String login) {

        // check if user with the same login already exist
        String sql = "SELECT COUNT(U.`id`) FROM Users AS U WHERE U.`login` = ?";

        try (PreparedStatement prep = connection.prepareStatement( sql )) {
            prep.setString(1, login);
            ResultSet res = prep.executeQuery();
            res.next();

            return res.getInt(1) > 0;
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return true;
        }
    }


    /**
     * Calculates hash (optionally salt) from password
     * @param password Password string
     * @return hash
     */
    public String hashPassword(String password, String salt) {
        return hashService.hash(password + salt);
    }

    public String hashPassword(String password) {
        return hashService.hash(password);
    }


    /**
     * Finds user with given credentials
     * @param login user login
     * @param pass user password
     * @return User or null
     */
    public User getUserByCredentials(String login, String pass) {

        String sql = "SELECT U.* FROM Users U WHERE `login` = ?";

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString(1, login);
            ResultSet res = prep.executeQuery();

            if(res.next()) {
                User user = new User(res);

                if (user.getSalt() == null && hashPassword(pass).equals(user.getPass()))
                    return user;

                String expectedHash = this.hashPassword(pass, user.getSalt());
                if(expectedHash.equals(user.getPass())) {
                    return user;
                }
            }
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
        }

        return null;
    }
}
