package database;


import database.executor.TExecutor;
import database.handlers.TResultHandler;

import java.lang.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThreadDAO {
    public static database.Thread getThreadById(Connection connection,Long id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<database.Thread> resultHandler = new TResultHandler<database.Thread>() {
            @Override
            public database.Thread handle(ResultSet resultSet) throws SQLException {
                resultSet.next();
                return new database.Thread(resultSet.getLong(1),resultSet.getLong(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getBoolean(7),resultSet.getBoolean(8));
            }
        };
        return exec.execQuery(connection,"SELECT id, forum_id, title, DATE_FORMAT(date,'%Y-%m-%d %H:%i:%s'), message, slug, isDeleted, isClosed from thread where id ='" + id
                +"';", resultHandler);
    }
}
