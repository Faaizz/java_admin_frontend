package com.faaizz.dev.online_platform.GUI.controller.customers;

import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;
import com.faaizz.dev.online_platform.api_inbound.model.Customer;
import com.faaizz.dev.online_platform.api_inbound.model.collection.CustomerCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.CustomerResource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenericCustomersController extends MainController {


    @FXML
    protected ScrollPane content_scrollpane;

    private final String VIEW= "view/customers/view.fxml";
    private final String SEARCH= "view/customers/search.fxml";


    public void initialize() throws Exception {

        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // SET CURRENT STAFF NAME
        super.setCurrentStaffName();

    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S */

    @FXML
    protected void handleRedirectToViewCustomers() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(VIEW);
    }

    @FXML
    protected void handleRedirectToSearchCustomers() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(SEARCH);
    }


    /*========================================================================================*/
    /*  D   I   S   P   L   A   Y       C   U   S   T   O   M   E   R   S */


    protected int current_page;

    /**
     * Loads unassigned customers
     * 
     * @param post_data   MAKSHIFT Map<String, String> TO ALLOW COMPATIBILITY WITH
     *                    MainController's setupPagination() METHOD
     * @param page_number
     * @throws Exception
     * @throws IOException
     */
    protected void loadCustomers(Map<String, String> post_data, int page_number) {
        

        try{

            // SET CURRENT PAGE AND POST DATA TO ENABLE PAGE REFRESH
            this.current_page= page_number;

            // VERIFY ADMIN
            verifyAdminAuthorization();

            // CREATE NEW ORDER RESOURCE
            CustomerResource customer_resource = new CustomerResource(SettingsData.getSettings().getBase_url(),
                SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SET REQUEST PAGE
            customer_resource.setPage_number(page_number);

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller = showLoadingMiniDialog();

            // GET UNASSIGNED customers

            Runnable load_customers = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {
                            // PERFORM REQUEST
                            String matched_customers_string = customer_resource.all();

                            CustomerCollection matched_customers = APIParser.getInstance()
                                    .parseMultiCustomerResponse(matched_customers_string);

                            // IF NO ORDER IS FOUND
                            if (matched_customers.getCustomers().size() <= 0) {
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("THERE ARE NO REGISTERED CUSTOMERS.");
                            }
                            // OTHERWISE
                            else {

                                // DISPLAY MATCHED PRODUCTS
                                displayCustomers(matched_customers.getCustomers(), matched_customers.getMeta(), post_data, mini_dialog_controller);

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
            load_customers.run();

        }catch(Exception e){
            e.printStackTrace();
        }

    }


    protected void displayCustomers(List<Customer> customers, Meta page_meta, Map<String, String> post_data, MiniDialogController mini_dialog_controller)
            throws Exception {

        VBox topmost_vbox= new VBox();
        topmost_vbox.getStyleClass().add("general-background");
        topmost_vbox.setPadding(new Insets(10, 10, 10, 10));

        // CREATE A SingleOrder OBJECT FOR EACH MATCHED CUSTOMER
        for( Customer customer: customers ){

            // CREATE SINGLE ORDER HBox
            HBox single_customer= new SingleCustomer(customer);
                                    
            // ADD TO TOPMOST VBOX
            topmost_vbox.getChildren().add(single_customer);

        }

        // SET CONTENT PANE <content> AS TOPMOST VBOX
        content_scrollpane.setContent(topmost_vbox);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadCustomers);

        // REMOVE LOADING DIALOG
        mini_dialog_controller.handleExit();

    }

    class SingleCustomer extends HBox{

        VBox level_one_vbox;
        VBox details_vbox;

        public SingleCustomer(Customer customer)
            {

            // SET CSS STYLE
            this.getStyleClass().addAll("sub-section-style", "transparent-background");
            this.setSpacing(30);
            this.setAlignment(Pos.CENTER);

            // PREPARE CUSTOMER FOR RENDERING

            level_one_vbox= new VBox();
            level_one_vbox.setSpacing(7);
            level_one_vbox.setPadding(new Insets(10, 10, 10, 10));
            level_one_vbox.setMaxWidth(600);
            level_one_vbox.setPrefWidth(600);

            details_vbox= new VBox();
            details_vbox.setSpacing(7);

            StringBuilder name_SB= new StringBuilder();
            name_SB.append(customer.getFirst_name()).append(" ").append(customer.getLast_name());
            Label name_L= new Label(name_SB.toString().toUpperCase());
            name_L.getStyleClass().addAll("mid-body-font", "boldened");
            name_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(name_L);

            Label email_L= new Label(customer.getEmail().toUpperCase());
            email_L.getStyleClass().addAll("mid-body-font");
            email_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(email_L);

            Label address_L= new Label(customer.getAddress().toUpperCase());
            address_L.getStyleClass().addAll("mid-body-font");
            address_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(address_L);

            Label gender_L= new Label(customer.getGender().toUpperCase());
            gender_L.getStyleClass().addAll("mid-body-font");
            gender_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(gender_L);

            StringBuilder phone_numbers= new StringBuilder();
            customer.getPhone_numbers().forEach(phone_number->{
                phone_numbers.append(phone_number).append(" ");
            });
            Label phone_L= new Label(phone_numbers.toString());
            phone_L.getStyleClass().addAll("mid-body-font");
            phone_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(phone_L);

            Label pending_orders_L= new Label("Pending Orders: " + String.valueOf(customer.getPending_orders()));
            pending_orders_L.getStyleClass().addAll("mid-body-font");
            pending_orders_L.setTextAlignment(TextAlignment.LEFT);
            details_vbox.getChildren().add(pending_orders_L);


            level_one_vbox.getChildren().add(details_vbox);


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
