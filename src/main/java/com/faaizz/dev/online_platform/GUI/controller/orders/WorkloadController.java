package com.faaizz.dev.online_platform.GUI.controller.orders;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.InstanceData;
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

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class WorkloadController extends GenericOrdersController {

    public void initialize() throws Exception {

        super.initialize();

        Staff current_staff = InstanceData.getCurrentStaff();

        //

        Map<String, String> post_data = new HashMap<>();
        post_data.put("staff_email", current_staff.getEmail());

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                loadWorkload(post_data, 1);
            }
        } );


    }

    /**
     * Loads the orders assigned to current staff whose email is in
     * poat_data.get("staff_email")
     * 
     * @param post_data   MAKSHIFT Map<String, String> TO ALLOW COMPATIBILITY WITH
     *                    MainController's setupPagination() METHOD
     * @param page_number
     * @throws IOException
     */
    private void loadWorkload(Map<String, String> post_data, int page_number){

        // CREATE NEW ORDER RESOURCE
        OrderResource order_resource = new OrderResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

        try{

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller = showLoadingMiniDialog();

            // GET ORDERS ASSIGNED TO THE CURRENT STAFF

            Runnable load_workload = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {

                            // PERFORM REQUEST
                            String matched_orders_string = order_resource.staff(post_data.get("staff_email"));

                            OrderCollection matched_orders = APIParser.getInstance()
                                    .parseMultiOrderResponse(matched_orders_string);

                            // IF NO ORDER IS FOUND
                            if (matched_orders.getOrders().size() <= 0) {
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("YOU DO NOT HAVE ANY PENDING ORDERS.");
                            }
                            // OTHERWISE
                            else {

                                // DISPLAY MATCHED PRODUCTS
                                displayOrders(matched_orders.getOrders(), matched_orders.getMeta(), post_data);

                                // REMOVE LOADING DIALOG
                                mini_dialog_controller.handleExit();

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
            load_workload.run();

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void displayOrders(List<Order> orders, Meta page_meta, Map<String, String> post_data)
            throws Exception {

        VBox topmost_vbox= new VBox();
        topmost_vbox.getStyleClass().add("general-background");
        topmost_vbox.setPadding(new Insets(10, 10, 10, 10));

        // CREATE A SingleOrder OBH=JECT FOR EACH MATCHED ORDER
        for( Order order: orders ){

            // GET PRODUCT
            ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            String order_product_string= productResource.single(order.getProduct_id());
            Product order_product= APIParser.getInstance().parseSingleProductResponse(order_product_string);

            // GET PRODUCT IMAGE
            StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(order_product.getImages().get(0));
            // GET IMAGE AS BufferedStream
            CloseableHttpClient http_client= HttpClients.createDefault();
            HttpGet http_get= new HttpGet(image_urlSB.toString());

            CloseableHttpResponse response = http_client.execute(http_get);
            HttpEntity entity = response.getEntity();

            BufferedInputStream image_imput_stream = new BufferedInputStream(entity.getContent());
            

            // PREPARE ORDER FOR RENDERING
            String order_date= order.getCreated_at().toString().split("T")[0];
            String est_del_date= (order.getEst_del_date()== null) ? "" : order.getEst_del_date().toString().split("T")[0];

            // CREATE SINGLE ORDER HBox
            HBox single_order= new SingleOrder(image_imput_stream, order_product.getId(), order_product.getName(), 
                                        order.getProduct_size(), order.getProduct_quantity(), order_date, order.getCustomer_email(), 
                                        order.getStatus(),est_del_date);
                                    
            // ADD TO TOPMOST VBOX
            topmost_vbox.getChildren().add(single_order);

        }

        // SET CONTENT PANE <content> AS TOPMOST VBOX
        content_scrollpane.setContent(topmost_vbox);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadWorkload);

    }

    class SingleOrder extends HBox{

        VBox level_one_vbox;
        VBox image_vbox;
        HBox level_one_hbox;
        ImageView imageview;
        Image image;
        VBox details_vbox;

        public SingleOrder(InputStream image_is, int product_id, String product_name, String size, int quantity, 
                            String date, String customer_email, String status, String est_del_date)
            {

            // SET CSS STYLE
            this.getStyleClass().addAll("sub-section-style", "transparent-background");
            this.setSpacing(30);
            this.setAlignment(Pos.CENTER);

            level_one_vbox= new VBox();
            level_one_vbox.setSpacing(7);

            level_one_hbox= new HBox();
            level_one_hbox.setSpacing(7);

            image_vbox= new VBox();
            image= new Image(image_is, 150, 0, true, false);
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

            Label quantity_L= new Label("QTY: " + String.valueOf(quantity));
            quantity_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(quantity_L);

            Label size_L= new Label("SIZE: " + size.toUpperCase());
            size_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(size_L);

            Label date_L= new Label("ORDER DATE: " + date);
            date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(date_L);

            Label customer_email_L= new Label("CUSTOMER EMAIL: " +  customer_email.toUpperCase());
            customer_email_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(customer_email_L);

            Label status_L= new Label("STATUS: " +  status.toUpperCase());
            status_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(status_L);

            Label est_del_date_L= new Label("EST DEL DATE: " +  (est_del_date.isEmpty() ? "UNAVAILABLE" : est_del_date) );
            est_del_date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(est_del_date_L);


            // CREATE UPDATE PANEL HBOX
            HBox update_panel= new UpdatePanel(status, est_del_date);
            details_vbox.getChildren().add(update_panel);


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

    class UpdatePanel extends HBox{

        private ObservableList<String> order_status;
        VBox update_vbox;
        TextField est_del_date_TF;
        ComboBox<String> status_CB;
        TextField failure_TF;

        VBox confirm_vbox;
        Button set_del_date_BT;
        Button update_status_BT;
        Button mark_failed_BT;


        public UpdatePanel(String status, String est_del_date){

            // SETUP ORDER STATUS
            order_status= FXCollections.observableArrayList();
            order_status.add("Pending");
            order_status.add("Delivered");
            order_status.add("Failed");

            this.setMaxWidth(1000000);
            this.setSpacing(10);

            // UPDATE VBOX
            update_vbox= new VBox();
            update_vbox.setMaxWidth(100000);
            update_vbox.setSpacing(7);

            // EST DEL DATE TEXTFIELD
            est_del_date_TF= new TextField(est_del_date);
            est_del_date_TF.getStyleClass().addAll("big-body-textfield", "small-body-font");
            est_del_date_TF.setPrefHeight(25);
            est_del_date_TF.setMinHeight(25);
            update_vbox.getChildren().add(est_del_date_TF);

            status_CB= new ComboBox<>();
            status_CB.setItems(order_status);
            status_CB.setMinHeight(25);
            status_CB.getStyleClass().addAll("big-body-combobox", "small-body-font");
            status_CB.setPrefHeight(25);
            status_CB.setMaxWidth(100000);
            if(order_status.contains(status)){
                status_CB.getSelectionModel().select(status);
            }
            update_vbox.getChildren().add(status_CB);
            failure_TF= new TextField();
            failure_TF.getStyleClass().addAll("big-body-textfield", "small-body-font");
            failure_TF.setMinHeight(25);
            failure_TF.setPrefHeight(25);
            update_vbox.getChildren().add(failure_TF);

            this.getChildren().add(update_vbox);


            // CONFIRM VBOX
            confirm_vbox= new VBox();
            confirm_vbox.setSpacing(7);
            confirm_vbox.setMaxWidth(1000000);

            set_del_date_BT= new Button("Set Est Delivery");
            set_del_date_BT.setMinHeight(25);
            set_del_date_BT.setPrefHeight(25);
            set_del_date_BT.setMaxWidth(100000);
            set_del_date_BT.getStyleClass().addAll("file-chooser-button", "image-view-button", "small-body-font");
            confirm_vbox.getChildren().add(set_del_date_BT);

            update_status_BT= new Button("Update Status");
            update_status_BT.setMinHeight(25);
            update_status_BT.setPrefHeight(25);
            update_status_BT.setMaxWidth(100000);
            update_status_BT.getStyleClass().addAll("file-chooser-button", "small-body-font");
            confirm_vbox.getChildren().add(update_status_BT);

            mark_failed_BT= new Button("Set as Failed");
            mark_failed_BT.setMinHeight(25);
            mark_failed_BT.setPrefHeight(25);
            mark_failed_BT.setMaxWidth(100000);
            mark_failed_BT.getStyleClass().addAll("file-chooser-button", "failed-button", "small-body-font");
            confirm_vbox.getChildren().add(mark_failed_BT);

            this.getChildren().add(confirm_vbox);



        }

    }

}
