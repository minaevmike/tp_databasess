package database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataService {
    private static Connection connection = null;
    public static Connection getConnection() {
        if(connection == null){
            try{
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

                StringBuilder url = new StringBuilder();
                url.
                        append("jdbc:mysql://").		//db type
                        append("localhost:"). 			//host name
                        append("3306/").				//port
                        append("forums?").			//db name
                        append("user=root&").	    //login
                        append("password=1");		//password

                connection = DriverManager.getConnection(url.toString());
            } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void connect()
    {
        connection = getConnection();
        System.out.append("Connected!\n");
        try {
            System.out.println("Autocommit: " + connection.getAutoCommit() + '\n');
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName() + '\n');
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion() + '\n');
            System.out.println("Driver: " + connection.getMetaData().getDriverName() + '\n');
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect()
    {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
