package com.faaizz.dev.online_platform.GUI.controller.products.trends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.GUI.controller.validators.Validators;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableTrend;
import com.faaizz.dev.online_platform.api_outbound.platform.TrendResource;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AddTrendController extends GenericTrendController {



    /*
     * =============================================================================
     * ===========
     */
    /* I N I T I A L I Z E */

    public void initialize() throws Exception {

        super.initialize();

        // Select the first item by default
        gender_dropdown.getSelectionModel().selectFirst();

        // Reset image_files_list
        image_files_map= new HashMap<>();

    }

    /*
     * =============================================================================
     * ===========
     */
    /* A D D  T R E N D */
    @FXML
    public void handleAddTrend() throws IOException {

        // VALIDATE INPUT

        if(this.validateEntries()) {

            /*  S   U   C   C   E   S   S       V   A   L   I   D   A   T   I   O   N */

            // SETUP NEW TREND RESOURCE
            TrendResource trendResource= new TrendResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            
            // CREATE UploadableTrend INSTANCE
            UploadableTrend to_upload= new UploadableTrend(
                    name_textfield.getText(),
                    description_text_area.getText(), 
                    (String) gender_dropdown.getSelectionModel().getSelectedItem(), 
                    (image_files_map.containsKey("one") ? image_files_map.get("one"): null)
            );

            // Call inherited method from MainController class to display loading dialog
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            // ADD TREND TO DATABASE IN SEPARATE THREAD
            Runnable add_trend_runnable = new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        try {
                            trendResource.add(to_upload);
                            // If successful, remove loading dialog
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("Trend Added Successfully");

                            // Set all TextFields and Text Areas to blank
                            List<TextField> textfields= new ArrayList<>();

                            textfields.add(name_textfield);
                            //TextArea
                            description_text_area.setText("");

                            //Upload Button
                            image_one_filechooser.setText("Image");

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
            add_trend_runnable.run();
        }
    }

    public boolean validateEntries(){

        final int[] validation_problems = {0}; // Integer to keep count of invalid entries. Array is used because of lambda compatibility

        /*========================================================================================*/
        // VALIDATE TEXTFEILDS
        List<TextField> textfields= new ArrayList<>();

        textfields.add(name_textfield);
        // Perform Validation
        Validators.validateTextFields(textfields, validation_problems);

        // VALIDATE DESCRIPTION TEXTAREA
        List<TextArea> textareas= new ArrayList<>();
        textareas.add(description_text_area);
        Validators.validateTextAreas(textareas, validation_problems);

        // VALIDATE DROPDOWNS
        List<ComboBox> dropdowns= new ArrayList<>();
        dropdowns.add(gender_dropdown);
        Validators.validateDropdowns(dropdowns, validation_problems);


        // CHECK IF IMAGE ONE IS SET
        Validators.validateImageSelect(image_files_map, image_one_filechooser, validation_problems, "one");

        // CHECK IF IMAGE TWO IS SET
        // Validators.validateImageSelect(image_files_map, image_two_filechooser, validation_problems);
        
        // CHECK IF IMAGE THREE IS SET
        // Validators.validateImageSelect(image_files_map, image_three_filechooser, validation_problems);   

        return (validation_problems[0] <=0);

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
    

}
