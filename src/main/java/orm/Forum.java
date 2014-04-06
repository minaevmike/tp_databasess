package orm;

import database.UserDAO;
import org.json.JSONObject;
import org.json.JSONTokener;
import sun.net.www.content.text.plain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Forum {
    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        String name = object.getString("name");
        String short_name = object.getString("short_name");
        String email = object.getString("user");
        String in = "INSERT INTO forum(forumName,shortForumName,user_id) values(?,?,?)";
        JSONObject result = new JSONObject();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(in, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, short_name);
            preparedStatement.setLong(3, UserDAO.getByEmail(connection, email).getId());
            preparedStatement.executeUpdate();
            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys.next();
            Long id = keys.getLong(1);
            result.put("code", 0);
            result.put("response", ForumFunctions.forumToJSON(connection, database.ForumDAO.getForumById(connection, id),false));
            response.getWriter().println(result);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }

    public static void details(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String related = request.getParameter("related");
        Boolean relateUser = false;
        if (related != null){
            relateUser =  related.contains("user");
        }
        String short_name = request.getParameter("forum");
        JSONObject object = new JSONObject();
        try{
            JSONObject resp = new JSONObject();
            resp = ForumFunctions.forumToJSON(connection, database.ForumDAO.getForumByShortForumName(connection, short_name), relateUser);
            object.put("code", 0);
            object.put("response", resp);
            System.out.println(object);
            response.getWriter().println(object);
        }
        catch (SQLException e){
            e.printStackTrace();
            response.getWriter().println(Functions.errorMsg("SMth go wrong"));
        }
    }

    public static void listPosts(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String l = request.getParameter("limit");
        String order = request.getParameter("order");
        if(order == null){
            order = "desc";
        }
        Integer limit = null;
        if(l != null){
            limit = Integer.parseInt(l);
        }
        String since = request.getParameter("since");
        String related = request.getParameter("related");
        String short_name = request.getParameter("forum");
        Boolean relateUser = false;
        Boolean relateForum = false;
        Boolean relateThread = false;
        if (related != null){
            relateUser =  related.contains("user");
            relateForum = related.contains("forum");
            relateThread = related.contains("thread");
        }

        response.getWriter().println(Functions.postDetails(connection,short_name, null,order,since,limit,relateUser,relateThread,relateForum));
    }

}
