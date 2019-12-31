package com.faaizz.dev.online_platform.GUI.controller.products;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductController extends GenericProductController {

    /*
     * =============================================================================
     * ===========
     */
    /* I N I T I A L I Z E */

    public void initialize() throws Exception {

        super.initialize();

        // Select the first item by default
        section_dropdown.getSelectionModel().selectFirst();
        handleSectionChange(null);
        handleSub_sectionChange(null);

        // Reset image_files_list
        image_files_map= new HashMap<>();

    }

    /*
     * =============================================================================
     * ===========
     */
    /* A D D P R O D U C T */
    @FXML
    public void handleAddProduct() throws IOException {

        // VALIDATE INPUT

        if(super.validateEntries()) {

            /*  S   U   C   C   E   S   S       V   A   L   I   D   A   T   I   O   N */

            // SETUP PRODUCT RESOURCE
            ProductResource productResource = new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SETUP SIZES AND CORRESPONDING QUANTITIES
            List<Map<String, String>> options = new ArrayList<>();

            // Get number of sizes
            int sizes_index = size_textfields_list.size();

            // Loop through sizes
            for (int i = 0; i < sizes_index; i++) {

                // Setup Map of size-quantit pair
                Map<String, String> temp_map = new HashMap<>();
                // Set size field
                temp_map.put("size", size_textfields_list.get(i).getText());
                // Set quantity field
                temp_map.put("quantity", quantity_textfields_list.get(i).getText());

                // Add to options list
                options.add(temp_map);

            }


            // CREATE UploadableProduct INSTANCE
            UploadableProduct to_upload = new UploadableProduct(
                    name_textfield.getText(),
                    brand_textfield.getText(),
                    description_text_area.getText(),
                    (String) section_dropdown.getSelectionModel().getSelectedItem(),
                    (String) sub_section_dropdown.getSelectionModel().getSelectedItem(),
                    (String) category_dropdown.getSelectionModel().getSelectedItem(),
                    color_textfield.getText(),
                    price_textfield.getText(),
                    material_textfield.getText(), options,
                    (image_files_map.containsKey("one") ? image_files_map.get("one"): null),
                    (image_files_map.containsKey("two") ? image_files_map.get("two"): null),
                    (image_files_map.containsKey("three") ? image_files_map.get("three"): null)
            );

            // Call inherited method from MainController class to display loading dialog
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            // ADD PRODUCT TO DATABASE IN SEPARATE THREAD
            Runnable add_product_runnable = new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        try {
                            productResource.add(to_upload);
                            // If successful, remove loading dialog
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("Product Added Successfully");

                            // Set all TextFields and Text Areas to blank
                            List<TextField> textfields= new ArrayList<>();

                            textfields.add(name_textfield);
                            textfields.add(brand_textfield);
                            textfields.add(color_textfield);
                            textfields.add(price_textfield);
                            textfields.add(material_textfield);
                            // Add all size textfields
                            size_textfields_list.forEach( size_textfield ->{
                                textfields.add(size_textfield);
                            } );
                            // Add all quantity textfields
                            quantity_textfields_list.forEach( quantity_textfield ->{
                                textfields.add(quantity_textfield);
                            } );

                            // Loop through all textfields
                            textfields.forEach( textfield->{
                                textfield.setText("");
                            } );

                            //TextArea
                            description_text_area.setText("");

                            //Upload Buttons
                            List<Button> upload_buttons= new ArrayList<>();
                            upload_buttons.add(image_one_filechooser);
                            upload_buttons.add(image_two_filechooser);
                            upload_buttons.add(image_three_filechooser);

                            // Loop through, check if file has been selected, if not set CSS red borders
                            for(int i=0; i < upload_buttons.size(); i++){

                                switch(i){
                                    case 0: upload_buttons.get(i).setText("Image One");
                                        break;
                                    
                                    case 1: upload_buttons.get(i).setText("Image Two");
                                        break;

                                    case 2: upload_buttons.get(i).setText("Image Three");
                                        break;
                                }

                            }

                            // Reset image_files_list
                            image_files_map= new HashMap<>();

                            // Reset ScrollPane to top
                            content_scrollpane.setVvalue(0);
                            

                        } catch (Exception e) {
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                        }
                    });
                }
            };

            // ATTEMPT ADD
            add_product_runnable.run();
        }
    }
    

}
