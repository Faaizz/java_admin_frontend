package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StaffResource extends APIResource {

    public StaffResource(String base_URL, String api_path, String api_token) {

        super(base_URL, api_path + "/staff", api_token);

    }

    /**
     * Logs in the staff with specified details
     * @param email
     * @param password
     * @param remember
     * @return
     * @throws Exception
     */
    public String login(String email, String password, String remember) throws Exception {

        List<NameValuePair> nvps= new ArrayList<>();
        nvps.add(new BasicNameValuePair("email", email));
        nvps.add(new BasicNameValuePair("password", password));
        nvps.add(new BasicNameValuePair("remember", String.valueOf(remember)));

        URI uri= buildUri("/login" );

        HttpPost httpPost = new HttpPost(uri);

        HttpEntity postEntity= new UrlEncodedFormEntity(nvps);
        httpPost.setEntity(postEntity);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        //CHECK FOR REDIRECT
        if( (response.getStatusLine().getStatusCode() >= 300) && (response.getStatusLine().getStatusCode() < 400)  ){

            //EFFECT THE REDIRECT

            String newLink= response.getFirstHeader("Location").getValue();

            httpPost= new HttpPost(new URI(newLink));
            httpPost.setEntity(postEntity);

            response= this.executeRequest(httpPost);

        }

        return this.handleResponse(response);

    }

    /**
     * Logs out currently authenticated user
     * @return returns true on success, false otherwise
     */
    public boolean logout() throws Exception {

        URI uri= buildUri("/logout" );

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        this.handleResponse(response);

        // CLEAR COOKIES
        return CookieStore.clearCookies();

    }

    /**
     * Get instance of authenticated <b>Staff</b>
     * @return Json representation of authenticated staff
     * @throws Exception
     */
    public String myAccount() throws Exception {

        URI uri= buildUri("/my_account");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Get all <b>Staff</b>
     * @return
     * @throws Exception
     */
    public String all() throws Exception {

        URI uri= buildUri("");

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Add new <b>Staff</b>
     * @param new_staff
     * @return Json response
     * @throws Exception
     */
    public String add(UploadablePerson new_staff) throws Exception {

        Gson gson= new Gson();

        URI uri= buildUri("");

        HttpPost httpPost= new HttpPost(uri);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("first_name", new_staff.getFirst_name());
        builder.addTextBody("last_name", new_staff.getLast_name());
        builder.addTextBody("email", new_staff.getEmail());
        builder.addTextBody("gender", new_staff.getGender());
        builder.addTextBody("password", new_staff.getPassword());
        builder.addTextBody("address", new_staff.getAddress());
        builder.addTextBody("phone_numbers", gson.toJson(new_staff.getPhone_numbers(), new TypeToken<List<String>>(){}.getType()));
        builder.addTextBody("privilege_level", new_staff.getPrivilege_level());

        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }


    /**
     * Delete <b>Staff</b> with specified email
     * @param email
     * @throws Exception
     */
    public void delete(String email) throws Exception {

        URI uri= buildUri("/" + email);

        HttpDelete httpDelete= new HttpDelete(uri);

        CloseableHttpResponse response= this.executeRequest(httpDelete);

        this.handleResponse(response);

    }

    /**
     * Get a single staff
     * @param email
     * @return
     * @throws Exception
     */
    public String single(String email) throws Exception{

        URI uri= buildUri("/email" + "/" + email);

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Search for staff with specified parameters
     * @param post_data
     * @return JSON representation of result
     * @throws Exception
     */
    public String search(Map<String, String> post_data) throws Exception{

        MultipartEntityBuilder builder= MultipartEntityBuilder.create();

        post_data.forEach( (key, value)->{

            builder.addTextBody(key, value);

        } );

        URI uri= buildUri("/search");

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }


    public String update(String email, UploadablePerson updated_data) throws Exception{

        MultipartEntityBuilder builder= MultipartEntityBuilder.create();

        //BUILD UP POST DATA WITH NON-EMPTY FIELDS
        if( !updated_data.getFirst_name().isEmpty() ){
            builder.addTextBody("first_name", updated_data.getFirst_name());
        }

        if( !updated_data.getLast_name().isEmpty() ){
            builder.addTextBody("last_name", updated_data.getLast_name());
        }

        if( !updated_data.getAddress().isEmpty() ){
            builder.addTextBody("address", updated_data.getAddress());
        }

        if( !updated_data.getPassword().isEmpty() ){
            builder.addTextBody("password", updated_data.getPassword() );
        }

        if( !updated_data.getNew_password().isEmpty() ){
            builder.addTextBody("new_password", updated_data.getNew_password());
        }

        Gson gson= new Gson();

        if( updated_data.getPhone_numbers() != null ){
            builder.addTextBody("phone_numbers", gson.toJson(updated_data.getPhone_numbers(), new TypeToken<List<String>>(){}.getType()));
        }


        URI uri= buildUri("/my_account");

        //SETUP HTTP POST REQUEST
        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);


    }

}
