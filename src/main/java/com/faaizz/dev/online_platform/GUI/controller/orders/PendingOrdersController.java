package com.faaizz.dev.online_platform.GUI.controller.orders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.Main;
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
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PendingOrdersController extends GenericOrdersController {

    public void initialize() throws Exception {

        super.initialize();

        Staff current_staff = InstanceData.getCurrentStaff();

        //

        Map<String, String> post_data = new HashMap<>();
        post_data.put("staff_email", current_staff.getEmail());

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                loadPendingOrders(post_data, 1);
            }
        } );


    }


    private int current_page;
    private Map<String, String> current_post_data;

    /**
     * Loads the orders assigned to current staff whose email is in
     * poat_data.get("staff_email")
     * 
     * @param post_data   MAKSHIFT Map<String, String> TO ALLOW COMPATIBILITY WITH
     *                    MainController's setupPagination() METHOD
     * @param page_number
     * @throws IOException
     */
    private void loadPendingOrders(Map<String, String> post_data, int page_number) {
        

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

            Runnable load_pending = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {

                            // PERFORM REQUEST
                            String matched_orders_string = order_resource.pending();

                            OrderCollection matched_orders = APIParser.getInstance()
                                    .parseMultiOrderResponse(matched_orders_string);

                            // IF NO ORDER IS FOUND
                            if (matched_orders.getOrders().size() <= 0) {
                                setLoadingOutcomeMessage("THERE ARE NO PENDING ORDERS.");
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
            load_pending.run();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void displayOrders(List<Order> orders, Meta page_meta, Map<String, String> post_data)
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
            StringBuilder image_urlSB= new StringBuilder().append("https://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(order_product.getImages().get(0));
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
        setupPagination(page_meta, post_data, this::loadPendingOrders);

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
            String est_del_date= (order.getEst_del_date()== null) ? "" : order.getEst_del_date().toString().split("T")[0];

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

            Label staff_L= new Label("STAFF IN CHARGE: " +  ((order.getStaff_email() != null) ? order.getStaff_email().toUpperCase() : "NONE" ));
            staff_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(staff_L);

            Label status_L= new Label("STATUS: " +  order.getStatus().toUpperCase());
            status_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(status_L);

            Label est_del_date_L= new Label("EST DEL DATE: " +  (est_del_date.isEmpty() ? "UNAVAILABLE" : est_del_date) );
            est_del_date_L.getStyleClass().add("small-body-font");
            details_vbox.getChildren().add(est_del_date_L);


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
