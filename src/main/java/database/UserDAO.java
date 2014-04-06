package database;

import database.executor.TExecutor;
import database.handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mike on 17.03.14.
 */
public class UserDAO {
    public static User getByEmail(Connection connection, String email) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<User> resultHandler = new TResultHandler<User>() {
            @Override
            public User handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new User(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getBoolean(6));
            }
        };
        return exec.execQuery(connection,"SELECT id, username, email, name, about, isAnonymous from user where email = '" + email
        +"';", resultHandler);

    }
    public static User getById(Connection connection, Long id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<User> resultHandler = new TResultHandler<User>() {
            @Override
            public User handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new User(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getBoolean(6));
            }
        };
        return exec.execQuery(connection,"SELECT id, username, email, name, about, isAnonymous from user where id = '" + id
                +"';", resultHandler);

    }
}
