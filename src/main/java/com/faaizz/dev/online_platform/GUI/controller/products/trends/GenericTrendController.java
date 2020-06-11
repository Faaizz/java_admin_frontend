package com.faaizz.dev.online_platform.GUI.controller.products.trends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.products.GenericProductController;
import com.faaizz.dev.online_platform.GUI.controller.validators.Validators;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class GenericTrendController extends MainController{

    // PAGE PATHS
    private final String MANAGE_PRODUCTS = "view/products/manage.fxml";
    private final String ADD_PRODUCTS = "view/products/add.fxml";
    private final String REMOVE_PRODUCTS = "view/products/remove.fxml";
    private final String MANAGE_TRENDS = "view/products/trends/manage.fxml";
    private final String ADD_TRENDS = "view/products/trends/add.fxml";
    private final String REMOVE_TRENDS = "view/products/trends/remove.fxml";
    
    protected Map<String, File> image_files_map;

    @FXML
    protected Button image_one_filechooser;
    @FXML
    protected Button image_two_filechooser;
    @FXML
    protected Button image_three_filechooser;

    @FXML
    protected TextField name_textfield;

    @FXML
    protected TextArea description_text_area;

    @FXML
    protected ComboBox<String> gender_dropdown;

    public void initialize() throws Exception{

        // INITIALIZE MENU
        super.setupMenu();

        // SET CURRENT STAFF NAME
        super.setCurrentStaffName();

        // SHOW MENU
        super.renderMenu();

        // Initialize gender dropdown
        initializeGender();

    }

    protected void initializeGender(){

        List<String>gender_list= FXCollections.observableArrayList("male", "female", "unisex");

        gender_dropdown.getItems().clear();
        gender_dropdown.getItems().addAll(gender_list);

    }

    @FXML
    public void handleFileChooser(Event event) {

        // Get button that triggered the event
        Button button = (Button) event.getSource();

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
    
}