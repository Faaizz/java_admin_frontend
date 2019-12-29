package com.faaizz.dev.online_platform.GUI.controller.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadablePerson;
import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditDetailsController extends GenericStaffController {

    @FXML
    protected TextField phone_number_textfield;
    @FXML
    protected TextField alt_phone_number_textfield;    
    @FXML
    protected PasswordField password_textfield;    
    @FXML
    protected PasswordField new_password_textfield;
    @FXML
    protected PasswordField con_new_password_textfield;

    protected String email;

    public void initialize() throws Exception {

        // INITIALIZE
        super.initialize();

        // Set "ENTER" key event on con_new_password_textfield to trigger login attempt
        con_new_password_textfield.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    handleSave();;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );

        // LOAD INFO OF CURRENT STAFF

        // Load in new thread
        Runnable my_account_runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    // Show loading dialog
                    MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                    // Initlize StaffResource
                    StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
                    Platform.runLater(() -> {
                        try {

                            // ATTEMPT TO LOAD STAFF DETAILS
                            String my_account_string= staff_resource.myAccount();

                            Staff my_account= APIParser.getInstance().parseSingleStaffResponse(my_account_string);

                            // Set details
                            email= my_account.getEmail();
                            phone_number_textfield.setText( ( (my_account.getPhone_numbers().size() >= 1) ? my_account.getPhone_numbers().get(0) : "" ) );
                            alt_phone_number_textfield.setText( ( (my_account.getPhone_numbers().size() >= 2) ? my_account.getPhone_numbers().get(1) : "" ) );

                            // If successful, remove loading dialog
                            mini_dialog_controller.handleExit();


                        } catch (Exception e) {
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                        }
                    });
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        // Run
        Platform.runLater(my_account_runnable);


    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S */

    @FXML
    public void handleSave() throws IOException {

        // Validation error counter
        int val_errors= 0;

        // CHECK IF PHONE NUMBER TEXTFIELDS ARE EMPTY
        List<TextField> textfields= new ArrayList<>();
        textfields.add(phone_number_textfield);
        textfields.add(alt_phone_number_textfield);
        textfields.add(password_textfield);

        for(TextField textfield : textfields){

            if(textfield.getText().isEmpty()){
                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                
                val_errors++;

            }else{
                textfield.getStyleClass().remove(CSS_RED_BORDERS);
            }

        }


        // IF NEW PASSWORD TEXTFIELD IS FILLED, ENSURE THAT CONFIRM NEW PASSWORD TEXTFIELD IS FILLED AND THEY CONTAIN THE SAME TEXT
        if(!new_password_textfield.getText().isEmpty()){

            if(con_new_password_textfield.getText().isEmpty() || !(new_password_textfield.getText().equals(con_new_password_textfield.getText()))){
                if(!con_new_password_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    con_new_password_textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                
                val_errors++;

            }else{
                con_new_password_textfield.getStyleClass().remove(CSS_RED_BORDERS);
            }

        }


        if(val_errors <= 0){

            // SUCCESS VALIDATION
            // Display loading dialog

            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            // Initialize StaffResource
            StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // Setup Runnable
            Runnable update_runnable= new Runnable(){
            
                @Override
                public void run() {
                    
                    Platform.runLater( ()->{

                        try{
                            // Setup UploadablePerson
                            List<String> phone_numbers= new ArrayList<>();
                            phone_numbers.add(phone_number_textfield.getText());
                            phone_numbers.add(alt_phone_number_textfield.getText());

                            UploadablePerson staff_update= new UploadablePerson("", "", email, "", "", "", phone_numbers);
                            staff_update.setPassword(password_textfield.getText());
                            staff_update.setNew_password(new_password_textfield.getText());

                            // Attempt Staff Update
                            staff_resource.update(email, staff_update);

                            //Success
                            // Enable close on dialog
                            mini_dialog_controller.enableCloseButton();

                            // Display success message
                            mini_dialog_controller.setDialog_text_label("Details Updated Successfully");

                            // Clear password fields
                            password_textfield.setText("");
                            new_password_textfield.setText("");
                            con_new_password_textfield.setText("");

                        }catch(Exception e){

                            mini_dialog_controller.setDialog_text_label(e.getMessage());
                            mini_dialog_controller.enableCloseButton();
                            e.printStackTrace();

                        }

                    } );

                }
            };

            // Run
            update_runnable.run();


        }

    }

}
