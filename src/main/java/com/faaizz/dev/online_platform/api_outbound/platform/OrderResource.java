package com.faaizz.dev.online_platform.api_outbound.platform;

import java.net.URI;
import java.util.List;

import com.faaizz.dev.online_platform.api_outbound.model.UploadableOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class OrderResource extends APIResource {


    private StringBuilder tempSB;

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public OrderResource(String base_URL, String api_path, String api_token){

        super(base_URL, api_path + "/orders", api_token);

        tempSB= new StringBuilder();

    }

    /**
     * Get all pending orders
     * @return 
     * @throws Exception
     */
    public String pending() throws Exception{

        URI uri= buildUri("/pending");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);        

    }


    /**
     * Get all delivered orders
     * @return
     * @throws Exception
     */
    public String delivered() throws Exception{

        URI uri= buildUri("/delivered");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }

    /**
     * Get all failed requests
     * @return
     * @throws Exception
     */
    public String failed() throws Exception{

        URI uri= buildUri("/failed");

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Get orders assoicated with the customer with specified email
     * @param customer_email
     * @return
     * @throws Exception
     */
    public String customer(String customer_email) throws Exception{

        tempSB= new StringBuilder().append("/customer/").append(customer_email);

        URI uri= buildUri(tempSB.toString());

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Get orders assoicated with the staff with specified email
     * @param staff_email
     * @return
     * @throws Exception
     */
    public String staff(String staff_email) throws Exception{

        tempSB= new StringBuilder().append("/staff/").append(staff_email);

        URI uri= buildUri(tempSB.toString());

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Get orders assoicated with the product with specified id
     * @param product_id
     * @return
     * @throws Exception
     */
    public String product(int product_id) throws Exception{

        tempSB= new StringBuilder().append("/product/").append(product_id);

        URI uri= buildUri(tempSB.toString());

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);

    }


    /**
     * Get the order with specified order id
     * @param id
     * @return
     * @throws Exception
     */
    public String single(int id) throws Exception {

        tempSB= new StringBuilder().append("/").append(id);

        URI uri= buildUri(tempSB.toString());

        HttpGet httpGet= new HttpGet(uri);

        CloseableHttpResponse response= this.executeRequest(httpGet);

        return this.handleResponse(response);


    }


    /**
     * Add new order
     * @param order
     * @return
     * @throws Exception
     */
    public String add(UploadableOrder order) throws Exception{

        // BUILD UP POST DATA
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        
        builder.addTextBody("product_id", String.valueOf(order.getProduct_id()));
        builder.addTextBody("product_size", order.getProduct_size());
        builder.addTextBody("product_quantity", String.valueOf(order.getProduct_quantity()));
        builder.addTextBody("customer_email", order.getCustomer_email());

        // SETUP POST REQUEST
        URI uri= buildUri("");

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());
        
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);

    }


    /**
     * Update order with specified id
     * @param updated_order
     * @return
     * @throws Exception
     */
    public String update(UploadableOrder updated_order, int order_id) throws Exception{

        // BUILD UP POST DATA
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();

        // ONLY ADD NON-EMPTY FIELDS

        // SET STATUS TO DEFAULT (pending) IF NO STATUS IS SPECIFIED
        if( updated_order.getStatus().isEmpty() ){

            builder.addTextBody("status", "pending");

        }
        else{
            builder.addTextBody("status", updated_order.getStatus());
        }

        if( !updated_order.getEst_del_date().isEmpty() ){

            builder.addTextBody("est_del_date", updated_order.getEst_del_date());

        }

        if( !updated_order.getFailure_cause().isEmpty() ){

            builder.addTextBody("failure_cause", updated_order.getFailure_cause());

        }

        if( !updated_order.getFailure_date().isEmpty() ){

            builder.addTextBody("failure_date", updated_order.getFailure_date());

        }

        if( !updated_order.getDelivery_date().isEmpty() ){

            builder.addTextBody("delivery_date", updated_order.getDelivery_date());

        }

        if( !updated_order.getStaff_email().isEmpty() ){

            builder.addTextBody("staff_email", updated_order.getStaff_email());

        }


        // SETUP POST REQUEST
        tempSB= new StringBuilder().append("/update_status/").append(order_id);
        
        URI uri= buildUri(tempSB.toString());

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());
        
        CloseableHttpResponse response= this.executeRequest(httpPost);

        return this.handleResponse(response);


    }


    /**
     * Delete order with specified id
     * @param id
     * @throws Exception
     */
    public void delete(int id) throws Exception{

        URI uri= buildUri("/" + String.valueOf(id));

        HttpDelete httpDelete= new HttpDelete(uri);

        CloseableHttpResponse response= this.executeRequest(httpDelete);

        this.handleResponse(response);

    }


    /**
     * Delete all orders with their ids specified in the input array
     * @param ids
     * @throws Exception
     */
    public void mass_delete(List<Integer> ids) throws Exception{

        Gson gson= new Gson();
        
        MultipartEntityBuilder builder= MultipartEntityBuilder.create();
        builder.addTextBody("ids", gson.toJson(ids, new TypeToken<List<Integer>>(){}.getType()));

        URI uri= buildUri("/mass_delete");

        HttpPost httpPost= new HttpPost(uri);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response= this.executeRequest(httpPost);

        this.handleResponse(response);
        
    }



}