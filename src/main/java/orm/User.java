package orm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import database.UserDAO;
import org.json.JSONArray;
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

    public static void listPosts(HttpServletResponse response, HttpServletRequest request,Connection connection) throws IOException{
        String email = request.getParameter("user");
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        String l = request.getParameter("limit");
        Long limit = null;
        if(l != null){
            limit = Long.parseLong(l);
        }
        String since = request.getParameter("since");
        JSONArray jsonArray = new JSONArray();
        try{
            Long id = UserDAO.getByEmail(connection,email).getId();
            String in = "SELECT id, parent, isApproved, isHighlighted, isEdited, isSpam, isDeleted, DATE_FORMAT(date,'%Y-%m-%d %H:%i:%s'), thread_id, message,user_id,forum from post where ";
            in = in + " user_id = ? ";
            if(since != null){
                in += " and date > ? ";
            }
            in += " ORDER  BY date " + order;
            if(limit != null){
                in += " limit " + limit.toString();
            }
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1, id);
            if(since != null){
                preparedStatement.setString(2, since);
            }
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                database.Post post = new database.Post(resultSet.getLong(1),resultSet.getLong(2),resultSet.getBoolean(3),resultSet.getBoolean(4),resultSet.getBoolean(5),
                        resultSet.getBoolean(6),resultSet.getBoolean(7),resultSet.getString(8),resultSet.getLong(9),resultSet.getString(10),resultSet.getLong(11),resultSet.getString(12));
                jsonArray.put(Functions.postToJSON(connection,post,false,false,false));
            }
            JSONObject object = new JSONObject();
            object.put("code", 0);
            object.put("response", jsonArray);
            response.getWriter().println(object);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMTH GO WRONG"));
        }
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
         JSONArray responseJSON = new JSONArray();
         String in = "SELECT  u.id, u.username, u.email, u.name, u.about, u.isAnonymous FROM follows f JOIN user u on " +
                 "u.id = f.idFollowers JOIN user u1 on u1.id = f.idFollowing where f.deleted = false and u1.email = ? and u.id >= ? order by u.name " + order;
         if(limit != null){
              in += " limit " + limit.toString();
         }
         try {
             PreparedStatement preparedStatement = connection.prepareStatement(in);
             preparedStatement.setString(1,email);
             preparedStatement.setInt(2, since_id);
             System.out.println(preparedStatement);
             ResultSet resultSet = preparedStatement.executeQuery();
             while (resultSet.next()){
                 database.User user = new database.User(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                         resultSet.getString(5),resultSet.getBoolean(6));
                 responseJSON.put(Functions.userDetails(connection,user).getJSONObject("response"));
             }
             JSONObject object = new JSONObject();
             object.put("code",0);
             object.put("response",responseJSON);
             response.getWriter().println(object);
         }
         catch (SQLException e){
             e.printStackTrace();
             response.getWriter().println(Functions.errorMsg("SMTH go wrong"));
         }
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
        JSONArray responseJSON = new JSONArray();
        String in = "SELECT  u1.id, u1.username, u1.email, u1.name, u1.about, u1.isAnonymous FROM follows f JOIN user u " +
                "on u.id = f.idFollowers JOIN user u1 on u1.id = f.idFollowing where f.deleted = false and u.email = ? and u1.id >= ? order by u1.name " + order;
        if(limit != null){
            in += " limit " + limit.toString();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1,email);
            preparedStatement.setInt(2,since_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            while (resultSet.next()){
                database.User user = new database.User(resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getBoolean(6));
                responseJSON.put(Functions.userDetails(connection,user).getJSONObject("response"));
            }
            JSONObject object = new JSONObject();
            object.put("code",0);
            object.put("response",responseJSON);
            response.getWriter().println(object);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMTH go wrong"));
        }
    }

    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        database.User user = null;
        try {
            String query = "INSERT INTO user (username, email, name, about, isAnonymous) VALUES(?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
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
            System.out.println(preparedStatement);
            preparedStatement.setBoolean(5, Functions.getOptionalB(object, "isAnonymous"));
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            Long id = keys.getLong(1);
            if(isAn == false){
                user = new database.User(id,object.getString("username"),object.getString("email"),object.getString("name"),
                    object.getString("about"),Functions.getOptionalB(object, "isAnonymous"));
            }
            else{
                user = new database.User(id,null,object.getString("email"),null,
                        null,Functions.getOptionalB(object, "isAnonymous"));
            }
        }
        catch (MySQLIntegrityConstraintViolationException e){
            try {
                user = UserDAO.getByEmail(connection,object.getString("email"));
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        catch (SQLException e){
            try {
                user = UserDAO.getByEmail(connection,object.getString("email"));
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try{
                System.out.println(Functions.getOptionalB(object, "isAnonymous"));
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
            String in = "INSERT INTO follows (idFollowers, idFollowing) values(?,?) on duplicate key update deleted = false";
            database.User user1 = UserDAO.getByEmail(connection,foolower);
            database.User user2 = UserDAO.getByEmail(connection,followee);
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1, user1.getId());
            preparedStatement.setLong(2, user2.getId());
            System.out.println(preparedStatement);
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
            String in = "update follows set deleted = true where idFollowers = ? and idFollowing = ?";
            database.User user1 = UserDAO.getByEmail(connection,foolower);
            database.User user2 = UserDAO.getByEmail(connection,followee);
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1, user1.getId());
            preparedStatement.setLong(2, user2.getId());
            System.out.println(preparedStatement);
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
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            response.getWriter().println(Functions.userDetails(connection, email));
        }
        catch (SQLException e){
            response.getWriter().println(Functions.errorMsg("Not such user"));
            e.printStackTrace();
        }
    }
}
