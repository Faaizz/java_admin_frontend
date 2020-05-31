package com.faaizz.dev.online_platform.GUI.controller.orders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Order;
import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.collection.OrderCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.OrderResource;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FailedOrdersController extends GenericOrdersController {

    public void initialize() throws Exception {

        super.initialize();

        Staff current_staff = InstanceData.getCurrentStaff();

        //

        Map<String, String> post_data = new HashMap<>();
        post_data.put("staff_email", current_staff.getEmail());

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                loadFailedOrders(post_data, 1);
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
    private void loadFailedOrders(Map<String, String> post_data, int page_number) {
        

        try{

            // SET CURRENT PAGE AND POST DATA TO ENABLE PAGE REFRESH
            this.current_page= page_number;

            // CREATE NEW ORDER RESOURCE
            OrderResource order_resource = new OrderResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SET REQUEST PAGE
            order_resource.setPage_number(page_number);

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller = showLoadingMiniDialog();

            // GET UNASSIGNED ORDERS

            Runnable failed = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {

                            // PERFORM REQUEST
                            String matched_orders_string = order_resource.failed();

                            OrderCollection matched_orders = APIParser.getInstance()
                                    .parseMultiOrderResponse(matched_orders_string);

                            // IF NO ORDER IS FOUND
                            if (matched_orders.getOrders().size() <= 0) {
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("THERE ARE NO FAILED ORDERS.");
                            }
                            // OTHERWISE
                            else {

                                // DISPLAY MATCHED PRODUCTS
                                displayOrders(matched_orders.getOrders(), matched_orders.getMeta(), post_data, mini_dialog_controller);

                            }

                        } catch (Exception e) {
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An Error Occurred\n" + e.getMessage());
                            e.printStackTrace();
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


    private void displayOrders(List<Order> orders, Meta page_meta, Map<String, String> post_data, MiniDialogController mini_dialog_controller)
            throws Exception {

        VBox topmost_vbox= new VBox();
        topmost_vbox.getStyleClass().add("general-background");
        topmost_vbox.setPadding(new Insets(10, 10, 10, 10));

        // CREATE A SingleOrder OBJECT FOR EACH MATCHED ORDER
        for( final Order order: orders ){

            // GET PRODUCT
            ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            String order_product_string= productResource.single(order.getProduct_id());
            Product order_product= APIParser.getInstance().parseSingleProductResponse(order_product_string);

            // GET PRODUCT IMAGE
            StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(order_product.getImages().get(0));
            Platform.runLater(
                new Runnable(){
                
                    @Override
                    public void run() {
                        // CREATE SINGLE ORDER HBox
                        HBox single_order= new SingleOrder(image_urlSB.toString(), order_product.getId(), order_product.getName(), order);
                        // ADD TO TOPMOST VBOX
                        topmost_vbox.getChildren().add(single_order);
                    }
                }
            );

        }

        // SET CONTENT PANE <content> AS TOPMOST VBOX
        content_scrollpane.setContent(topmost_vbox);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadFailedOrders);

        // REMOVE LOADING DIALOG
        mini_dialog_controller.handleExit();

    }

    /**
     * Refresh orders displayed. This should typically be done after updating an order.
     * Uses information of orders currently being displayed which is stored in the instance variables
     * current_orders, current_page_meta, and current_post_data
     */
    private void refreshOrders() throws Exception {
        loadFailedOrders(null, current_page);
    }

    class SingleOrder extends HBox{

        VBox level_one_vbox;
        VBox image_vbox;
        HBox level_one_hbox;
        ImageView imageview;
        Image image;
        VBox details_vbox;

        public SingleOrder(String image_url, int product_id, String product_name, Order order)
            {

            // SET CSS STYLE
            this.getStyleClass().addAll("sub-section-style", "transparent-background");
            this.setSpacing(30);
            this.setAlignment(Pos.CENTER);

            // PREPARE ORDER FOR RENDERING
            String order_date= order.getCreated_at().toString().split("T")[0];
            String failure_date= (order.getFailure_date()== null) ? "" : order.getFailure_date().toString().split("T")[0];

            level_one_vbox= new VBox();
            level_one_vbox.setSpacing(7);
            level_one_vbox.setMaxWidth(600);
            level_one_vbox.setPrefWidth(600);

            level_one_hbox= new HBox();
            level_one_hbox.setSpacing(7);
            level_one_hbox.setPadding(new Insets(10, 10, 10, 10));

            image_vbox= new VBox();
            image= new Image(image_url, 150, 0, true, false);
            imageview= new ImageView(image);
            
            image_vbox.getChildren().add(imageview);

            level_one_hbox.getChildren().add(image_vbox);

            details_vbox= new VBox();
            details_vbox.setSpacing(7);

            Label product_id_L= new Label(String.valueOf(product_id));
            product_id_L.getStyleClass().addAll("big-body-font", "boldened");
            details_vbox.getChildren().add(product_id_L);

            Label product_name_L= new Label(product_name.toUpperCase());
            product_name_L.getStyleClass().addAll("mid-body-font", "boldened");
            details_vbox.getChildren().add(product_name_L);

            Label quantity_L= new Label("QTY: " + String.valueOf(order.getProduct_quantity()));
            quantity_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(quantity_L);

            Label size_L= new Label("SIZE: " + order.getProduct_size().toUpperCase());
            size_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(size_L);

            Label date_L= new Label("ORDER DATE: " + order_date);
            date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(date_L);

            Label customer_email_L= new Label("CUSTOMER EMAIL: " +  order.getCustomer_email().toUpperCase());
            customer_email_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(customer_email_L);

            Label failure_reason_L= new Label("FAILURE REASON: " +  ((order.getFailure_cause() != null) ? order.getFailure_cause().toUpperCase() : "NONE" ));
            failure_reason_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(failure_reason_L);

            Label failure_date_L= new Label("FAILURE DATE: " +  (failure_date.isEmpty() ? "UNAVAILABLE" : failure_date) );
            failure_date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(failure_date_L);


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
