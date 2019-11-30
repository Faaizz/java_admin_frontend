package com.faaizz.dev.online_platform.GUI.controller.products;

import com.faaizz.dev.online_platform.GUI.controller.Main;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add extends Main {

    private static Map<String, Map<String, List<String>>> sections;

    @FXML
    private BorderPane root_border_pane;

    @FXML
    private ComboBox section_dropdown;
    @FXML
    private ComboBox sub_section_dropdown;
    @FXML
    private ComboBox category_dropdown;

    @FXML
    private Button add_size_button;
    private int sizes_count; // Keeps count of sizes added
    private List<TextField> size_textfields_list;
    private List<TextField> quantity_textfields_list;

    @FXML
    private TextField quantity_textfield;
    @FXML
    private TextField size_textfield;

    @FXML
    private VBox decoy_labels_vbox;
    @FXML
    private VBox size_and_quantity_vbox;

    @FXML
    private TextField name_textfield;
    @FXML
    private TextField brand_textfield;
    @FXML
    private TextField color_textfield;
    @FXML
    private TextField price_textfield;
    @FXML
    private TextField material_textfield;

    @FXML
    private TextArea description_text_area;

    private List<File> image_files_list;

    @FXML
    private Button image_one_filechooser;
    @FXML
    private Button image_two_filechooser;
    @FXML
    private Button image_three_filechooser;

    /*
     * =============================================================================
     * ===========
     */
    /* I N I T I A L I Z E */

    public void initialize() {

        /*
         * =============================================================================
         * ===========
         */
        /* S E C T I O N S S U B S E C T I O N S C A T E G O R I E S */

        // ORGANISE SECTIONS, SUB SECTIONS, AND CATEGORIES FOR THEIR RESPECTIVE
        // COMBOBOXES
        List<String> male_clothing_options = FXCollections.observableArrayList("T-Shirts", "Shorts", "Shirts",
                "Trousers", "Sweatshirts & Hoodies", "Sweaters, Jackets, & Coats", "Underwear");
        List<String> female_clothing_options = FXCollections.observableArrayList("Tops", "Dresses", "Skirts",
                "Leggings & Vests", "Shorts", "Shirts", "Trousers", "Sweatshirts & Hoodies",
                "Sweaters, Jackets, & Coats", "Underwear & Lingerie");

        Map<String, List<String>> clothing_section = new HashMap<>();
        clothing_section.put("male", male_clothing_options);
        clothing_section.put("female", female_clothing_options);

        List<String> male_shoes_options = FXCollections.observableArrayList("Oxford", "Loafers", "Sneakers", "Boots",
                "Sandals & Slippers");
        List<String> female_shoes_options = FXCollections.observableArrayList("Flats", "Heels & Pumps",
                "Sandals & Slippers", "Sneakers", "Boots");

        Map<String, List<String>> shoes_section = new HashMap<>();
        shoes_section.put("male", male_shoes_options);
        shoes_section.put("female", female_shoes_options);

        sections = new HashMap<>();
        sections.put("clothing", clothing_section);
        sections.put("shoes", shoes_section);
        sections.put("accessories", null);
        sections.put("bags & watches", null);

        List<String> sections_list = FXCollections.observableArrayList("clothing", "shoes", "accessories",
                "bags & watches");

        // Set categories as ComboBox options
        section_dropdown.getItems().clear();
        section_dropdown.getItems().addAll(sections_list);

        // Select the first item by default
        section_dropdown.getSelectionModel().selectFirst();

        /*
         * =============================================================================
         * ===========
         */
        /* S I Z E S A N D Q U A N T I T Y */

        sizes_count = 1;
        size_textfields_list = new ArrayList<>();
        quantity_textfields_list = new ArrayList<>();

        // Add existing size and quantity TextFields to respective lists
        size_textfields_list.add(size_textfield);
        quantity_textfields_list.add(quantity_textfield);

        /*
         * =============================================================================
         * ===========
         */
        image_files_list = new ArrayList<>();

    }

    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleSectionChange() {

        // Get selected section string
        String selected_section = (String) section_dropdown.getSelectionModel().getSelectedItem();

        // Check if selected section has an underlying Map<String, List<String>>
        if (sections.get(selected_section) != null) {
            // If there's an underlying list of sub sections, set it as underlying map keys
            // for the sub_section_dropdown
            sub_section_dropdown.getItems().setAll(sections.get(selected_section).keySet());
        }
        // Otherwise, set section name as sub_section
        else {
            sub_section_dropdown.getItems().setAll(selected_section);
        }

        // Select first item by default
        sub_section_dropdown.getSelectionModel().selectFirst();


    }

    @FXML
    public void handleSub_sectionChange() {

        // Get selected section string
        String selected_section = (String) section_dropdown.getSelectionModel().getSelectedItem();

        // Get selected sub section
        String selected_sub_section = (String) sub_section_dropdown.getSelectionModel().getSelectedItem();

        // Check if selected sub_section has an underlying List of categories
        if ((sections.get(selected_section) != null)
                && (sections.get(selected_section).get(selected_sub_section) != null)) {
            // Set category_dropdown items as the underlying list
            category_dropdown.getItems().setAll(sections.get(selected_section).get(selected_sub_section));
        }
        // Otherwise, set category as section name
        else {
            category_dropdown.getItems().setAll(selected_section);
        }

        // Select first category by default
        category_dropdown.getSelectionModel().selectFirst();

    }

    @FXML
    public void handleAddSize() {

        // Create Additional Qunatity label
        Label additional_qty_label = new Label();
        additional_qty_label.setText("Quantity: ");
        // Set CSS style
        additional_qty_label.getStyleClass().add("mid-body-font");
        // Set min and prf height
        additional_qty_label.setMinHeight(30);
        additional_qty_label.setPrefHeight(30);
        // Set padding
        additional_qty_label.setPadding(new Insets(0, 15, 0, 25));

        // Create decoy placeholder Label for decoy_labels_vbox
        Label additional_decoy_label = new Label();
        additional_decoy_label.setText(" ");
        // Set CSS style
        additional_decoy_label.getStyleClass().add("mid-body-font");
        // Set min and prf height
        additional_decoy_label.setMinHeight(30);
        additional_decoy_label.setPrefHeight(30);

        // Create additional size textfield
        TextField additional_size = new TextField();
        // Set CSS Style
        additional_size.getStyleClass().add("big-body-textfield");
        // Set min and prf height
        additional_size.setMinHeight(30);
        additional_size.setPrefHeight(30);
        // Set min and pref width
        additional_size.setMinWidth(70);
        additional_size.setPrefWidth(70);

        // Create additional quantity textfield
        TextField additional_qty = new TextField();
        // Set CSS Style
        additional_qty.getStyleClass().add("big-body-textfield");
        // Set min and prf height
        additional_qty.setMinHeight(30);
        additional_qty.setPrefHeight(30);
        // Set min and pref width
        additional_qty.setMinWidth(70);
        additional_qty.setPrefWidth(70);

        // Create additional HBox
        HBox additional_hbox = new HBox();
        // Add the items to the hbox
        additional_hbox.getChildren().addAll(additional_size, additional_qty_label, additional_qty);
        // Set Padding
        additional_hbox.setPadding(new Insets(5, 0, 0, 0));

        // Add decoy Label to decoy_labels_vbox
        decoy_labels_vbox.getChildren().add(additional_decoy_label);

        // Add hbox to scene before image_one_filechooser
        size_and_quantity_vbox.getChildren().add(additional_hbox);

        // Add new size textfield to List of textfields
        size_textfields_list.add(additional_size);

        // Add new quantity textfield to List of quantity textfields
        quantity_textfields_list.add(additional_qty);

    }

    @FXML
    public void handleFileChooser(Event event) {

        // Get button that triggered the event
        Button button = (Button) event.getSource();
        String file_number = button.getId();

        // Create FileChooser
        FileChooser fileChooser = new FileChooser();

        List<String> extentions = new ArrayList<>();
        extentions.add("*.bmp");
        extentions.add("*.png");
        extentions.add("*.jpeg");
        extentions.add("*.jpg");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (bmp, png, jpeg)",
                extentions);

        fileChooser.getExtensionFilters().add(extFilter);

        File temp_file = fileChooser.showOpenDialog(root_border_pane.getScene().getWindow());

        // Add file to image_files_list
        image_files_list.add(temp_file);

        // Set button text to file name
        button.setText(temp_file.getName());

    }

    /*
     * =============================================================================
     * ===========
     */
    /* A D D P R O D U C T */
    @FXML
    public void handleAddProduct() throws IOException {

        final int[] validation_problems = {0}; // Integer to keep count of invalid entries. Array is used because of lambda compatibility
        final String CSS_RED_BORDERS= "red_borders";

        /*========================================================================================*/
        // VALIDATE TEXTFEILDS
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

            // If empty, add CSS CSS_RED_BORDERS class
            if(textfield.getText().isEmpty()){
                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                // Increment validation_problems
                validation_problems[0]++;
            }

            // If it has text, remove red_borders class
            else{
                textfield.getStyleClass().remove(CSS_RED_BORDERS);
            }

        } );

        // VALIDATE NUMBERS
        List<TextField> expect_numbers= new ArrayList<>();
        expect_numbers.add(price_textfield);
        // Add all quantity textfields
        quantity_textfields_list.forEach( textField -> {
            expect_numbers.add(textField);
        } );

        // Loop through expect_numbers
        expect_numbers.forEach( textfield ->{

            try{
                // Try to parse text content to number
                Double.valueOf(textfield.getText());
            }catch(NumberFormatException e){
                // Set red_borders css class
                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                // Increment validation_problems
                validation_problems[0]++;
            }

        } );

        // VALIDATE DESCRIPTION TEXTAREA
        if(description_text_area.getText().isEmpty()){
            // Add CSS red_borders class
            if(!description_text_area.getStyleClass().contains(CSS_RED_BORDERS)){
                description_text_area.getStyleClass().add(CSS_RED_BORDERS);
            }
            
            // Increment validation_problems
            validation_problems[0]++;
        }else{
            // Remove CSS red_borders class
            description_text_area.getStyleClass().remove(CSS_RED_BORDERS);
        }

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

        // VALIDATE FILE UPLOAD BUTTONS
        List<Button> upload_buttons= new ArrayList<>();
        upload_buttons.add(image_one_filechooser);
        upload_buttons.add(image_two_filechooser);
        upload_buttons.add(image_three_filechooser);

        // Loop through, check if file has been selected, if not set CSS red borders
        upload_buttons.forEach( button ->{
            if(button.getText().contains("Browse")){
                if(!button.getStyleClass().contains(CSS_RED_BORDERS)){
                    button.getStyleClass().add(CSS_RED_BORDERS);
                }                
                // Increment validation_problems
                validation_problems[0]++;
            }else{
                // Otherwise remove CSS red borders
                button.getStyleClass().remove(CSS_RED_BORDERS);
            }
        } );


        /*========================================================================================*/
        if(validation_problems[0] <=0){
            /*  S   U   C   C   E   S   S       V   A   L   I   D   A   T   I   O   N */

            // SETUP PRODUCT RESOURCE
            ProductResource productResource= new ProductResource(SettingsData.getBase_URL(), SettingsData.getApi_path(), SettingsData.getApi_token());

            // SETUP SIZES AND CORRESPONDING QUANTITIES
            List<Map<String, String>> options= new ArrayList<>();

            // Get number of sizes
            int sizes_index= size_textfields_list.size();

            // Loop through sizes
            for(int i= 0; i < sizes_index; i++){

                // Setup Map of size-quantit pair
                Map<String, String> temp_map= new HashMap<>();
                // Set size field
                temp_map.put("size", size_textfields_list.get(i).getText());
                // Set quantity field
                temp_map.put("quantity", quantity_textfields_list.get(i).getText());

                // Add to options list
                options.add(temp_map);

            }


            // CREATE UploadableProduct INSTANCE
            UploadableProduct to_upload= new UploadableProduct(
                name_textfield.getText(),
                brand_textfield.getText(),
                description_text_area.getText(),
                (String)section_dropdown.getSelectionModel().getSelectedItem(),
                (String)sub_section_dropdown.getSelectionModel().getSelectedItem(),
                (String)category_dropdown.getSelectionModel().getSelectedItem(),
                color_textfield.getText(),
                price_textfield.getText(),
                material_textfield.getText(), options,
                image_files_list.get(0), image_files_list.get(1), image_files_list.get(2)
            );

            // DISPLAY LOADING DIALOG
            // Create dialog
            Dialog mini_dialog= new Dialog();
            mini_dialog.initOwner(root_border_pane.getScene().getWindow());
            // Create FXML Loader
            FXMLLoader mini_dialog_loader= new FXMLLoader();
            mini_dialog_loader.setLocation(getClass().getResource("../../view/dialogs/mini_dialog.fxml"));
            // Load FXML
            mini_dialog.getDialogPane().setContent(mini_dialog_loader.load());
            mini_dialog.initStyle(StageStyle.UNDECORATED);
            // Get Dialog Controller
            MiniDialogController mini_dialog_controller= mini_dialog_loader.getController();
            mini_dialog_controller.setDialog_text_label("Loading...");
            mini_dialog.show();

            // ADD PRODUCT TO DATABASE IN SEPERATE THREAD
            Runnable add_product_runnable= new Runnable() {
                @Override
                public void run() {
                    Platform.runLater( ()->{
                        try {
                            productResource.add(to_upload);
                            // If successful, remove loading dialog
                            mini_dialog.hide();
                        } catch (Exception e) {
                            mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());

                        }
                    } );
                }
            };

            // ATTEMPT ADD
            add_product_runnable.run();




        }


    }



}
