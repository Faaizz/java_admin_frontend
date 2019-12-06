package com.faaizz.dev.online_platform.GUI.controller.products;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageProductsController extends GenericProductController {

    @FXML
    protected TextField min_price_textfield;
    @FXML
    protected TextField max_price_textfield;
    @FXML
    protected Button search_products_button;

    public void initialize() throws Exception {
        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();
    }


    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleSearchProduct() throws IOException {
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
                                //GRAB NUMBER OF PAGES
                                int no_of_pages= matched_products.getMeta().getLast_page();
                                mini_dialog_controller.setDialog_text_label(String.valueOf(matched_products.getProducts().size()));
                            }

                        } catch (Exception e) {
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

    public boolean validateSearchFields(){

        final int[] validation_problems = {0}; // Integer to keep count of invalid entries. Array is used because of lambda compatibility
        final String CSS_RED_BORDERS= "red_borders";

        /*========================================================================================*/
        // VALIDATE PRICE FIELDS AND SECTION DROPDOWNS
        List<TextField> textfields= new ArrayList<>();

        textfields.add(min_price_textfield);
        textfields.add(max_price_textfield);

        // VALIDATE NUMBERS
        List<TextField> expect_numbers= new ArrayList<>();
        expect_numbers.add(min_price_textfield);
        expect_numbers.add(max_price_textfield);

        // Loop through expect_numbers
        expect_numbers.forEach( textfield ->{

            try{
                if(!textfield.getText().isEmpty()){
                    // Try to parse text content to number
                    Double.valueOf(textfield.getText());
                }
            }catch(NumberFormatException e){
                // Set red_borders css class
                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                // Increment validation_problems
                validation_problems[0]++;
            }

        } );


        // VALIDATE DROPDOWNS
        List<ComboBox> dropdowns= new ArrayList<>();
        dropdowns.add(section_dropdown);
        dropdowns.add(sub_section_dropdown);
        dropdowns.add(category_dropdown);

        // Loop through dropdowns
        dropdowns.forEach( dropdown ->{
            // If no item is selected, add CSS red_borders class
            if(dropdown.getSelectionModel().getSelectedItem() == null){
                if(!dropdown.getStyleClass().contains(CSS_RED_BORDERS)){
                    dropdown.getStyleClass().add(CSS_RED_BORDERS);
                }

                // Increment validation_problems
                validation_problems[0]++;
            }else{
                // Remove CSS red_borders
                dropdown.getStyleClass().remove(CSS_RED_BORDERS);
            }
        } );

        return (validation_problems[0] <=0);
    }


}
