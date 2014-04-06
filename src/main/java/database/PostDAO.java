package database;

import database.executor.TExecutor;
import database.handlers.TResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by Mike on 05.04.14.
 */
public class PostDAO {
    public static Post getPostById(Connection connection,Long id) throws SQLException{
        TExecutor exec = new TExecutor();
        TResultHandler<Post> resultHandler = new TResultHandler<Post>() {
            @Override
            public Post handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new Post(resultSet.getLong(1),resultSet.getLong(2),resultSet.getBoolean(3),resultSet.getBoolean(4),resultSet.getBoolean(5),
                        resultSet.getBoolean(6),resultSet.getBoolean(7),resultSet.getString(8),resultSet.getLong(9),resultSet.getString(10),resultSet.getLong(11),resultSet.getString(12));
            }
        };
        return exec.execQuery(connection,"SELECT id, parent, isApproved, isHighlighted, isEdited, isSpam, isDeleted, DATE_FORMAT(date,'%Y-%m-%d %H:%i:%s'), thread_id, message,user_id,forum from post where id = '" + id
                +"';", resultHandler);
    }
}
