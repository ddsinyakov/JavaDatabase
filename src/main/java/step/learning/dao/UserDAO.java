package step.learning.dao;

import step.learning.entities.User;
import step.learning.services.database.DataBaseProvider;
import step.learning.services.hash.HashService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
        // генерируем id для новой записи
        String id = UUID.randomUUID().toString() ;
        // готовим запрос (подстановка введенных данных!!)
        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`) VALUES(?,?,?,?)" ;

        try( PreparedStatement prep = connection.prepareStatement( sql ) ) {
            prep.setString(1, id);
            prep.setString(2, user.getLogin());
            prep.setString(3, hashService.hash(user.getPass()));
            prep.setString(4, user.getName());
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            System.out.println( ex.getMessage() ) ;
            return null ;
        }

        return id ;
    }
}
