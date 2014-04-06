package database;

import database.executor.TExecutor;
import database.handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumDAO {
    public static Forum getForumById(Connection connection, Long id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<Forum> resultHandler = new TResultHandler<Forum>() {
            @Override
            public Forum handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new Forum(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getLong(4));
            }
        };
        return exec.execQuery(connection,"SELECT id, forumName, shortForumName, user_id from forum where id = " + id +";", resultHandler);
    }
    public static Forum getForumByShortForumName(Connection connection, String short_name) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<Forum> resultHandler = new TResultHandler<Forum>() {
            @Override
            public Forum handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new Forum(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getLong(4));
            }
        };
        System.out.println("SELECT id, forumName, shortForumName, user_id from forum where shortForumName = '" + short_name +"';");
        return exec.execQuery(connection,"SELECT id, forumName, shortForumName, user_id from forum where shortForumName = '" + short_name +"';", resultHandler);
    }
}
