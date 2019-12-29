package com.faaizz.dev.online_platform.GUI.controller.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class AddStaffController extends GenericStaffController{

    @FXML
    protected TextField first_name_textfield;
    @FXML
    protected TextField last_name_textfield;
    @FXML
    protected ComboBox<String> gender_dropdown;
    @FXML
    protected ComboBox<String> admin_dropdown;
    @FXML
    protected TextField email_textfield;
    @FXML
    protected TextArea address_text_area;
    @FXML
    protected TextField phone_number_textfield;
    @FXML
    protected TextField alt_phone_number_textfield;
    @FXML
    protected PasswordField password_textfield;
    @FXML
    protected PasswordField confirm_password_textfield;
    @FXML
    protected Button add_staff_button;
    


    public void initialize() throws Exception {

        super.initialize();

        // Select "male" gender by default
        gender_dropdown.getSelectionModel().select("male");

        // Select "false" admin status by default
        admin_dropdown.getSelectionModel().select("false");

    
        // Set "ENTER" key event on confirm_password_textfield to trigger login attempt
        confirm_password_textfield.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    handleConfirm();;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        );

    }


    @FXML
    public void handleConfirm(){

        // VALIDATION

        // Initialize validation errors counter
        int val_errors= 0;

        // TextFields and PasswordFields
        List<TextField> texts= new ArrayList<>();
        texts.add(first_name_textfield);
        texts.add(last_name_textfield);
        texts.add(email_textfield);
        texts.add(phone_number_textfield);
        texts.add(alt_phone_number_textfield);
        texts.add(password_textfield);
        texts.add(confirm_password_textfield);

        // Loop through and ensure each field has content
        for(TextField field: texts){

            if(field.getText().isEmpty()){
                
                // IF THE FIELD IS EMPTY, DISPLAY RED BORDERS
                if(!field.getStyleClass().contains(CSS_RED_BORDERS)){
                    field.getStyleClass().add(CSS_RED_BORDERS);
                }

                // INCREMENT ERRORS COUNT
                val_errors++;

            }else{

                // IF IT HAS TEXT, REMOVE RED BORDERS
                field.getStyleClass().remove(CSS_RED_BORDERS);

            }

        }


        // Check that "password_textfield" and "confirm_password_textfield" have the same contents
        if( !password_textfield.getText().equals(confirm_password_textfield.getText()) ){

            // If they contain different text
            if(!confirm_password_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                confirm_password_textfield.getStyleClass().add(CSS_RED_BORDERS);
            }

            // Increment errors count
            val_errors++;

        }else{

            // If the text match, remove red borders
            confirm_password_textfield.getStyleClass().remove(CSS_RED_BORDERS);
        }


        // Check that address_text_area is not empty
        if( address_text_area.getText().isEmpty() ){

            if(!address_text_area.getStyleClass().contains(CSS_RED_BORDERS)){

                address_text_area.getStyleClass().add(CSS_RED_BORDERS);

            }

        }else{

            // If address is filled, remove red borders
            address_text_area.getStyleClass().remove(CSS_RED_BORDERS);

        }


        // Check if "gender_dropdown" has an item selected
        if( gender_dropdown.getSelectionModel().getSelectedItem() == null ){

            // If no item is selected, show red borders
            if( !gender_dropdown.getStyleClass().contains(CSS_RED_BORDERS) ){
                gender_dropdown.getStyleClass().add(CSS_RED_BORDERS);
            }

            // Increment errors count
            val_errors++;

        }else{

            // If an item is selected, remove red borders
            gender_dropdown.getStyleClass().remove(CSS_RED_BORDERS);

        }




        // SUCCESS VALIDATION
        if(val_errors <= 0){

            Runnable add_staff_runnable= new Runnable(){
            
                @Override
                public void run() {
                    
                    try{

                        // Show loading dialog
                        MiniDialogController mini_dialog_controller= showLoadingMiniDialog();
        
                        // Create StaffResource
                        StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
        
        
                        // Attempt Add Staff in new Thread
                        Platform.runLater( ()->{

                            try{

                                // Prepare new staff

                                // Setup list of phone numbers
                                List<String> phone_numbers= new ArrayList<>();
                                phone_numbers.add(phone_number_textfield.getText());
                                phone_numbers.add(alt_phone_number_textfield.getText());

                                UploadablePerson new_staff_person= new UploadablePerson(
                                    first_name_textfield.getText(), last_name_textfield.getText(), email_textfield.getText(), password_textfield.getText(), 
                                    address_text_area.getText(), gender_dropdown.getSelectionModel().getSelectedItem(), phone_numbers
                                );

                                // Is new staff admin?
                                String isAdmin= admin_dropdown.getSelectionModel().getSelectedItem();
                                if(isAdmin.equals("true")){
                                    // Set admin privilege_level in new_staff_person object
                                    new_staff_person.setAdmin();
                                }

                                String new_staff_string= staff_resource.add(new_staff_person);

                                Staff new_staff= APIParser.getInstance().parseSingleStaffResponse(new_staff_string);
                                
                                // SUCCESS
                                mini_dialog_controller.setDialog_text_label("New Staff account created for " + new_staff.getEmail());

                                // Enable cllose button
                                mini_dialog_controller.enableCloseButton();



                            }catch(Exception e){

                                mini_dialog_controller.setDialog_text_label(e.getMessage());
                                mini_dialog_controller.enableCloseButton();

                            }

                        

                        } );
        
        
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }
            };

            // Run
            add_staff_runnable.run();

            

        }


    }


}