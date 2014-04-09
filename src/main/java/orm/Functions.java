package orm;

import database.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Mike on 06.04.14.
 */
public class Functions {
    public static Boolean getOptionalB(JSONObject object, String paramName){
        Boolean out;
        try {
            out = object.getBoolean(paramName);
        }
        catch (JSONException e){
            out = false;
        }
        return out;
    }
    public static Integer getOptionalI(JSONObject object, String paramName){
        Integer out;
        try{
            out = object.getInt(paramName);
        }
        catch (JSONException e){
            out = null;
        }
        return out;
    }
    public static JSONObject errorMsg(String msg){
        JSONObject result = new JSONObject();
        result.put("code", 1);
        result.put("message", msg);
        return result;
    }
    public static JSONObject userDetails(Connection connection, String email,String order, Integer limit, Integer since_id, Integer type ){
        JSONObject result = new JSONObject();
        try{
            database.User user = UserDAO.getByEmail(connection, email);
            result.put("code", 0);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("about", user.getAbout());
            responseJSON.put("email", user.getEmail());
            responseJSON.put("id", user.getId());
            responseJSON.put("isAnonymous", user.getIsAnonymous());
            responseJSON.put("name", user.getName());
            responseJSON.put("username", user.getUsername());
            if(type == 1 ){
                responseJSON.put("followers",getFollowers(email,connection,order,limit,since_id));
                responseJSON.put("following",getFollowing(email,connection));
            }
            if(type == 2){
                responseJSON.put("following",getFollowing(email,connection,order,limit,since_id));
                responseJSON.put("followers",getFollowers(email,connection));
            }
            if(type == 0){
                responseJSON.put("followers",getFollowers(email,connection));
                responseJSON.put("following",getFollowing(email,connection));
            }
            responseJSON.put("subscriptions", subs(connection,user.getId()));
            result.put("response", responseJSON);

        }
        catch (SQLException e){
            result.put("code", 1);
            result.put("message", "No such user");
        }
        finally {
            System.out.println(result.toString());
        }
        return result;
    }
    public static JSONObject userDetails(Connection connection,String email){
        return userDetails(connection,email,"desc",null, -1, 0);
    }

    public static JSONArray subs(Connection connection, Long id){
        JSONArray out = new JSONArray();
        String in = "SELECT thread_id from subscribe where user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                out.put(rs.getLong(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return out;
    }

    public static JSONObject userDetails(Connection connection, Long id) throws  SQLException{
        return userDetails(connection, UserDAO.getById(connection,id).getEmail());
    }

    public static JSONObject postDetails(Connection connection,String forum, Long thread, String order, String since, Long limit, Boolean relateUser, Boolean relateThread, Boolean relateForum){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String in = "SELECT id, parent, isApproved, isHighlighted, isEdited, isSpam, isDeleted, DATE_FORMAT(date,'%Y-%m-%d %H:%i:%s'), thread_id, message,user_id,forum from post where removed = FALSE and ";
        if(forum != null){
            in = in + " forum = ?";
        }
        else{
            in = in + " thread_id = ? ";
        }
        if(since != null){
            in += " and date > ? ";
        }
        in += " ORDER  BY date " + order;
        if(limit != null){
            in += " limit " + limit.toString();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            if(forum != null){
                preparedStatement.setString(1,forum);
            }
            else {
                preparedStatement.setLong(1,thread);
            }
            if(since != null){
                preparedStatement.setString(2, since);
            }
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                database.Post post = new database.Post(resultSet.getLong(1),resultSet.getLong(2),resultSet.getBoolean(3),resultSet.getBoolean(4),resultSet.getBoolean(5),
                        resultSet.getBoolean(6),resultSet.getBoolean(7),resultSet.getString(8),resultSet.getLong(9),resultSet.getString(10),resultSet.getLong(11),resultSet.getString(12));
                jsonArray.put(postToJSON(connection,post,relateUser,relateThread,relateForum));
            }
            jsonObject.put("code", 0);
            jsonObject.put("response", jsonArray);
        }
        catch (SQLException e){
            jsonObject = errorMsg("Smth goes wrong");
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static Long votes(Connection connection,Long id, Integer type, Integer value){
        String in = "SELECT COUNT(*) FROM vote where type = ? and id = ? and value = ?";
        Long out;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setInt(1, type);
            preparedStatement.setLong(2, id);
            preparedStatement.setInt(3, value);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            out = rs.getLong(1);
        }
        catch (SQLException e){
            e.printStackTrace();
            out = Long.MAX_VALUE;
        }
        return  out;
    }
    public static JSONObject postToJSON(Connection connection, database.Post post, Boolean relateUser, Boolean relateThread,Boolean relateForum) throws  SQLException{
        JSONObject out = new JSONObject();
        out.put("date", post.getDate());
        if(relateForum){
            out.put("forum", ForumFunctions.forumToJSON(connection,ForumDAO.getForumByShortForumName(connection,post.getForum()),false));
        }
        else {
            out.put("forum", post.getForum());
        }
        out.put("id", post.getId());
        out.put("isApproved", post.isApproved());
        out.put("isDeleted", post.isDeleted());
        out.put("isEdited", post.isEdited());
        Long likes = votes(connection, post.getId(), 1, 1);
        Long dislikes = votes(connection, post.getId(), 1, -1);
        Long points = likes - dislikes;
        out.put("likes", likes);
        out.put("dislikes", dislikes);
        out.put("points", points);
        out.put("isHighlighted", post.isHighlighted());
        out.put("isSpam", post.isSpam());
        out.put("message", post.getMessage());
        if(relateThread){
            out.put("thread", ThreadFunctions.threadDetails(connection,post.getThread(),false,false));
        }
        else {
            out.put("thread", post.getThread());
        }
        String email = database.UserDAO.getById(connection, post.getUser()).getEmail();
        if(relateUser){
            out.put("user", Functions.userDetails(connection,email).getJSONObject("response"));
        }
        else{
            out.put("user", email);
        }
        return out;
    }

    public static JSONArray getFollowers(String email, Connection connection, String order, Integer limit, Integer since_id){
        JSONArray responseJSON = new JSONArray();
        String in = "SELECT u1.email FROM follows f JOIN user u on u.id = f.idFollowers JOIN user u1 on u1.id = f.idFollowing where u.email = ? and u1.id > ? order by u1.name " + order;
        if(limit != null){
            in += " limit " + limit.toString();
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, since_id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                responseJSON.put(rs.getString(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return responseJSON;
    }
    public static JSONArray getFollowers(String email, Connection connection){
        return getFollowers(email, connection, "desc", null, -1);
    }
    public static JSONArray getFollowing(String email, Connection connection, String order, Integer limit, Integer since_id){
        JSONArray responseJSON = new JSONArray();
        String in = "SELECT u.email FROM follows f JOIN user u on u.id = f.idFollowers JOIN user u1 on u1.id = f.idFollowing where u1.email = ? and u.id > ? order by u1.name " + order;
        if(limit != null){
            in += " limit " + limit.toString();
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, since_id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                responseJSON.put(rs.getString(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return responseJSON;
    }
    public static JSONArray getFollowing(String email, Connection connection){
        return getFollowing(email, connection, "desc", null, -1);
    }
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }
}

