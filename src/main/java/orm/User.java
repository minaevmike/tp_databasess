package orm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import database.UserDAO;
import org.json.JSONObject;
import org.json.JSONTokener;
import orm.Functions;
/**
 * Created by Mike on 06.04.14.
 */
public class User {
    public static void Details(HttpServletResponse response, HttpServletRequest request,Connection connection) throws IOException{
        String email = request.getParameter("user");
        response.getWriter().println(Functions.userDetails(connection, email).toString());
    }

    public static void listFollowers(HttpServletResponse response, HttpServletRequest request,Connection connection) throws IOException{
        String email = request.getParameter("user");
        String l = request.getParameter("limit");
        Integer limit = null;
        if(l != null){
            limit = Integer.parseInt(l);
        }
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        order = order.replace("'", "");
        Integer since_id = -1;
        String sid = request.getParameter("since_id");
        if(sid != null){
            since_id = Integer.parseInt(sid);
        }
        response.getWriter().println(Functions.userDetails(connection, email, order, limit, since_id, 1));
    }

    public static void listFollowing(HttpServletResponse response, HttpServletRequest request,Connection connection) throws IOException{
        String email = request.getParameter("user");
        String l = request.getParameter("limit");
        Integer limit = null;
        if(l != null){
            limit = Integer.parseInt(l);
        }
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        order = order.replace("'", "");
        Integer since_id = -1;
        String sid = request.getParameter("since_id");
        if(sid != null){
            since_id = Integer.parseInt(sid);
        }
        response.getWriter().println(Functions.userDetails(connection, email, order, limit, since_id, 2));
    }

    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            System.out.println(object.getString("username"));
            String query = "INSERT INTO user (username, email, name, about, isAnonymous) VALUES(?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Boolean isAn = Functions.getOptionalB(object, "isAnonymous");
            if(isAn == false){
                preparedStatement.setString(1,object.getString("username"));
                preparedStatement.setString(3,object.getString("name"));
                preparedStatement.setString(4, object.getString("about"));
            }
            else{
                preparedStatement.setNull(1, Types.NULL);
                preparedStatement.setNull(3, Types.NULL);
                preparedStatement.setNull(4, Types.NULL);
            }
            preparedStatement.setString(2,object.getString("email"));
            preparedStatement.setBoolean(5, Functions.getOptionalB(object, "isAnonymous"));
            preparedStatement.executeUpdate();
        }
        catch (MySQLIntegrityConstraintViolationException e){

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                database.User user = UserDAO.getByEmail(connection, object.getString("email"));
                JSONObject result = new JSONObject();
                result.put("code", 0);
                JSONObject responseJSON = new JSONObject();
                Boolean isAn = user.getIsAnonymous();
                if(isAn == false){
                    responseJSON.put("about", user.getAbout());
                    responseJSON.put("name", user.getName());
                    responseJSON.put("username", user.getUsername());
                }
                else{
                    responseJSON.put("about", JSONObject.NULL);
                    responseJSON.put("name", JSONObject.NULL);
                    responseJSON.put("username", JSONObject.NULL);
                }
                responseJSON.put("isAnonymous", user.getIsAnonymous());
                responseJSON.put("id", user.getId());
                responseJSON.put("email", user.getEmail());
                result.put("response", responseJSON);
                System.out.println(result.toString());
                response.getWriter().println(result.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void follow(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        String foolower = object.getString("follower");
        String followee = object.getString("followee");
        try {
            String in = "INSERT INTO follows (idFollowers, idFollowing) values(?,?) ON DUPLICATE KEY idFollowers = idFollowers";
            database.User user1 = UserDAO.getByEmail(connection,foolower);
            database.User user2 = UserDAO.getByEmail(connection,followee);
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1, user1.getId());
            preparedStatement.setLong(2, user2.getId());
            preparedStatement.executeUpdate();
            response.getWriter().println(Functions.userDetails(connection,foolower).toString());
        }
        catch (SQLException e){
            response.getWriter().println(Functions.errorMsg("Already follow"));
            e.printStackTrace();
        }
    }
    public static void unfollow(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        String foolower = object.getString("follower");
        String followee = object.getString("followee");
        try {
            String in = "delete from follows where idFollowers = ? and idFollowing = ?";
            database.User user1 = UserDAO.getByEmail(connection,foolower);
            database.User user2 = UserDAO.getByEmail(connection,followee);
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1, user1.getId());
            preparedStatement.setLong(2, user2.getId());
            preparedStatement.executeUpdate();
            response.getWriter().println(Functions.userDetails(connection,foolower).toString());
        }
        catch (SQLException e){
            response.getWriter().println(Functions.errorMsg("Not followed"));
            e.printStackTrace();
        }
    }
    public static void updateProfile(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        String email = object.getString("user");
        String about = object.getString("about");
        String name = object.getString("name");
        try {
            String in = "UPDATE user set name = ?, about = ? where email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,about);
            preparedStatement.setString(3,email);
            preparedStatement.executeUpdate();
            response.getWriter().println(Functions.userDetails(connection, email));
        }
        catch (SQLException e){
            response.getWriter().println(Functions.errorMsg("Not such user"));
            e.printStackTrace();
        }
    }
}
