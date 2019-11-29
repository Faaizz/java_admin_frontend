package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class CustomerResource extends APIResource {

    public CustomerResource(String base_URL, String api_path, String api_token) {

        super(base_URL, api_path + "/customers", api_token);

    }


    /**
     * Log customer in
     * @param email
     * @param password
     * @param remember
     * @return
     * @throws Exception
     */
    public String login(String email, String password, String remember) throws Exception{

        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        builder.addTextBody("email", email);
        builder.addTextBody("password", password);
        builder.addTextBody("remember", remember);

        URI uri= buildUri("/login");

        HttpEntity postEntity= builder.build();

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(postEntity);

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
     * Logout ayuthenticated Customer by deleting the X-REMEMBER-CUSTOMER cookie
     */
    public boolean logout() throws Exception{

        //DELETE EXISTING CUSTOMER AUTHENTICATION COOKIE "X-REMEMBER"
        return CookieStore.removeCookie("X-REMEMBER-CUSTOMER");

    }

    /**
     * Get all registered customers
     * @return
     * @throws Exception
     */
    public String all() throws Exception{

        URI uri = buildUri("");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Add a new Customer to database
     * @param customer_to_add
     * @return
     * @throws Exception
     */
    public String add(UploadablePerson customer_to_add) throws Exception{

        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        builder.addTextBody("first_name", customer_to_add.getFirst_name());
        builder.addTextBody("last_name", customer_to_add.getLast_name());
        builder.addTextBody("password", customer_to_add.getPassword());
        builder.addTextBody("email", customer_to_add.getEmail());
        builder.addTextBody("address", customer_to_add.getAddress());
        builder.addTextBody("gender", customer_to_add.getGender());

        Gson gson= new Gson();
        builder.addTextBody("phone_numbers", gson.toJson(customer_to_add.getPhone_numbers(), new TypeToken<List<String>>(){}.getType()));

        URI uri= buildUri("");

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);


    }

    /**
     * Delete customer account with specified email
     * @param email
     * @throws Exception
     */
    public void delete(String email) throws Exception{

        URI uri = buildUri("/" + email);

        HttpDelete httpDelete= new HttpDelete(uri);

        CloseableHttpResponse response= this.executeRequest(httpDelete);

        this.handleResponse(response);

    }


    /**
     * Update Customer details
     * @param updated_customer
     * @param newsletters: Set to null or empty string to leave newsletter settings as is. Otherwise, specify "yes" or "no"
     * @return
     * @throws Exception
     */
    public String update(UploadablePerson updated_customer, String newsletters) throws Exception{

        //BUILD UP POST DATA
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();

        //INCLUDE ONLY NON-EMPTY FIELDS IN THE POST BODY
        if( !updated_customer.getFirst_name().isEmpty() ){
            builder.addTextBody("first_name", updated_customer.getFirst_name());
        }

        if( !updated_customer.getLast_name().isEmpty() ){
            builder.addTextBody("last_name", updated_customer.getLast_name());
        }

        if( !updated_customer.getAddress().isEmpty() ){
            builder.addTextBody("address", updated_customer.getAddress());
        }

        if( !updated_customer.getGender().isEmpty() ){
            builder.addTextBody("gender", updated_customer.getGender());
        }

        if( !updated_customer.getPassword().isEmpty() ){
            builder.addTextBody("password", updated_customer.getPassword());
        }

        if( !updated_customer.getNew_password().isEmpty() ){
            builder.addTextBody("new_password", updated_customer.getNew_password());
        }

        //PHONE NUMBERS
        Gson gson= new Gson();

        if( null != updated_customer.getPhone_numbers() ){
            builder.addTextBody("phone_numbers", gson.toJson(updated_customer.getPhone_numbers(), new TypeToken<List<String>>(){}.getType()));
        }

        //NEWSLETTERS
        if( null != newsletters && !newsletters.isEmpty() ){
            builder.addTextBody("newsletters",newsletters);
        }


        //SETUP POST REQUEST
        URI uri= buildUri("/my_account");

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }


    /** 
     * Return details of authenticateed Customer
     * @return
     * @throws Exception
     */
    public String myAccount() throws Exception{

        URI uri= buildUri("/my_account");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Get Customer with specified email
     * @param email
     * @return
     * @throws Exception
     */
    public String single(String email) throws Exception{

        StringBuilder tempSB= new StringBuilder();
        tempSB.append("/email").append("/").append(email);

        URI uri= buildUri(tempSB.toString());

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    public String search(Map<String, String> search_params) throws Exception{

        //BUILD POST DATA
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        search_params.forEach( (key, value) -> {

            builder.addTextBody(key, value);

        } );

        //SET UP POST REQUEST
        URI uri= buildUri("/search");

        HttpPost httpPost= new HttpPost(uri);

        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);
        
    }

}
