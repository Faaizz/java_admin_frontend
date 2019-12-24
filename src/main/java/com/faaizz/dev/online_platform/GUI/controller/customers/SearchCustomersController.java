package com.faaizz.dev.online_platform.GUI.controller.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.collection.CustomerCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.CustomerResource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SearchCustomersController extends GenericCustomersController {

    @FXML
    TextField first_textfield;
    @FXML
    TextField last_textfield;
    @FXML
    TextField address_textfield;
    @FXML
    TextField email_textfield;


    public void initialize() throws Exception {

        super.initialize();

    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S */

    @FXML
    protected void handleSearchCustomer(){

        // SETUP SEARCH PARAMETERS
        Map<String, String> post_data= new HashMap<>();

        // ADD NON-EMPTY FIELDS
        if(!first_textfield.getText().isEmpty()){
            post_data.put("first_name", first_textfield.getText());
        }

        if(!last_textfield.getText().isEmpty()){
            post_data.put("last_name", last_textfield.getText());
        }

        if(!address_textfield.getText().isEmpty()){
            post_data.put("address", address_textfield.getText());
        }

        if(!email_textfield.getText().isEmpty()){
            post_data.put("email", email_textfield.getText());
        }

        // PERFORM SEARCH
        try{

            int page_number= 1;

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

            Runnable search_customers = new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(() -> {

                        try {
                            // PERFORM REQUEST
                            String matched_customers_string = customer_resource.search(post_data);

                            CustomerCollection matched_customers = APIParser.getInstance()
                                    .parseMultiCustomerResponse(matched_customers_string);

                            // IF NO ORDER IS FOUND
                            if (matched_customers.getCustomers().size() <= 0) {
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("NO MATCHES FOUND.");
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
            search_customers.run();

        }catch(Exception e){
            e.printStackTrace();
        }


    }


}