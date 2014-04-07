package orm;


import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class Thread {
    public static void create(HttpServletResponse response, HttpServletRequest request, Connection connection) throws IOException {
        String json = Functions.getBody(request);
        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
        Boolean isDelted = Functions.getOptionalB(object,"isDeleted");
        String forum = object.getString("forum");
        String title = object.getString("title");
        Boolean isClosed = object.getBoolean("isClosed");

    }
}
