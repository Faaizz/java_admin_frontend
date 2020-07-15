package com.faaizz.dev.online_platform.GUI.controller.products;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.controller.validators.Validators;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class GenericProductController extends ProductController {

    // PAGE PATHS
    private final String MANAGE_PRODUCTS = "view/products/manage.fxml";
    private final String ADD_PRODUCTS = "view/products/add.fxml";
    private final String REMOVE_PRODUCTS = "view/products/remove.fxml";
    private final String MANAGE_TRENDS = "view/products/trends/manage.fxml";
    private final String ADD_TRENDS = "view/products/trends/add.fxml";
    private final String REMOVE_TRENDS = "view/products/trends/remove.fxml";

    @FXML
    protected Button add_size_button;
    protected int sizes_count; // Keeps count of sizes added
    protected List<TextField> size_textfields_list;
    protected List<TextField> quantity_textfields_list;

    @FXML
    protected TextField quantity_textfield;
    @FXML
    protected TextField size_textfield;

    @FXML
    protected VBox decoy_labels_vbox;
    @FXML
    protected VBox size_and_quantity_vbox;

    @FXML
    protected TextField price_textfield;
    @FXML
    protected TextField material_textfield;

    @FXML
    protected TextArea description_text_area;

    protected Map<String, File> image_files_map;

    @FXML
    protected Button image_one_filechooser;
    @FXML
    protected Button image_two_filechooser;
    @FXML
    protected Button image_three_filechooser;

    @FXML
    protected ScrollPane content_scrollpane;

    public void initialize() throws Exception {

        // INITIALIZE MENU
        super.setupMenu();

        // SET "active-section-button" ON PRODUCTS
        products_button.getStyleClass().add("active-section-button");

        // SHOW MENU
        super.renderMenu();

        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // SET CURRENT STAFF NAME
        super.setCurrentStaffName();

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
        image_files_map = new HashMap<>();
    }

    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleAddSize(Event e) {

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

        // Create FileChooser
        FileChooser fileChooser = new FileChooser();

        List<String> extentions = new ArrayList<>();
        // extentions.add("*.bmp");
        // extentions.add("*.png");
        // extentions.add("*.jpeg");
        extentions.add("*.jpg");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (jpg)",
                extentions);

        fileChooser.getExtensionFilters().add(extFilter);

        File temp_file = fileChooser.showOpenDialog(root_border_pane.getScene().getWindow());

        if (temp_file != null) {

            // Check which file to add or edit
            if(button.getText().toLowerCase().contains("one")) {
                // add image file to image_files_map
                image_files_map.put("one", temp_file);

                // Append (selected) to button label
                button.setText("Image One (selected)");
            }
            else if(button.getText().toLowerCase().contains("two")) {
                // add image file to image_files_map
                image_files_map.put("two", temp_file);

                // Append (selected) to button label
                button.setText("Image Two (selected)");
            }
            else if(button.getText().toLowerCase().contains("three")) {
                // add image file to image_files_map
                image_files_map.put("three", temp_file);

                // Append (selected) to button label
                button.setText("Image Three (selected)");
            }

        }

    }


    @FXML
    protected void handleRedirectToAddProducts() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(ADD_PRODUCTS);    

    }


    @FXML
    protected void handleRedirectToManageProducts() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(MANAGE_PRODUCTS);    

    }


    @FXML
    protected void handleRedirectToRemoveProducts() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(REMOVE_PRODUCTS);    

    }

    @FXML
    protected void handleRedirectToAddTrends() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(ADD_TRENDS);    

    }


    @FXML
    protected void handleRedirectToManageTrends() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(MANAGE_TRENDS);    

    }


    @FXML
    protected void handleRedirectToRemoveTrends() throws IOException, AuthenticationException {

        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(REMOVE_TRENDS);    

    }



    /*========================================================================================*/

    public boolean validateEntries(){

        final int[] validation_problems = {0}; // Integer to keep count of invalid entries. Array is used because of lambda compatibility

        /*========================================================================================*/
        // VALIDATE TEXTFEILDS
        List<TextField> textfields= new ArrayList<>();

        textfields.add(name_textfield);
        textfields.add(brand_textfield);
        textfields.add(colors_textfield);
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

        // VALIDATE TEXTFIELDS
        Validators.validateTextFields(textfields, validation_problems);

        // VALIDATE NUMBERS
        List<TextField> price_textfields= new ArrayList<>();
        price_textfields.add(price_textfield);
        Validators.validatePrices(price_textfields, validation_problems);


        // Validate Quantities
        // Add all quantity textfields
        Validators.validateNumbers(quantity_textfields_list, validation_problems);

        // VALIDATE DESCRIPTION TEXTAREA
        List<TextArea> textareas= new ArrayList<>();
        textareas.add(description_text_area);
        Validators.validateTextAreas(textareas, validation_problems);

        // VALIDATE DROPDOWNS
        List<ComboBox> dropdowns= new ArrayList<>();
        dropdowns.add(section_dropdown);
        dropdowns.add(sub_section_dropdown);
        dropdowns.add(category_dropdown);
        Validators.validateDropdowns(dropdowns, validation_problems);


        // CHECK IF IMAGE ONE IS SET
        Validators.validateImageSelect(image_files_map, image_one_filechooser, validation_problems, "one");

        // CHECK IF IMAGE TWO IS SET
        Validators.validateImageSelect(image_files_map, image_two_filechooser, validation_problems, "two");
        
        // CHECK IF IMAGE THREE IS SET
        Validators.validateImageSelect(image_files_map, image_three_filechooser, validation_problems, "three");   


        return (validation_problems[0] <=0);

    }
}
