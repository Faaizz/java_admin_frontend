package com.faaizz.dev.online_platform.GUI.controller.orders;

import com.faaizz.dev.online_platform.api_outbound.model.UploadableOrder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.faaizz.dev.online_platform.api_inbound.model.collection.StaffCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.OrderResource;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;
import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class UnassignedOrdersController extends GenericOrdersController {

    protected ObservableList<Staff> staff_list= null;

    public void initialize() throws Exception {

        super.initialize();

        // INITIALIZE staff_list
        staff_list= FXCollections.observableArrayList();

        Staff current_staff = InstanceData.getCurrentStaff();

        //

        Map<String, String> post_data = new HashMap<>();

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                try{
                    loadUnassignedOrders(post_data, 1);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } );


    }


    private int current_page;

    /**
     * Loads unassigned orders
     * 
     * @param post_data   MAKSHIFT Map<String, String> TO ALLOW COMPATIBILITY WITH
     *                    MainController's setupPagination() METHOD
     * @param page_number
     * @throws Exception
     * @throws IOException
     */
    private void loadUnassignedOrders(Map<String, String> post_data, int page_number) {
        

        try{

            // SET CURRENT PAGE AND POST DATA TO ENABLE PAGE REFRESH
            this.current_page= page_number;

            // CREATE NEW STAFF RESOURCE
            StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // CREATE NEW ORDER RESOURCE
            OrderResource order_resource = new OrderResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SET REQUEST PAGE
            order_resource.setPage_number(page_number);

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller = showLoadingMiniDialog();

            // GET UNASSIGNED ORDERS

            Runnable load_unassigned = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {

                            // LOAD STAFF LIST
                            String matched_staff_string= staff_resource.all();

                            StaffCollection matched_staff_collection= APIParser.getInstance().parseMultiStaffResponse(matched_staff_string);

                            // SET staff_list  TO LOADED STAFF LIST
                            staff_list= FXCollections.observableArrayList(matched_staff_collection.getStaffs());

                            // PERFORM REQUEST
                            String matched_orders_string = order_resource.unassigned();

                            OrderCollection matched_orders = APIParser.getInstance()
                                    .parseMultiOrderResponse(matched_orders_string);

                            // IF NO ORDER IS FOUND
                            if (matched_orders.getOrders().size() <= 0) {
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("THERE ARE NO UNASSIGNED ORDERS.");
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
            load_unassigned.run();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void displayOrders(List<Order> orders, Meta page_meta, Map<String, String> post_data, MiniDialogController mini_dialog_controller)
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
            

            // CREATE SINGLE ORDER HBox
            HBox single_order= new SingleOrder(image_imput_stream, order_product.getId(), order_product.getName(), order);
                                    
            // ADD TO TOPMOST VBOX
            topmost_vbox.getChildren().add(single_order);

        }

        // SET CONTENT PANE <content> AS TOPMOST VBOX
        content_scrollpane.setContent(topmost_vbox);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadUnassignedOrders);

        // REMOVE LOADING DIALOG
        mini_dialog_controller.handleExit();

    }

    /**
     * Refresh orders displayed. This should typically be done after updating an order.
     * Uses information of orders currently being displayed which is stored in the instance variables
     * current_orders, current_page_meta, and current_post_data
     */
    private void refreshOrders() throws Exception {
        loadUnassignedOrders(null, current_page);
    }

    class SingleOrder extends HBox{

        VBox level_one_vbox;
        VBox image_vbox;
        HBox level_one_hbox;
        ImageView imageview;
        Image image;
        VBox details_vbox;

        public SingleOrder(InputStream image_is, int product_id, String product_name, Order order)
            {

            // SET CSS STYLE
            this.getStyleClass().addAll("sub-section-style", "transparent-background");
            this.setSpacing(30);
            this.setAlignment(Pos.CENTER);

            // PREPARE ORDER FOR RENDERING
            String order_date= order.getCreated_at().toString().split("T")[0];
            String est_del_date= (order.getEst_del_date()== null) ? "" : order.getEst_del_date().toString().split("T")[0];

            level_one_vbox= new VBox();
            level_one_vbox.setSpacing(7);
            level_one_vbox.setMaxWidth(600);
            level_one_vbox.setPrefWidth(600);

            level_one_hbox= new HBox();
            level_one_hbox.setSpacing(7);
            level_one_hbox.setPadding(new Insets(10, 10, 10, 10));

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

            Label status_L= new Label("STATUS: " +  order.getStatus().toUpperCase());
            status_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(status_L);


            // CREATE UPDATE PANEL HBOX
            HBox update_panel= new UpdatePanel(order.getId());
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

        VBox update_vbox;
        ComboBox<Staff> staff_select_CB;

        VBox confirm_vbox;
        Button assign_staff_BT;

        int order_id;


        public UpdatePanel(int order_id){

            // INITIALIZE ORDER
            this.order_id= order_id;

            this.setMaxWidth(1000000);
            this.setSpacing(10);
            this.setPadding(new Insets(5, 5, 5, 5));

            // UPDATE VBOX
            update_vbox= new VBox();
            update_vbox.setMaxWidth(100000);
            update_vbox.setSpacing(7);

            staff_select_CB= new ComboBox<>();
            staff_select_CB.setItems(staff_list);
            staff_select_CB.setMinHeight(25);
            staff_select_CB.getStyleClass().addAll("big-body-combobox", "small-body-font");
            staff_select_CB.setPrefHeight(25);
            staff_select_CB.setMaxWidth(100000);
            update_vbox.getChildren().add(staff_select_CB);

            this.getChildren().add(update_vbox);


            // CONFIRM VBOX
            confirm_vbox= new VBox();
            confirm_vbox.setSpacing(7);
            confirm_vbox.setMaxWidth(1000000);

            assign_staff_BT= new Button("Assign Staff");
            assign_staff_BT.setMinHeight(25);
            assign_staff_BT.setPrefHeight(25);
            assign_staff_BT.setMaxWidth(100000);
            assign_staff_BT.getStyleClass().addAll("file-chooser-button", "small-body-font");
            assign_staff_BT.setOnAction( new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {

                    // CHECK IF A STAFF HAS BEEN SELECTED IN staff_select_CB 
                    Staff selected_staff= staff_select_CB.getSelectionModel().getSelectedItem();

                    // IF WE HAVE A VALID DATE
                    if(selected_staff != null){

                        try{

                            // SHOW LOADING DIALOG
                            MiniDialogController dialog_controller= showLoadingMiniDialog();
                            // CREATE ORDER RESOURCE
                            OrderResource order_resource= new OrderResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

                            Runnable set_del_date_run= new Runnable(){
                            
                                @Override
                                public void run() {
                                    Platform.runLater(()->{

                                        try{
                                            //ATTEMPT STATUS UPDATE
                                            UploadableOrder updated_order= new UploadableOrder(0, "", 0, "");
                                            // SET STATUS DATE
                                            updated_order.setStaff_email(selected_staff.getEmail());

                                            order_resource.update(updated_order, order_id);

                                            // SUCCESS UPDATE
                                            // REFRESH ORDERS DISPLAYED
                                            refreshOrders();
                                            // ENABLE DIALOG CLOSE
                                            dialog_controller.enableCloseButton();
                                            // DISPLAY SUCCESS MESSAGE
                                            dialog_controller.setDialog_text_label("Order Assigned Successful");


                                        } catch (Exception e) {
                                            // DISPLAY ERROR ON DIALOG
                                            dialog_controller.setDialog_text_label(e.getMessage());
                                            // ENABLE DIALOG CLOSE BUTTON
                                            dialog_controller.enableCloseButton();

                                            e.printStackTrace();
                                        }

                                    });
                                }
                            };

                            set_del_date_run.run();

                        
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }

                }
                
            } );
            confirm_vbox.getChildren().add(assign_staff_BT);

            this.getChildren().add(confirm_vbox);



        }

    }

}
