package frontend;



import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import database.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

import orm.Functions;
import orm.User;


public class Frontend extends HttpServlet {

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String param = request.getPathInfo();
        String [] params = param.split("/");
        Connection connection = DataService.getConnection();
        System.out.println(param);
        switch (params[3]){
            case "thread":{
               switch (params[4]){
                    case "create":{
                        orm.Thread.create(response,request,connection);
                        break;
                    }
                   case "close":{
                       orm.Thread.close(response,request,connection);
                       break;
                   }
                   case "open":{
                       orm.Thread.open(response,request,connection);
                       break;
                   }
                   case "remove":{
                       orm.Thread.remove(response,request,connection);
                       break;
                   }
                   case "vote":{
                       orm.Thread.vote(response,request,connection);
                       break;
                   }
                   case "restore":{
                       orm.Thread.restore(response,request,connection);
                       break;
                   }
                   case "subscribe":{
                       orm.Thread.subscribe(response,request,connection);
                       break;
                   }
                   case "unsubscribe":{
                       orm.Thread.unsubscribe(response,request,connection);
                       break;
                   }
                   case "update":{
                       orm.Thread.update(response,request,connection);
                       break;
                   }
                }
                break;
            }
            case "user": {
                switch (params[4]){
                    case "create":{
                            orm.User.create(response,request,connection);
                            break;
                    }
                    case "follow":{
                        orm.User.follow(response,request,connection);
                        break;
                    }
                    case "unfollow":{
                        orm.User.unfollow(response,request,connection);
                        break;
                    }
                    case "updateProfile":{
                        orm.User.updateProfile(response,request,connection);
                        break;
                    }
                }
                break;
            }
            case "post":{
                switch (params[4]){
                    case "create":{
                        orm.Post.create(response,request,connection);
                        break;
                    }
                    case "remove":{
                        orm.Post.remove(response,request,connection);
                        break;
                    }
                    case "restore":{
                        orm.Post.restore(response,request,connection);
                        break;
                    }
                    case "update":{
                        orm.Post.update(response,request,connection);
                        break;
                    }
                    case "vote":{
                        orm.Post.vote(response,request,connection);
                        break;
                    }
                }
                break;
            }
            case "forum":{
                switch (params[4]){
                    case "create":{
                        orm.Forum.create(response,request,connection);
                        break;
                    }
                }
                break;
            }
        }

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String param = request.getPathInfo();
        String [] params = param.split("/");
        Connection connection = DataService.getConnection();
        System.out.println(param);
        switch (params[3]){
            case "clear":{
                try{
                    PreparedStatement ups = connection.prepareStatement("select concat(\'TRUNCATE TABLE \', table_schema" +
                        ",\'.\',TABLE_NAME,\';\') FROM INFORMATION_SCHEMA.TABLES where table_schema = \'forums\';");
                    ResultSet resultSet = ups.executeQuery();
                    connection.setAutoCommit(false);
                    PreparedStatement FKoff = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
                    FKoff.executeUpdate();
                    while (resultSet.next()){
                        PreparedStatement t = connection.prepareStatement(resultSet.getString(1));
                        t.executeUpdate();
                    }
                    PreparedStatement FKon = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
                    FKon.executeUpdate();
                    connection.commit();
                    connection.setAutoCommit(true);
                    response.getWriter().println("OK");
                }
                catch (SQLException e){
                    try{
                        connection.rollback();
                    }
                    catch (SQLException ex){
                        ex.printStackTrace();
                        response.getWriter().println("Not good at all");

                    }
                    e.printStackTrace();
                    response.getWriter().println("Ne OK");
                }
                break;
            }
            case "thread":{
                switch (params[4]){
                    case "details":{
                        orm.Thread.details(response,request,connection);
                        break;
                    }
                    case "list":{
                        orm.Thread.list(response,request,connection);
                        break;
                    }
                    case "listPosts":{
                        orm.Thread.listPosts(response,request,connection);
                        break;
                    }
                }
                break;
            }
            case "user":{
                switch (params[4]){
                    case "details":{
                        orm.User.Details(response,request,connection);
                        break;
                    }
                    case "listFollowers":{
                        orm.User.listFollowers(response,request,connection);
                        break;
                    }
                    case "listFollowing":{
                        User.listFollowing(response,request,connection);
                        break;
                    }
                    case "listPosts":{
                        orm.User.listPosts(response,request,connection);
                        break;
                    }

                }
                break;
            }
            case "post":{
                switch (params[4]){
                    case "details":{
                        orm.Post.details(response,request,connection);
                        break;
                    }
                    case "list":{
                        orm.Post.list(response,request,connection);
                        break;
                    }
                }
                break;
            }
            case "forum":{
                switch (params[4]){
                    case "details":{
                        orm.Forum.details(response, request, connection);
                        break;
                    }
                    case "listPosts":{
                        orm.Forum.listPosts(response,request,connection);
                        break;
                    }
                    case "listUsers":{
                        orm.Forum.listUsers(response,request,connection);
                        break;
                    }
                    case "listThreads":{
                        orm.Forum.listThreads(response,request,connection);
                        break;
                    }
                }
                break;
            }
        }
    }
}


