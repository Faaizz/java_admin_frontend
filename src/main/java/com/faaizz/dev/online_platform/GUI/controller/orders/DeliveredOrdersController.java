package com.faaizz.dev.online_platform.GUI.controller.orders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Customer;
import com.faaizz.dev.online_platform.api_inbound.model.Order;
import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.collection.OrderCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.CustomerResource;
import com.faaizz.dev.online_platform.api_outbound.platform.OrderResource;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;



public class DeliveredOrdersController extends GenericOrdersController {

    public void initialize() throws Exception {

        super.initialize();

        Staff current_staff = InstanceData.getCurrentStaff();

        //

        Map<String, String> post_data = new HashMap<>();
        post_data.put("staff_email", current_staff.getEmail());

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                loadDeliveredOrders(post_data, 1);
            }
        } );


    }


    private int current_page;

    /**
     * Loads the orders assigned to current staff whose email is in
     * poat_data.get("staff_email")
     * 
     * @param post_data   MAKSHIFT Map<String, String> TO ALLOW COMPATIBILITY WITH
     *                    MainController's setupPagination() METHOD
     * @param page_number
     * @throws IOException
     */
    private void loadDeliveredOrders(Map<String, String> post_data, int page_number) {
        

        try{

            // SET CURRENT PAGE AND POST DATA TO ENABLE PAGE REFRESH
            this.current_page= page_number;

            // CREATE NEW ORDER RESOURCE
            OrderResource order_resource = new OrderResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SET REQUEST PAGE
            order_resource.setPage_number(page_number);

            // Show loading cursor
            Main.getStage().getScene().setCursor(Cursor.WAIT);

            // GET UNASSIGNED ORDERS

            Runnable failed = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {

                            // PERFORM REQUEST
                            String matched_orders_string = order_resource.delivered();

                            OrderCollection matched_orders = APIParser.getInstance()
                                    .parseMultiOrderResponse(matched_orders_string);

                            // IF NO ORDER IS FOUND
                            if (matched_orders.getOrders().size() <= 0) {
                                setLoadingOutcomeMessage("THERE ARE NO DELIVERED ORDERS.");
                                displayLoadingMessageInScrollpane();
                            }
                            // OTHERWISE
                            else {

                                // DISPLAY MATCHED PRODUCTS
                                displayOrders(matched_orders.getOrders(), matched_orders.getMeta(), post_data);

                            }

                        } catch (Exception e) {
                            setLoadingOutcomeMessage("An Error Occurred\n" + e.getMessage());
                            displayLoadingMessageInScrollpane();
                            e.printStackTrace();
                        }
                        finally{
                            // Hide loading cursor
                            Main.getStage().getScene().setCursor(Cursor.DEFAULT);
                        }

                    });

                }
            };

            // LOAD WORKLOAD
            failed.run();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void displayOrders(final List<Order> orders, Meta page_meta, Map<String, String> post_data)
            throws Exception {

        VBox topmost_vbox= new VBox();
        topmost_vbox.getStyleClass().add("general-background");
        topmost_vbox.setPadding(new Insets(10, 10, 10, 10));

        // CREATE A SingleOrder OBJECT FOR EACH MATCHED ORDER
        for( final Order order: orders ){

           // GET PRODUCT
            // ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            // String order_product_string= productResource.single(order.getProduct_id());
            // Product order_product= APIParser.getInstance().parseSingleProductResponse(order_product_string);

            // GET CUSTOMER
            CustomerResource customerResource= new CustomerResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            String order_customer_string= customerResource.single(order.getCustomer_email());
            Customer order_customer= APIParser.getInstance().parseSingleCustomerResponse(order_customer_string);

            // GET PRODUCT IMAGE
            // StringBuilder image_urlSB= new StringBuilder().append("https://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(order_product.getImages().get(0));
            Platform.runLater(
                new Runnable(){
                
                    @Override
                    public void run() {
                        try{
                            // CREATE SINGLE ORDER HBox
                            HBox single_order= new SingleOrder(order, order_customer);
                            // ADD TO TOPMOST VBOX
                            topmost_vbox.getChildren().add(single_order);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            );

        }

        // SET CONTENT PANE <content> AS TOPMOST VBOX
        content_scrollpane.setContent(topmost_vbox);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadDeliveredOrders);

    }

    /**
     * Refresh orders displayed. This should typically be done after updating an order.
     * Uses information of orders currently being displayed which is stored in the instance variables
     * current_orders, current_page_meta, and current_post_data
     */
    private void refreshOrders() throws Exception {
        loadDeliveredOrders(null, current_page);
    }

    class SingleOrder extends HBox{

        VBox level_one_vbox;
        VBox image_vbox;
        HBox level_one_hbox;
        ImageView imageview;
        Image image;
        VBox details_vbox;

        public SingleOrder(Order order, Customer order_customer) throws NumberFormatException, Exception
            {
            // SET CSS STYLE
            this.getStyleClass().addAll("sub-section-style", "transparent-background");
            this.setSpacing(30);
            this.setAlignment(Pos.CENTER);

            // PREPARE ORDER FOR RENDERING
            String order_date= order.getCreated_at().toString().split("T")[0];
            String delivery_date= (order.getDelivery_date()== null) ? "" : order.getDelivery_date().toString().split("T")[0];

            level_one_vbox= new VBox();
            level_one_vbox.setSpacing(7);
            level_one_vbox.setMaxWidth(600);
            level_one_vbox.setPrefWidth(600);

            level_one_hbox= new HBox();
            level_one_hbox.setSpacing(7);
            level_one_hbox.setPadding(new Insets(10, 10, 10, 10));

            // image_vbox= new VBox();
            // image= new Image(image_url, 150, 0, true, false);
            // imageview= new ImageView(image);
            
            // image_vbox.getChildren().add(imageview);

            // level_one_hbox.getChildren().add(image_vbox);

            details_vbox= new VBox();
            details_vbox.setSpacing(7);

            // Order ID
            Label order_id_L= new Label("Order ID: " + String.valueOf(order.getId()));
            order_id_L.getStyleClass().addAll("big-body-font", "boldened");
            details_vbox.getChildren().add(order_id_L);

            // Loop through order products
            for(Map<String,String> ord_prod: order.getProducts()){

                // GET PRODUCT
               
                ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
                String order_product_string= productResource.single(Integer.parseInt(ord_prod.get("id")));
                Product order_product= APIParser.getInstance().parseSingleProductResponse(order_product_string);

                Label product_id_L= new Label(ord_prod.get("id"));
                product_id_L.getStyleClass().addAll("mid-body-font", "boldened");
                details_vbox.getChildren().add(product_id_L);

                Label product_name_L= new Label(order_product.getName().toUpperCase());
                product_name_L.getStyleClass().addAll("small-body-font", "boldened");
                details_vbox.getChildren().add(product_name_L);

                Label color_L= new Label("COLOR: " + ord_prod.get("color").toUpperCase());
                color_L.getStyleClass().add("small-body-font");
                details_vbox.getChildren().add(color_L);

                Label quantity_L= new Label("QTY: " + ord_prod.get("quantity"));
                quantity_L.getStyleClass().add("small-body-font");
                details_vbox.getChildren().add(quantity_L);

                Label size_L= new Label("SIZE: " + ord_prod.get("size").toUpperCase());
                size_L.getStyleClass().add("small-body-font");
                details_vbox.getChildren().add(size_L);
            }

            Label date_L= new Label("ORDER DATE: " + order_date);
            date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(date_L);

            Label customer_email_L= new Label("CUSTOMER EMAIL: " +  order.getCustomer_email().toUpperCase());
            customer_email_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(customer_email_L);

            Label customer_address_L= new Label("CUSTOMER ADDRESS: " +  order_customer.getAddress().toUpperCase());
            customer_address_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(customer_address_L);

            Label customer_phone_L= new Label("CUSTOMER PHONE: " +  order_customer.getPhone_numbers().get(0));
            customer_phone_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(customer_phone_L);

            Label delivery_date_L= new Label("DELIVERY DATE: " +  (delivery_date.isEmpty() ? "UNAVAILABLE" : delivery_date) );
            delivery_date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(delivery_date_L);


            level_one_hbox.getChildren().add(details_vbox);

            level_one_vbox.getChildren().add(level_one_hbox);


            // CREATE SEPERATION BAR
            Label sep_bar= new Label("\'");
            sep_bar.setMaxWidth(1000000);
            sep_bar.setPrefHeight(2);
            sep_bar.setMinHeight(2);
            sep_bar.getStyleClass().add("darker-background");

            level_one_vbox.getChildren().add(sep_bar);

            this.getChildren().add(level_one_vbox);


        }

    }

}
