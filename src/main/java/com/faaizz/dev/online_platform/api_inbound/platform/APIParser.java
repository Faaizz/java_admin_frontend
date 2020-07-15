package com.faaizz.dev.online_platform.api_inbound.platform;

import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.Trend;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.collection.OrderCollection;
import com.faaizz.dev.online_platform.api_inbound.model.supplement.*;
import com.faaizz.dev.online_platform.api_inbound.model.Customer;
import com.faaizz.dev.online_platform.api_inbound.model.Order;
import com.faaizz.dev.online_platform.api_inbound.model.collection.CustomerCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.StaffCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.TrendCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIParser {

    /**
     * This is the main Class. It provides methods with which to parses JSON data from API to Java Classes. 
     * It uses a Singleton implementation.
     */

    private static APIParser instance;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public static APIParser getInstance(){
        return instance;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */
    private APIParser(){

    }

    static{
        instance= new APIParser();
    }

    /*========================================================================================*/
    /* P   R   O   D   U   C   T   */

    /**
     * Parse single Product JSON response into <code>Product</code> object
     * @param json
     * @return
     */
    public Product parseSingleProductResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        JsonObject data= resObject.getAsJsonObject("data");

        return parseProduct(data);

    }

    /**
     * Parse JSON String into <code>Product</code> object
     * @param data
     * @return
     */
    private Product parseProduct(JsonObject data){

        Gson gson= new Gson();

        //Intermediate object to enable proper parsing
        ProductTrans intermediate= gson.fromJson(data, ProductTrans.class);

        //PARSE Product Images
        JsonArray imgsObj= JsonParser.parseString(intermediate.getImages()).getAsJsonArray();

        Type imgsTypeToken= new TypeToken<List<String>>(){}.getType();
        List<String> imgsList= gson.fromJson(imgsObj, imgsTypeToken);

        //PARSE ProductOptions
        JsonArray optionsObj= JsonParser.parseString(intermediate.getOptions()).getAsJsonArray();

        Type optionTypeToken= new TypeToken<List<ProductOption>>(){}.getType();
        List<ProductOption> optionsList= gson.fromJson(optionsObj, optionTypeToken);

        //Append parts to create Product object
        Product resultProduct= new Product(
                intermediate.getId(),
                intermediate.getName(),
                intermediate.getBrand(),
                intermediate.getDescription(),
                intermediate.getSection(),
                intermediate.getSub_section(),
                intermediate.getCategory(),
                intermediate.getPrice(),
                intermediate.getColors(),
                intermediate.getMaterial(),
                imgsList,
                optionsList
        );

        return resultProduct;

    }

    /**
     * Parses multi Product JSON response
     * @param json
     * @return
     */
    public ProductCollection parseMultiProductResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        //Array of products
        JsonArray data= resObject.getAsJsonArray("data");

        //List of Products
        List<Product> products= new ArrayList<>();

        //Parse each Product and add to list of products
        data.forEach( productJson -> {

            products.add(parseProduct(productJson.getAsJsonObject()));

        });

        //LINKS
        Links links= getLinks(resObject);

        //META
        Meta meta= getMeta(resObject);

        //Create ProductCollection and return it
        ProductCollection productCollection= new ProductCollection(products, meta, links);

        return productCollection;

    }

    /*========================================================================================*/
    /* S   T   A   F   F   */

    /**
     * Parse single Staff JSON response into <code>Staff</code> object
     * @param json
     * @return
     */
    public Staff parseSingleStaffResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        JsonObject data= resObject.getAsJsonObject("data");

        return parseStaff(data);

    }

    /**
     * Parse JSON String into <code>Staff</code> object
     * @param data
     * @return
     */
    private Staff parseStaff(JsonObject data){

        Gson gson= new Gson();

        //Intermediate object to enable proper parsing
        StaffTrans intermediate= gson.fromJson(data, StaffTrans.class);

        //PARSE Staff phone numbers
        JsonArray phoneObj= JsonParser.parseString(intermediate.getPhone_numbers()).getAsJsonArray();

        Type phoneTypeToken= new TypeToken<List<String>>(){}.getType();
        List<String> phone_numbers= gson.fromJson(phoneObj, phoneTypeToken);

        //PARSE Staff privilege level
        String privString= intermediate.getPrivilege_level().trim();
        //Use a switch to check privilege level
        StaffPrivilegeLevel privilegeLevel;
        switch(privString){
            //IF STAFF IS ADMIN, SET TO ADMIN
            case "admin": privilegeLevel= StaffPrivilegeLevel.ADMIN;
                        break;
            //OTHERWISE, SET TO ORDINARY STAFF
            default: privilegeLevel= StaffPrivilegeLevel.STAFF;
        }

        //Append parts to create Staff object
        Staff resultStaff= new Staff(
                intermediate.getFirst_name(),
                intermediate.getLast_name(),
                intermediate.getEmail(),
                intermediate.getAddress(),
                intermediate.getGender(),
                phone_numbers,
                privilegeLevel,
                intermediate.getPending_orders()
        );

        return resultStaff;

    }

    /**
     *
     * @param json
     * @return
     */
    public StaffCollection parseMultiStaffResponse(String json){

        Gson gson= new Gson();

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        //Array of products
        JsonArray data= resObject.getAsJsonArray("data");

        //List of Products
        List<Staff> staffs= new ArrayList<>();

        //Parse each Staff and add to list of staff
        data.forEach( staffJson -> {

            staffs.add(parseStaff(staffJson.getAsJsonObject()));

        });

        //LINKS
        Links links= getLinks(resObject);

        //META
        Meta meta= getMeta(resObject);

        //Create ProductCollection and return it
        StaffCollection staffCollection= new StaffCollection(staffs, meta, links);
        ;
        return staffCollection;

    }


    /*========================================================================================*/
    /* C   U   S   T   O   M   E   R   */

    /**
     * Parse single Customer JSON response into <code>Customer</code> object
     * @param json
     */
    public Customer parseSingleCustomerResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        JsonObject data= resObject.getAsJsonObject("data");

        return parseCustomer(data);

    }

    /**
     * Parse JSON String into <code>Customer</code> object
     * @param data
     * @return
     */
    private Customer parseCustomer(JsonObject data){

        Gson gson= new Gson();

        //Intermediate object to enable proper parsing
        CustomerTrans intermediate= gson.fromJson(data, CustomerTrans.class);

        //PARSE Customer phone numbers
        JsonArray phoneObj= JsonParser.parseString(intermediate.getPhone_numbers()).getAsJsonArray();

        Type phoneTypeToken= new TypeToken<List<String>>(){}.getType();
        List<String> phone_numbers= gson.fromJson(phoneObj, phoneTypeToken);


        //Append parts to create Customer object
        Customer resultCustomer= new Customer(
                intermediate.getFirst_name(),
                intermediate.getLast_name(),
                intermediate.getEmail(),
                intermediate.getAddress(),
                intermediate.getGender(),
                phone_numbers,
                intermediate.getPending_orders(),
                intermediate.getActivation_status(),
                intermediate.getNewsletters()
        );

        return resultCustomer;

    }

    public CustomerCollection parseMultiCustomerResponse(String json){

        Gson gson= new Gson();

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        //Array of products
        JsonArray data= resObject.getAsJsonArray("data");

        //List of Products
        List<Customer> customers= new ArrayList<>();

        //Parse each Staff and add to list of staff
        data.forEach( customerJson -> {

            customers.add(parseCustomer(customerJson.getAsJsonObject()));

        });

        //LINKS
        Links links= getLinks(resObject);

        //META
        Meta meta= getMeta(resObject);

        //Create ProductCollection and return it
        CustomerCollection customerCollection= new CustomerCollection(customers, meta, links);

        return customerCollection;

    }


    /*========================================================================================*/
    /* O   R   D   E   R   S   */

    public Order parseSingleOrderResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        JsonObject data= resObject.getAsJsonObject("data");

        return parseOrder(data);

    }

    private LocalDateTime handleDates(String dateString){

        LocalDateTime dateOutput;

        if(dateString == null || dateString.isEmpty()){
            dateOutput= null;
        }
        else{

            //REPLACE WHITESPACE SEPARATOR WITH "T" eg "2019-09-07 14:38:35" BECOMES "2019-09-07T14:38:35"
            dateString= dateString.replace(" ", "T");

            //CREATE LocalDateTime OBJECT FROM STRING
            try{
                dateOutput= LocalDateTime.parse(dateString);
            }catch (DateTimeParseException e){
                //FAILED, SET TO NULL
                dateOutput= null;
            }

        }

        return dateOutput;

    }

    private Order parseOrder(JsonObject data){

        Gson gson= new Gson();

        //Intermediate object to enable proper parsing
        OrderTrans intermediate= gson.fromJson(data, OrderTrans.class);

        // Parse Order Products
        JsonArray ord_prods= JsonParser.parseString(intermediate.getProducts()).getAsJsonArray();

        Type prodsTypeToken= new TypeToken<List<Map<String,String>>>(){}.getType();
        List<Map<String,String>> prodsList= gson.fromJson(ord_prods, prodsTypeToken);

        //PARSE DATES
        LocalDateTime est_del_date= handleDates(intermediate.getEst_del_date());
        LocalDateTime failure_date= handleDates(intermediate.getFailure_date());
        LocalDateTime delivery_date= handleDates(intermediate.getDelivery_date());
        LocalDateTime created_at= handleDates(intermediate.getCreated_at());
        LocalDateTime updated_at= handleDates(intermediate.getUpdated_at());


        //Append parts to create Order object
        Order resultOrder= new Order(
                intermediate.getId(),
                prodsList,
                intermediate.getCustomer_email(),
                intermediate.getReference(),
                intermediate.getAmount(),
                intermediate.getAmount_paid(),
                intermediate.getStaff_email(),
                intermediate.getStatus(),
                est_del_date,
                failure_date,
                intermediate.getFailure_cause(),
                delivery_date,
                created_at,
                updated_at
        );
        return resultOrder;

    }


    public OrderCollection parseMultiOrderResponse(String json){

        Gson gson= new Gson();

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        //Array of orders
        JsonArray data= resObject.getAsJsonArray("data");

        //List of Products
        List<Order> orders= new ArrayList<>();

        //Parse each Product and add to list of products
        data.forEach( orderJson -> {

            orders.add(parseOrder(orderJson.getAsJsonObject()));

        });

        //LINKS
        Links links= getLinks(resObject);

        //META
        Meta meta= getMeta(resObject);

        //Create ProductCollection and return it
        OrderCollection orderCollection= new OrderCollection(orders, meta, links);

        return orderCollection;

    }

    /*========================================================================================*/
    /* T    R   E   N   D  */

    /**
     * Parse JSON String into <code>Trend</code> object
     * @param data
     * @return
     */
    public Trend parseTrend(JsonObject data){
        
        Gson gson= new Gson();

        // Intermediate object to enable proper parsing
        TrendTrans intermediate= gson.fromJson(data, TrendTrans.class);

        // Parse Trend images
        JsonArray imgsObj= JsonParser.parseString(intermediate.getImages()).getAsJsonArray();

        Type imgsTypeToken= new TypeToken<List<String>>(){}.getType();
        List<String> imgsList= gson.fromJson(imgsObj, imgsTypeToken);

        // Merge parts to create Trend object
        Trend resultTrend= new Trend(
            intermediate.getId(),
            intermediate.getName(),
            intermediate.getDescription(),
            intermediate.getGender(),
            intermediate.getProductsNumber(),
            imgsList
        );

        return resultTrend;

    }

    /**
     * Parse single Trend JSON response into <code>Trend</code> object
     * @param String json
     * @return Trend
     */
    public Trend parseSIngleTrendResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        JsonObject data= resObject.getAsJsonObject("data");

        return parseTrend(data);
    }

    /**
     * Parse multi Trend JSON response
     * @param String json
     * @return TrendCollection
     */
    public TrendCollection parseMultiTrendResponse(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        //Array of trends
        JsonArray data= resObject.getAsJsonArray("data");

        //List of trends
        List<Trend> trends= new ArrayList<>();

        //Parse each Trend and add to list of trends
        data.forEach( trendJson -> {

            trends.add(parseTrend(trendJson.getAsJsonObject()));

        });

        //LINKS
        Links links= getLinks(resObject);

        //META
        Meta meta= getMeta(resObject);

        // Create TrendCollection and return it
        TrendCollection trendCollection= new TrendCollection(trends, meta, links);

        return trendCollection;
        

    }


    public Meta getResponseMeta(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        return getMeta(resObject);

    }

    private Meta getMeta(JsonObject response_object){

        Gson gson= new Gson();

        //META
        JsonObject metaJson= response_object.getAsJsonObject("meta");
        //Parse as Meta object
        Meta meta= gson.fromJson(metaJson, Meta.class);

        return meta;

    }

    public Links getResponseLinks(String json){

        JsonElement resElement= JsonParser.parseString(json);
        JsonObject resObject= resElement.getAsJsonObject();

        return getLinks(resObject);

    }

    private Links getLinks(JsonObject response_object){

        Gson gson= new Gson();

        JsonObject linksJson= response_object.getAsJsonObject("links");
        //Parse as Links object
        Links links= gson.fromJson(linksJson, Links.class);

        return links;

    }

}
