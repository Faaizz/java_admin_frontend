package com.faaizz.dev.online_platform.GUI.controller.products;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.exceptions.ResponseException;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class RemoveProductsController extends ManageProductsController {

    @FXML
    protected TextField brand_textfield;

    public void initialize() throws Exception {
        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // Set "ENTER" key event on brand_textfield to trigger login attempt
        brand_textfield.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    handleDeleteProducts();;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );
    }

    @FXML
    public void handleDeleteProducts() throws IOException {

        // VALIDATE ENTRIES
        if(validateSearchFields()){

            // SUCCESS VALIDATION
            // SETUP PRODUCT RESOURCE
            ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            // SETUP SEARCH PARAMETERS
            Map<String, String> post_data= new HashMap<>();

            post_data.put("section", (String)section_dropdown.getSelectionModel().getSelectedItem());
            post_data.put("sub_section", (String)sub_section_dropdown.getSelectionModel().getSelectedItem());
            post_data .put("category", (String)category_dropdown.getSelectionModel().getSelectedItem());

            if(!min_price_textfield.getText().isEmpty()){
                post_data.put("min_price", min_price_textfield.getText());
            }

            if(!max_price_textfield.getText().isEmpty()){
                post_data.put("max_price", max_price_textfield.getText());
            }


            if(!name_textfield.getText().isEmpty()){
                post_data.put("name", name_textfield.getText());
            }
            
            if(!brand_textfield.getText().isEmpty()){
                post_data.put("brand", brand_textfield.getText());
            }

            if(!color_textfield.getText().isEmpty()){
                post_data.put("color", color_textfield.getText());
            }

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            Runnable search_runnable= new Runnable() {
                @Override
                public void run() {
                    Platform.runLater( ()->{
                        try {

                            //PERFORM SEARCH
                            String matched_products_string= productResource.search(post_data);

                            //PARSE RESPONSE
                            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

                            mini_dialog_controller.enableCloseButton();

                            // IF NO PRODUCT IS FOUND
                            if(matched_products.getProducts().size() <= 0){
                                mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY PRODUCT WITH THE SPECIFIED SEARCH PARAMETERS.");
                            }
                            // OTHERWISE
                            else{
                                
                                //DELETE MATCHED PRODUCTS
                                int no_of_pages= matched_products.getMeta().getLast_page();

                                // LIST TO STORE IDS OF MATCHED PRODUCTS
                                List<Integer> ids= new ArrayList<>();

                                // IF RESULT IS A SINGLE PAGE, GET PRODUCT ID OF MATCHED PRODUCTS
                                if(no_of_pages == 1){

                                    matched_products.getProducts().forEach( product-> {
                                        ids.add(product.getId());
                                    });

                                }
                                // OTHERWISE
                                else{

                                    // CHECK FIRST PAGE
                                    matched_products.getProducts().forEach( product-> {
                                        ids.add(product.getId());
                                    });

                                    // THEN LOOP THROUGH EVERY OTHER PAGE
                                    for(int page_number= 2; page_number<= no_of_pages; page_number++){

                                        // SET RESULT PAGE
                                        productResource.setPage_number(page_number);

                                        // GET NEW PRODUCTS
                                        matched_products_string= productResource.search(post_data);

                                        // PARSE PRODUCTS
                                        matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

                                        // ADD IDS TO LIST
                                        matched_products.getProducts().forEach( product-> {
                                            ids.add(product.getId());
                                        });

                                    }


                                }


                                // PERFORM MASS DELETE
                                int[] ids_ints= new int[ids.size()];
                                // GET IDS INTO AN Int ARRAY
                                for(int i=0; i<ids.size(); i++){
                                    ids_ints[i]= ids.get(i);
                                }

                                // RESET PAGE TO 1
                                productResource.setPage_number(1);
                                // PERFORM MASS DELETE
                                productResource.massDelete(ids_ints);
                                
                                mini_dialog_controller.setDialog_text_label(ids_ints.length + " products deleted successfully.");
                            }


                        }
                        catch (Exception e) {
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An Error Occurred\n" + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            };

            // EXECUTE RUNNABLE
            search_runnable.run();

        }
    }


}