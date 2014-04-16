package orm;

import database.PostDAO;
import database.UserDAO;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by Mike on 06.04.14.
 */
public class Post {
    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try{
            String in = "INSERT INTO post (parent, isApproved, isHighlighted, isEdited, isSpam, isDeleted, date, thread_id, message,user_id,forum) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(in, PreparedStatement.RETURN_GENERATED_KEYS);
            Long parent = Functions.getOptionalL(object, "parent");
            if(parent == null){
                preparedStatement.setNull(1, Types.NULL);
            }
            else {
                preparedStatement.setInt(1, Functions.getOptionalI(object, "parent"));
            }
            preparedStatement.setBoolean(2, Functions.getOptionalB(object, "isApproved"));
            preparedStatement.setBoolean(3,Functions.getOptionalB(object,"isHighlighted"));
            preparedStatement.setBoolean(4,Functions.getOptionalB(object,"isEdited"));
            preparedStatement.setBoolean(5,Functions.getOptionalB(object,"isSpam"));
            preparedStatement.setBoolean(6, Functions.getOptionalB(object, "isDeleted"));
            preparedStatement.setString(7, object.getString("date"));
            preparedStatement.setInt(8, object.getInt("thread"));
            preparedStatement.setString(9, object.getString("message"));
            Long uId = UserDAO.getByEmail(connection, object.getString("user")).getId();
            preparedStatement.setLong(10, uId);
            preparedStatement.setString(11, object.getString("forum"));
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            Long id = keys.getLong(1);
            JSONObject result = new JSONObject();
            database.Post post = new database.Post(id,parent,Functions.getOptionalB(object, "isApproved"),Functions.getOptionalB(object,"isHighlighted"),
                    Functions.getOptionalB(object,"isEdited"),Functions.getOptionalB(object,"isSpam"),Functions.getOptionalB(object, "isDeleted"),
                    object.getString("date"),object.getLong("thread"),object.getString("message"),uId,object.getString("forum"));
            result.put("code", 0);
            result.put("response",  Functions.postToJSON(connection,post,false,false,false));
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void list(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String forum = request.getParameter("forum");
        String thread = request.getParameter("thread");
        Long thread_id = null;
        if(thread != null){
            thread_id = Long.parseLong(thread);
        }
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
        response.getWriter().println(Functions.postDetails(connection, forum, thread_id, order, since, limit,false,false,false));
    }

    public static void remove(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            Integer id = object.getInt("post");
            String in = "update post set isDeleted = true where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            JSONObject result = new JSONObject();
            result.put("code", 0);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("post", id);
            result.put("response",  responseJSON);
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }

    public static void restore(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            Integer id = object.getInt("post");
            String in = "update post set isDeleted = false where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            JSONObject result = new JSONObject();
            result.put("code", 0);
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("post", id);
            result.put("response",  responseJSON);
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }

    public static void update(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            Long id = object.getLong("post");
            String message = object.getString("message");
            String in = "update post set message = ? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1,message);
            preparedStatement.setLong(2, id);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("response", Functions.postToJSON(connection,database.PostDAO.getPostById(connection,id),false,false,false));
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }

    public static void details(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        Long id = Long.parseLong(request.getParameter("post"));
        String related = request.getParameter("related");
        Boolean relateUser =false;
        Boolean relateForum=false;
        Boolean relateThread=false;
        if (related != null){
            relateUser =  related.contains("user");
            relateForum = related.contains("forum");
            relateThread = related.contains("thread");
        }
        response.getWriter().println(Functions.postDetails(connection,id,"desc",null,null,relateUser,relateThread,relateForum));
    }

    public static void vote(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            Long vote = object.getLong("vote");
            Long id = object.getLong("post");
            String in = "INSERT INTO vote (type,id,value) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setInt(1, 1);
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(3, vote);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("response", Functions.postToJSON(connection,database.PostDAO.getPostById(connection,id),false,false,false));
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }
}
