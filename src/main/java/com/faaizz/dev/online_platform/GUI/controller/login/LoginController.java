package com.faaizz.dev.online_platform.GUI.controller.login;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.exceptions.ResponseException;
import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.client.ClientProtocolException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ConnectException;

public class LoginController extends MainController {

    @FXML
    private TextField id_textfield;
    @FXML
    private PasswordField passwordfield;

    @FXML
    private Button login_button;

    private boolean validateEntries(){

        boolean id_is_empty= id_textfield.getText().isEmpty();
        boolean password_is_empty= passwordfield.getText().isEmpty();

        ObservableList id_style_list= id_textfield.getStyleClass();
        ObservableList password_style_list= passwordfield.getStyleClass();

        // IF LOGIN FIELD IS EMPTY, SET CSS red_borders
        if( id_is_empty ){
            // IF CSS red_borders isn't added to list of classes already
            if( !id_style_list.contains(CSS_RED_BORDERS) ){
                // ADD IT
                id_style_list.add(CSS_RED_BORDERS);
            }
        }
        else{

            // REMOVE red_borders IF IT HAS
            if( id_style_list.contains(CSS_RED_BORDERS) ){
                id_style_list.remove(CSS_RED_BORDERS);
            }
        }

        // IF PASSWORD FIELD IS EMPTY, SET CSS red_borders
        if( password_is_empty ){
            // IF CSS red_borders isn't added to list of classes already
            if( !password_style_list.contains(CSS_RED_BORDERS) ){
                // ADD IT
                password_style_list.add(CSS_RED_BORDERS);
            }
        }
        else{

            // REMOVE red_borders IF IT HAS
            if( password_style_list.contains(CSS_RED_BORDERS) ){
                password_style_list.remove(CSS_RED_BORDERS);
            }
        }

        // RETURN TRUE IF NEITHER LOGIN ID OR PASSWORD IS EMPTY
        return( !(id_is_empty || password_is_empty) );

    }

    @FXML
    public void handleLogin() throws IOException {

        // VALIDATION
        if(validateEntries()){

            // CALL INHERITED METHOD FROM MainController CLASS TO DISPLAY LOAING DIALOG
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            // PERFORM API ACCESS IN NEW THREAD
            Runnable staff_login= new Runnable() {
                @Override
                public void run() {
                    Platform.runLater( ()->{

                        try{

                            // CREATE StaffResource
                            StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

                            // LOGIN STAFF
                            staff_resource.login(id_textfield.getText(), passwordfield.getText(), "yes");

                            //GRAB DETAILS OF AUTHENTICATED STAFF
                            String login_staff_string= staff_resource.myAccount();

                            //PARSE STAFF
                            Staff login_staff= APIParser.getInstance().parseSingleStaffResponse(login_staff_string);

                            // SET INSTANCE DATA TO REGISTER LOGIN
                            InstanceData.setAuthenticated(true);

                            // SET CURRENT STAFF
                            InstanceData.setCurrentStaff(login_staff);

                            // CLOSE LOADING DIALOG
                            mini_dialog_controller.handleExit();

                            // REDIRECT TO MANAGE PRODUCTS (SEARCH PRODUCTS)
                            Main.getInstance().redirectToManageProducts();

                        }catch (ResponseException e){

                            // ENABLE CLOSE BUTTON
                            mini_dialog_controller.enableCloseButton();

                            // PARSE JSON RESPONSE

                            // SET message RECIEVED FROM API
                            mini_dialog_controller.setDialog_text_label(e.getJsonResponse());

                            // LOG ERROR TO CONSOLE
                            e.printStackTrace();
                        }catch(Exception e){
                            // ENABLE CLOSE BUTTON
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An Unxpected Error Ocurred");

                            // LOG ERROR TO CONSOLE
                            e.printStackTrace();
                        }

                    } );
                }
            };


            // RUN THE NEW THREAD
            staff_login.run();


        }


    }

}
