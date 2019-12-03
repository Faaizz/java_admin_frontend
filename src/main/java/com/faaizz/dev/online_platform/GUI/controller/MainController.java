package com.faaizz.dev.online_platform.GUI.controller;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * All other controllers inherit this class. It defines functions that are general to all scenes
 */

public class MainController {

    protected final String CSS_RED_BORDERS= "red_borders";

    /*========================================================================================*/
    @FXML
    protected BorderPane root_border_pane;

    @FXML
    protected Label staff_name;

    /*========================================================================================*/
    @FXML
    public void handleExit(){
        root_border_pane.getScene().getWindow().hide();
    }

    @FXML
    public void handleLogout() throws IOException {

        // SHOW LOADING DIALOG
        MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

        try{

            // CLOSE LOADING DIALOG
            mini_dialog_controller.handleExit();

            // GET MAIN CLASS AND TRIGGER LOGOUT
            Main.getInstance().logout();

        }catch(Exception e){

            mini_dialog_controller.enableCloseButton();
            mini_dialog_controller.setDialog_text_label("An Unexpected Error occured.\nPlease restart the application");

            // LOG EXCEPTION TO CONSOLE
            e.printStackTrace();
        }

    }

    /*========================================================================================*/
    public MiniDialogController showLoadingMiniDialog() throws IOException {
        // DISPLAY LOADING DIALOG
        // Create dialog
        Dialog mini_dialog = new Dialog();
        mini_dialog.initOwner(root_border_pane.getScene().getWindow());
        // Create FXML Loader
        FXMLLoader mini_dialog_loader = new FXMLLoader();
        mini_dialog_loader.setLocation(getClass().getResource("../../view/dialogs/mini_dialog.fxml"));
        // Load FXML
        mini_dialog.getDialogPane().setContent(mini_dialog_loader.load());
        mini_dialog.initStyle(StageStyle.UNDECORATED);
        // Get Dialog Controller
        MiniDialogController mini_dialog_controller = mini_dialog_loader.getController();
        mini_dialog_controller.setDialog_text_label("Loading...");
        // Disable close button
        mini_dialog_controller.disableCloseButton();
        mini_dialog.show();

        return mini_dialog_controller;
    }

    /**
     * SET CURRENT STAFF NAME
     * @return First name of logged in staff
     */
    public void setCurrentStaffName(){
        staff_name.setText(InstanceData.getCurrentStaff().getFirst_name());
    }


    public void verifyAdminAuthorization() throws IOException {

        // SHOW LOADING DIALOG
        MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

        try{

            // CHECK IF AUTHENTICATED STAFF IS ADMIN
            if( !(InstanceData.getCurrentStaff().getPrivilege_level().equals("admin")) ){

                // IF NOT ADMIN, LOGOUT THE USER AND REDIRECT TO LOGIN PAGE
                Main.getInstance().logout();

                // THEN THROW AN EXCEPTION
                throw new AuthenticationException("Failed Authorization. \nPlease login as admin");
            }
            // OTHERWISE, CLOSE DIALOG AND DO NOTHING
            mini_dialog_controller.handleExit();

        }catch (Exception e){

            mini_dialog_controller.enableCloseButton();
            mini_dialog_controller.setDialog_text_label(e.getMessage());

            // LOG EXCEPTION TO CONSOLE
            e.printStackTrace();

        }



    }

}
