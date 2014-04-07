package send;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

/**
 * Created by Mike on 17.03.14.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        sendPostCreatePost();
    }
    private static void sendGetPostList(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/post/list?since=2014-01-01 00:00:00&limit=2&order=asc&forum=forumwithsufficientlylargename");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private static void sendGetLF(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/user/listFollowers?user=example@mail.ru&limit=2&since_id=2&order=asc");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendGet(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/user/details?user=example@mail.ru");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendForumListUsers(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/forum/details?forum=forumwithsufficientlylargename&limit=2&since_id=10&order=asc");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendForumList(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/forum/listPosts?forum=forumwithsufficientlylargename&related=['user']&order=asc&limit=2&since=2014-01-01 00:00:00");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private static void sendForumDetails(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet request = new HttpGet("http://localhost:8083/db/api/forum/details?forum=forumwithsufficientlylargename&related=['user']");
            //HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            //StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'John', 'email': 'exam213dfgdfgdfgfdgdfgdfgdfg5ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendForumCreate(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/forum/create");
            StringEntity params =new StringEntity("{'name': 'Forum With Sufficiently Large Name', 'short_name': 'forumwithsufficientlylargename', 'user': 'example3@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendVotePost(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/post/vote");
            StringEntity params =new StringEntity("{'post': 4, 'vote': -1} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendUpdatePost(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/post/update");
            StringEntity params =new StringEntity("{'post': 4, 'message': 'TEST UPDSP1'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendPostF(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/user/follow");
            StringEntity params =new StringEntity("{'follower':'example@mail.ru','followee':'example3@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendPostUpdate(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/user/updateProfile");
            StringEntity params =new StringEntity("{'about':'HEY!!!!','user':'example@mail.ru','name':'prr'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendPostList(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/post/list");
            StringEntity params =new StringEntity("{'since': '2014-01-02 00:00:00', 'limit': 2, 'order': 'asc', 'forum': 'forumwithsufficientlylargename'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendPostCreatePost(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/post/create");
            StringEntity params =new StringEntity("{'isApproved': True, 'user': 'C++@mail.ru', 'date': '2014-01-01 00:00:01', 'message': 'my message 1', 'isSpam': False, 'isHighlighted': True, 'thread': 1, 'forum': 'forumwithsufficientlylargename', 'isDeleted': False, 'isEdited': True}: ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
    private static void sendPost(){
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/db/api/user/create");
            StringEntity params =new StringEntity("{'username': 'ussdfer1', 'about': 'hello im user1', 'name': 'C++', 'email': 'C++@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity,"UTF-8");
            System.out.println(responseEntity);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
