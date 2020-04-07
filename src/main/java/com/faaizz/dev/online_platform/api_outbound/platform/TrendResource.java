package com.faaizz.dev.online_platform.api_outbound.platform;

import java.net.URI;

import com.faaizz.dev.online_platform.api_outbound.model.UploadableTrend;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class TrendResource extends APIResource {

    public TrendResource(String base_URL, String api_path, String api_token) {
        super(base_URL, api_path + "/trends", api_token);
    }

    /**
     * Returns all Trends
     * @return JSON
     * @throws Exception
     */
    public String all() throws Exception{

        URI uri= buildUri("/gender");

        // CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);
        // GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Return Trends by gender
     * @return JSON
     * @throws Exception
     */
    public String all(String gender) throws Exception{

        URI uri= buildUri("/gender/" + gender);

        // CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);
        // GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Return trend with the specified ID if it exists.
     * @param int id
     * @return JSON
     * @throws Exception
     */
    public String single(int id) throws Exception{

        URI uri= buildUri("/" + String.valueOf(id));

        // CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Return products of the trend with specified ID
     * @param int id
     * @return JSON
     * @throws Exception
     */
    public String products(int id) throws Exception{

        URI uri= buildUri("/" + String.valueOf(id) + "/products");

        // CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Add new Trend
     * @param UploadableTrend trend
     * @return JSON
     * @throws Exception
     */
    public String add(UploadableTrend trend) throws Exception{

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("image_one", trend.getImage_one(), ContentType.IMAGE_PNG, "image_one.png");

        builder.addTextBody("name", trend.getName(), ContentType.TEXT_PLAIN);
        builder.addTextBody("gender", trend.getGender(), ContentType.TEXT_PLAIN);
        builder.addTextBody("description", trend.getDescription(), ContentType.TEXT_PLAIN);

        URI uri= buildUri("" );

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(builder.build());

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);
    }

    /**
     * Update existing Trend with specified ID
     * @param int id
     * @param UploadableTrend trend
     * @return JSON
     * @throws Exception
     */
    public String update(int id, UploadableTrend trend) throws Exception{

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(trend.getImage_one() != null)
            builder.addBinaryBody("image_one", trend.getImage_one(), ContentType.IMAGE_PNG, "image_one.png");

        if( !(trend.getName().isEmpty() || trend.getName() == null) )
            builder.addTextBody("name", trend.getName(), ContentType.TEXT_PLAIN);

        if( !(trend.getGender().isEmpty() || trend.getGender() == null) )
            builder.addTextBody("gender", trend.getGender(), ContentType.TEXT_PLAIN);

        if( !(trend.getDescription().isEmpty() || trend.getDescription() == null) )
            builder.addTextBody("description", trend.getDescription(), ContentType.TEXT_PLAIN);

            URI uri= buildUri("/" + id );

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(builder.build());
    
            //GET RESPONSE
            CloseableHttpResponse response= this.executeRequest(httpPost);
    
            return this.handleResponse(response);

    }

    /**
     * Delete trend with sprecified ID
     * @param int id
     * @return 
     * @throws Exception
     */
    public void delete(int id) throws Exception {

        URI uri= buildUri("/" + id );

        HttpDelete httpDelete= new HttpDelete(uri);

        //EXECUTE REQUEST
        this.handleResponse(this.executeRequest(httpDelete));

    }



}