package orm;


import database.ForumDAO;
import database.ThreadDAO;
import database.UserDAO;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Thread {
    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Boolean isDelted = Functions.getOptionalB(object,"isDeleted");
        String forum = object.getString("forum");
        String title = object.getString("title");
        Boolean isClosed = object.getBoolean("isClosed");
        String user = object.getString("user");
        String date = object.getString("date");
        String message = object.getString("message");
        String slug = object.getString("slug");
        String in = "INSERT INTO thread(user_id, forum_id, title,date, message, slug, isDeleted, isClosed) values (?,?,?,?,?,?,?,?)";
        JSONObject result = new JSONObject();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1,UserDAO.getByEmail(connection,user).getId());
            preparedStatement.setLong(2, ForumDAO.getForumByShortForumName(connection,forum).getId());
            preparedStatement.setString(3, title);
            preparedStatement.setString(4,date);
            preparedStatement.setString(5,message);
            preparedStatement.setString(6,slug);
            preparedStatement.setBoolean(7,isDelted);
            preparedStatement.setBoolean(8,isClosed);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            Long id = keys.getLong(1);
            JSONObject jsonThread = ThreadFunctions.threadToJSON(connection, ThreadDAO.getThreadById(connection,id),false, false);
            result.put("code", 0);
            result.put("response", jsonThread);
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth go wrong"));
        }
    }

    public static void close(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        String in = "UPDATE thread set isClosed = true where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("thread", id);
            JSONObject out = new JSONObject();
            out.put("code", 0);
            out.put("response", jsonObject);
            response.getWriter().println(out);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth go wrong"));
        }
    }

    public static void details(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String related = request.getParameter("related");
        Boolean relateUser = false;
        Boolean relateForum = false;
        Long id = Long.parseLong(request.getParameter("thread"));
        if (related != null){
            relateUser =  related.contains("user");
            relateForum = related.contains("forum");
        }
        try{
            JSONObject object = ThreadFunctions.threadDetails(connection,id,relateUser,relateForum );
            JSONObject out = new JSONObject();
            out.put("code", 0);
            out.put("response", object);
            response.getWriter().println(out);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth goes wrong"));
        }
    }
    public static void list(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String l = request.getParameter("limit");
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        Long limit = null;
        if(l != null){
            limit = Long.parseLong(l);
        }
        String since = request.getParameter("since");
        String  user = request.getParameter("user");
        String short_name = request.getParameter("forum");
        response.getWriter().println(ThreadFunctions.list(connection,user, short_name,since,order,limit));
    }
    public static void listPosts(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String l = request.getParameter("limit");
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        Long limit = null;
        if(l != null){
            limit = Long.parseLong(l);
        }
        String since = request.getParameter("since");
        Long id = Long.parseLong(request.getParameter("thread"));
        response.getWriter().println(Functions.postDetails(connection,null,id,order,since,limit,false,false,false));
    }
    public static void open(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        JSONObject obj = new JSONObject();
        String in = "UPDATE thread SET isClosed = false where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            obj.put("code", 0);
            JSONObject t = new JSONObject();
            t.put("thread", id);
            obj.put("response", t);
        }
        catch (SQLException e){
            e.printStackTrace();
            obj = Functions.errorMsg("Smth go wrong");
        }
        response.getWriter().println(obj);
    }
    public static void remove(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        JSONObject obj = new JSONObject();
        String in = "UPDATE thread SET isDeleted = true where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            obj.put("code", 0);
            JSONObject t = new JSONObject();
            t.put("thread", id);
            obj.put("response", t);
        }
        catch (SQLException e){
            e.printStackTrace();
            obj = Functions.errorMsg("Smth go wrong");
        }
        response.getWriter().println(obj);
    }
    public static void restore(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        JSONObject obj = new JSONObject();
        String in = "UPDATE thread SET isDeleted = false where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            obj.put("code", 0);
            JSONObject t = new JSONObject();
            t.put("thread", id);
            obj.put("response", t);
        }
        catch (SQLException e){
            e.printStackTrace();
            obj = Functions.errorMsg("Smth go wrong");
        }
        response.getWriter().println(obj);
    }
    public static void subscribe(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        String user = object.getString("user");
        String in = "INSERT INTO subscribe (user_id, thread_id) VALUES(?,?) on duplicate key update deleted = false";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(1, UserDAO.getByEmail(connection, user).getId());
            preparedStatement.executeUpdate();
            System.out.println(preparedStatement);
            JSONObject obj = new JSONObject();
            obj.put("thread", id);
            obj.put("user", user);
            JSONObject out = new JSONObject();
            out.put("code", 0);
            out.put("response",obj);
            response.getWriter().println(out);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth goes wrong"));
        }
    }
    public static void unsubscribe(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        String user = object.getString("user");
        String in = "update subscribe set deleted = true where user_id = ? and thread_id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setLong(2, id);
            preparedStatement.setLong(1, UserDAO.getByEmail(connection, user).getId());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            JSONObject obj = new JSONObject();
            obj.put("thread", id);
            obj.put("user", user);
            JSONObject out = new JSONObject();
            out.put("code", 0);
            out.put("response",obj);
            response.getWriter().println(out);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth goes wrong"));
        }
    }

    public static void update(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Long id = object.getLong("thread");
        String slug = object.getString("slug");
        String message = object.getString("message");
        String in = "UPDATE thread set slug = ?, message = ? where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setString(1, slug);
            preparedStatement.setString(2, message);
            preparedStatement.setLong(3, id);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            JSONObject out = new JSONObject();
            out.put("code", 0);
            out.put("response",ThreadFunctions.threadDetails(connection,id,false,false));
            response.getWriter().println(out);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("Smth go wrong"));
        }
    }

    public static void vote(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException{
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        try {
            Long vote = object.getLong("vote");
            Long id = object.getLong("thread");
            String in = "INSERT INTO vote (type,id,value) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(in);
            preparedStatement.setInt(1, 2);
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