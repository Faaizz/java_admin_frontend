package com.faaizz.dev.online_platform.api_outbound.platform;

import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductResource extends APIResource {

    public ProductResource(String base_URL, String api_path, String api_token) {

        super(base_URL, api_path + "/products", api_token);

    }


    /**
     * Returns all products
     * @return
     * @throws Exception
     */
    public String all() throws Exception{

        URI uri= buildUri("");

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Returns products of specified section
     * @param section
     * @return
     * @throws Exception
     */
    public String all(String section) throws Exception{

        URI uri= buildUri("/" + section);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Returns products of specified section and sub section
     * @param section
     * @param sub_section
     * @return
     * @throws Exception
     */
    public String all(String section, String sub_section) throws Exception{

        URI uri=  buildUri("/" + section + "/" + sub_section);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Returns products of specified section, sub section, and category
     * @param section
     * @param sub_section
     * @param category
     * @return
     * @throws Exception
     */
    public String all(String section, String sub_section, String category) throws Exception{

        URI uri=  buildUri("/" + section + "/" + sub_section + "/" + category);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Returns products added within the past 3 weeks
     * @return
     * @throws Exception
     */
    public String newIn() throws Exception {

        URI uri= buildUri("/new_in");

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    public String newIn(int weeks) throws Exception {

        URI uri= buildUri("/new_in" + "/" + String.valueOf(weeks));

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    public String newIn(int weeks, String section) throws Exception {

        URI uri= buildUri("/new_in" + "/" + String.valueOf(weeks) + "/" + section);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    public String newIn(int weeks, String section, String sub_section) throws Exception {

        URI uri= buildUri("/new_in" + "/" + String.valueOf(weeks) + "/"
                + section + "/" + sub_section);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    public String newIn(int weeks, String section, String sub_section, String category) throws Exception {

        URI uri= buildUri("/new_in" + "/" + String.valueOf(weeks) + "/"
                + section + "/" + sub_section + "/" + category);

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Returns product with the specified ID if it exists.
     * @param id
     * @return
     * @throws Exception
     */
    public String single(int id) throws Exception {

        URI uri= buildUri("/" + String.valueOf(id) );

        //CREATE REQUEST
        HttpGet httpGet= new HttpGet(uri);

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    public String search(Map<String, String> post_data) throws Exception {

        List<NameValuePair> nvps= new ArrayList<>();

        post_data.forEach( (key, value) -> {

            nvps.add(new BasicNameValuePair(key, value));

        } );

        URI uri= buildUri("/search" );
        
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }

    public String add(UploadableProduct product) throws Exception {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("image_one", product.getImage_one(), ContentType.IMAGE_PNG, "image_one.png");
        builder.addBinaryBody("image_two", product.getImage_two(), ContentType.IMAGE_PNG, "image_two.png");
        builder.addBinaryBody("image_three", product.getImage_three(), ContentType.IMAGE_JPEG, "image_three.png");

        builder.addTextBody("name", product.getName(), ContentType.TEXT_PLAIN);
        builder.addTextBody("brand", product.getBrand(), ContentType.TEXT_PLAIN);
        builder.addTextBody("description", product.getDescription(), ContentType.TEXT_PLAIN);
        builder.addTextBody("section", product.getSection(), ContentType.TEXT_PLAIN);
        builder.addTextBody("sub_section", product.getSub_section(), ContentType.TEXT_PLAIN);
        builder.addTextBody("category", product.getCategory(), ContentType.TEXT_PLAIN);
        builder.addTextBody("color", product.getColor(), ContentType.TEXT_PLAIN);
        builder.addTextBody("price", product.getPrice(), ContentType.TEXT_PLAIN);
        builder.addTextBody("material", product.getMaterial(), ContentType.TEXT_PLAIN);
        builder.addTextBody("options", product.getOptions(), ContentType.TEXT_PLAIN);

        URI uri= buildUri("" );

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(builder.build());

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }

    public String update(int product_id, UploadableProduct product) throws Exception {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        if(product.getImage_one() != null)
            builder.addBinaryBody("image_one", product.getImage_one(), ContentType.IMAGE_PNG, "image_one.png");

        if(product.getImage_two() != null)
            builder.addBinaryBody("image_two", product.getImage_two(), ContentType.IMAGE_PNG, "image_two.png");

        if(product.getImage_three() != null)
            builder.addBinaryBody("image_three", product.getImage_three(), ContentType.IMAGE_JPEG, "image_three.png");

        if( !(product.getName().isEmpty() || product.getName() == null) )
            builder.addTextBody("name", product.getName(), ContentType.TEXT_PLAIN);

        if( !(product.getBrand().isEmpty() || product.getBrand() == null) )
            builder.addTextBody("brand", product.getBrand(), ContentType.TEXT_PLAIN);

        if( !(product.getDescription().isEmpty() || product.getDescription() == null) )
            builder.addTextBody("description", product.getDescription(), ContentType.TEXT_PLAIN);

        if( !(product.getSection().isEmpty() || product.getSection() == null) )
            builder.addTextBody("section", product.getSection(), ContentType.TEXT_PLAIN);

        if( !(product.getSub_section().isEmpty() || product.getSub_section() == null) )
            builder.addTextBody("sub_section", product.getSub_section(), ContentType.TEXT_PLAIN);

        if( !(product.getCategory().isEmpty() || product.getCategory() == null) )
            builder.addTextBody("category", product.getCategory(), ContentType.TEXT_PLAIN);

        if( !(product.getColor().isEmpty() || product.getColor() == null) )
            builder.addTextBody("color", product.getColor(), ContentType.TEXT_PLAIN);

        if( !(product.getPrice().isEmpty() || product.getPrice() == null) )
            builder.addTextBody("price", product.getPrice(), ContentType.TEXT_PLAIN);

        if( !(product.getMaterial().isEmpty() || product.getMaterial() == null) )
            builder.addTextBody("material", product.getMaterial(), ContentType.TEXT_PLAIN);

        if( product.getOptions() != null )
            builder.addTextBody("options", product.getOptions(), ContentType.TEXT_PLAIN);


        URI uri= buildUri("/" + product_id );

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(builder.build());

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);


    }


    public void delete(int product_id) throws Exception {

        URI uri= buildUri("/" + product_id );

        HttpDelete httpDelete= new HttpDelete(uri);

        //EXECUTE REQUEST
        this.handleResponse(this.executeRequest(httpDelete));

    }


    public void massDelete(int[] ids) throws Exception {

        //CONVERT INTEGER ARRAY TO JSON
        Gson gson= new Gson();

        Type typeToken= new TypeToken<int[]>(){}.getType();

        String ids_json= gson.toJson(ids, typeToken);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("ids", ids_json, ContentType.TEXT_PLAIN);

        URI uri= buildUri("/mass_delete");

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(builder.build());

        //GET RESPONSE
        CloseableHttpResponse response= this.executeRequest(httpPost);

        this.handleResponse(response);


    }



}
