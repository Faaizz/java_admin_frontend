package com.faaizz.dev.online_platform.GUI.controller.settings;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.model.Settings;
import com.google.gson.Gson;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsController {

    @FXML
    private HBox loading_hbox;

    @FXML
    private TextField base_url_textfield;
    @FXML
    private TextField api_path_textfield;
    @FXML
    private TextField api_token_textfield;

    @FXML
    private VBox settings_vbox;
    @FXML
    private HBox settings_hbox;

    public void initialize() {

        // DISPLAY CURRENT SETTINGS
        base_url_textfield.setText(SettingsData.getSettings().getBase_url());
        api_path_textfield.setText(SettingsData.getSettings().getApi_path());
        api_token_textfield.setText(SettingsData.getSettings().getApi_token());

    }

    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleExit(){
        loading_hbox.getScene().getWindow().hide();
    }


     /*
     * =============================================================================
     * ===========
     */

    /**
     * ATTEMPTS TO SAVE NEW SETTINGS
     * 
     * @throws IOException
     */
    public void handleSaveSettings() throws IOException {

        // CREATE NEW Settings OBJECT WITH NEW INPUT TO TextFields
        Settings new_settings= new Settings(base_url_textfield.getText(), api_path_textfield.getText(), api_token_textfield.getText()); 

        // CONVERT NEW SETTING TO JSON STRING
        Gson gson= new Gson();

        String new_settings_json= gson.toJson(new_settings, Settings.class);

    

        // REMOVE TEXTFIELDS HBox
        settings_vbox.getChildren().remove(settings_hbox);

        // CREATE INNER VBOX TO DISPLAY RESULT OF SAVE ATTEMPT
        VBox temp_vbox= new VBox();
        temp_vbox.setMaxWidth(1000000);
        temp_vbox.setAlignment(Pos.CENTER);

        // SETUP LABEL
        Label temp_label= new Label();
        temp_label.getStyleClass().add("big-body-font");



        // WRITE NEW SETTINGS TO FILE
        try(BufferedWriter temp_file_writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream("settings/main.json")))){

            temp_file_writer.write(new_settings_json);

        }catch(Exception e){

            // DISPLAY ERROR
            temp_label.setText(e.getMessage());

            // ADD LABEL INNER VBOX
            temp_vbox.getChildren().add(temp_label);

            // ADD INNER VBOX TO EXISTING VBOX
            settings_vbox.getChildren().add(temp_vbox);

            // LOG ERROR TO CONSOLE
            e.printStackTrace();

        }



        // RELOAD SettingsData
        SettingsData.loadSettings();


        // SUCCESS
        temp_label.setText("New settings saved successfully.");

        // DISPLAY SUCCESS MESSAGE
        temp_vbox.getChildren().addAll(temp_label);
        
        // APPEND INNER VBOX TO EXISTING VBOX
        settings_vbox.getChildren().addAll(temp_vbox);

    }

}