package orm;

import database.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForumFunctions {
    public static JSONObject forumToJSON(Connection connection, database.Forum forum, Boolean related_user) throws SQLException{
        JSONObject object = new JSONObject();
        object.put("id", forum.getId());
        object.put("name", forum.getName());
        object.put("short_name", forum.getShort_name());
        String email = database.UserDAO.getById(connection, forum.getUser()).getEmail();
        if(related_user){
            object.put("user", Functions.userDetails(connection,email).getJSONObject("response"));
        }
        else{
            object.put("user", email);
        }
        return object;
    }

    /*public static JSONObject listPosts(Connection connection, String short_name, Boolean relateUser, Boolean relateForum, Boolean relateThread, String since, String order, ){
        JSONObject out = new JSONObject();
        String in  = "SELECT id, parent, isApproved, isHighlighted, isEdited, isSpam, isDeleted, DATE_FORMAT(date,'%Y-%m-%d %H:%i:%s'), thread_id, message,user_id,forum from post where removed = FALSE and forum = ?";
    }*/
    public static JSONObject listUser(Connection connection, String short_name, Long since_id, String order, Long limit) {
        JSONObject object = new JSONObject();
        JSONArray out = new JSONArray();
        String in = "SELECT DISTINCT user_id from post where forum = ? and user_id > ? ORDER BY user_id " + order ;
        if(limit != null){
            in += " LIMIT " + limit;
        }
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1, short_name);
            preparedStatement.setLong(2, since_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                out.put(Functions.userDetails(connection, rs.getLong(1)).getJSONObject("response"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            object = Functions.errorMsg("Smth go wrong");
        }
        object.put("code", 0);
        object.put("response", out);
        return object;
    }
}
