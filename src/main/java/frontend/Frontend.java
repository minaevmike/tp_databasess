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
        switch (params[3]){
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
                        //Integer id = object.getInt("post");
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
        switch (params[3]){
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

                }
            }
            case "post":{
                switch (params[4]){
                    case "details":{
                        break;
                    }
                    case "list":{
                        orm.Post.list(response,request,connection);
                        break;
                    }
                }
            }
            case "forum":{
                switch (params[4]){
                    case "details":{
                        System.out.println("OUT");
                        orm.Forum.details(response, request, connection);
                        break;
                    }
                    case "listPosts":{
                        orm.Forum.listPosts(response,request,connection);
                        break;
                    }
                }
            }
        }
    }
}


