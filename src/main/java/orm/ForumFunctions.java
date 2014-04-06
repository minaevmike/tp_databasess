package orm;

import database.*;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;

public class ForumFunctions {
    public static JSONObject forumToJSON(Connection connection, database.Forum forum, Boolean related_user) throws SQLException{
        JSONObject object = new JSONObject();
        object.put("id", forum.getId());
        object.put("name", forum.getName());
        object.put("short_name", forum.getShort_name());
        String email = database.UserDAO.getById(connection, forum.getUser()).getEmail();
        if(related_user){
            object.put("user", Functions.userDetails(connection,email));
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
}
