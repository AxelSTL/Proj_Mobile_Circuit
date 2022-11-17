package com.example.book2run.ui.data;

import android.os.StrictMode;
import android.util.Log;

import com.example.book2run.ui.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private String prenom;
    public Result<LoggedInUser> login(String username, String password) {
        try {
            // TODO: handle loggedInUser authentication

            boolean userExist= logAccount(username, password);
            Log.i("test", String.valueOf(userExist));
            if(userExist){

                int user = updateUserInfo(username);
                LoggedInUser inUser = new LoggedInUser(String.valueOf(user), prenom);
                return new Result.Success<>(inUser);
            } else {
                // TODO : faire qu'il doit se reco
            }
          /*  LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");*/
            //return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
        return null;
    }

    public void logout() {
        // TODO: revoke authentication
    }


    public boolean logAccount(String username, String password) throws IOException {
        boolean userExist = false;
        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            String requestURL = "http://192.168.2.118:8180/login?user=" + username + "&mdp=" + password;
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            Log.i("L'utilisateur existe ? ", buffer.toString());
            if(buffer.toString().equals("true")){
                userExist = true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userExist;
    }


    public int updateUserInfo(String username) throws IOException {
        int id =0;
        try {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        String requestURL = "http://192.168.2.118:8180/utilisateur?user=" + username;
        URL url = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            Log.i("Line de l'update ", line);
        }
        LoginRepository user = LoginRepository.getInstance(new LoginDataSource());
        JSONObject userFromPost = new JSONObject(String.valueOf(buffer));
        user.code= userFromPost.getInt("code");
        user.mail = userFromPost.getString("email");
        user.lastName = userFromPost.getString("nom");
        user.username = userFromPost.getString("prenom");
        prenom = user.username;
        //Log.i("L'utilisateur existe ? ", buffer.toString());
        if(buffer.toString().equals("true")){
           // userExist = true;
        }

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

}