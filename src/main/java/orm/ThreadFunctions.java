package orm;

import database.*;
import database.Forum;
import database.Thread;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mike on 08.04.14.
 */
public class ThreadFunctions {
    public static JSONObject threadToJSON(Connection connection,Long id, Boolean relateUser, Boolean relateForum) throws SQLException{
        return threadToJSON(connection, ThreadDAO.getThreadById(connection,id) , relateUser, relateForum);
    }


    public static JSONObject threadToJSON(Connection connection,database.Thread thread, Boolean relateUser, Boolean relateForum) throws SQLException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date",thread.getDate());
        Forum forum = ForumDAO.getForumById(connection,thread.getForum());
        if(relateForum){
            jsonObject.put("forum", ForumFunctions.forumToJSON(connection, forum,false));
        }
        else {
            jsonObject.put("forum",forum.getName());
        }
        jsonObject.put("id",thread.getId());
        jsonObject.put("isClosed", thread.getIsClosed());
        jsonObject.put("isDeleted", thread.getIsDeleted());
        jsonObject.put("message", thread.getMessage());
        jsonObject.put("slug", thread.getSlug());
        jsonObject.put("title", thread.getTitle());
        String email = database.UserDAO.getById(connection, forum.getUser()).getEmail();
        if(relateUser){
            jsonObject.put("user", Functions.userDetails(connection,email).getJSONObject("response"));
        }
        else{
            jsonObject.put("user", email);
        }
        return jsonObject;

    }

    public static Long countPosts(Connection connection, Long id){
        String in = "SELECT COUNT(*) FROM post where thread_id = ?";
        Long out;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println(preparedStatement);
            rs.next();
            out = rs.getLong(1);

        }
        catch (SQLException e){
            out = Long.MIN_VALUE;
        }
        return out;
    }

    public static JSONObject threadDetails(Connection connection, Thread thread, Boolean relateUser, Boolean relateForum) throws SQLException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date",thread.getDate());
        Long dislikes = Functions.votes(connection,thread.getId(),2,-1);
        Long likes = Functions.votes(connection,thread.getId(),2,1);
        Long points = likes - dislikes;
        Long posts = ThreadFunctions.countPosts(connection, thread.getId());
        Forum forum = ForumDAO.getForumById(connection,thread.getForum());
        jsonObject.put("dislikes", dislikes);
        if(relateForum){
            jsonObject.put("forum", ForumFunctions.forumToJSON(connection, forum,false));
        }
        else {
            jsonObject.put("forum",forum.getShort_name());
        }
        jsonObject.put("id",thread.getId());
        jsonObject.put("isClosed", thread.getIsClosed());
        jsonObject.put("isDeleted", thread.getIsDeleted());
        jsonObject.put("likes", likes);
        jsonObject.put("message", thread.getMessage());
        jsonObject.put("points", points);
        jsonObject.put("posts", posts);
        jsonObject.put("slug", thread.getSlug());
        jsonObject.put("title", thread.getTitle());
        String email = database.UserDAO.getById(connection, thread.getUser()).getEmail();
        if(relateUser){
            jsonObject.put("user", Functions.userDetails(connection,email).getJSONObject("response"));
        }
        else{
            jsonObject.put("user", email);
        }
        return jsonObject;
    }
    public static JSONObject threadDetails(Connection connection, Long id, Boolean relateUser, Boolean relateForum) throws SQLException{
        database.Thread thread = ThreadDAO.getThreadById(connection, id);
        return threadDetails(connection,thread,relateUser,relateForum);
    }

    public static JSONObject list(Connection connection, String user, String forum,String since, String order, Long limit,Boolean relateUser,Boolean relateForum){
        JSONObject object = new JSONObject();
        String in="SELECT t.id, t.forum_id, t.title, DATE_FORMAT(t.date,'%Y-%m-%d %H:%i:%s'), t.message, t.slug, t.isDeleted, t.isClosed, t.user_id  from thread t join" +
                " user u on u.id = t.user_id join forum f on t.forum_id = f.id";
        if(user != null){
            in += " where u.email = ?";
        }
        else{
            in += " where f.shortForumName = ?";
        }
        if(since != null){
            in += " and date > ? ";
        }
        in += " ORDER BY t.date " + order;
        if(limit != null){
            in += " LIMIT " + limit;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            if(user != null){
                preparedStatement.setString(1, user);
            }
            else{
                preparedStatement.setString(1, forum);

            }
            if(since != null){
                preparedStatement.setString(2, since);
            }
            JSONArray array = new JSONArray();
            System.out.println(preparedStatement);
            ResultSet rs= preparedStatement.executeQuery();
            while(rs.next()){
                database.Thread thread = new Thread(rs.getLong(1),rs.getLong(9),rs.getLong(2),rs.getString(3),rs.getString(4),rs.getString(5),
                        rs.getString(6),rs.getBoolean(7),rs.getBoolean(8));
                array.put(threadDetails(connection, thread, relateUser, relateForum));
            }
            object.put("code",0);
            object.put("response",array);
        }
        catch (SQLException e){
            e.printStackTrace();
            object = Functions.errorMsg("Smth go wrong");
        }
        return object;
    }

    public static JSONObject list(Connection connection, String user, String forum,String since, String order, Long limit){
        return list(connection, user, forum, since, order, limit,false,false);
    }
}