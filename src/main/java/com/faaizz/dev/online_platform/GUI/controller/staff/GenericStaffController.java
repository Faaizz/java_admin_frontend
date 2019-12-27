package com.faaizz.dev.online_platform.GUI.controller.staff;

import com.faaizz.dev.online_platform.GUI.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class GenericStaffController extends MainController {

    @FXML
    protected ScrollPane content_scrollpane;

    private final String EDIT= "view/staff/edit.fxml";
    private final String ADD= "view/staff/add.fxml";
    private final String MANAGE= "view/staff/manage.fxml";


    public void initialize() throws Exception {

        // INITIALIZE MENU
        super.setupMenu();

        // SET "active-section-button" ON PRODUCTS
        staff_button.getStyleClass().add("active-section-button");

        // SHOW MENU
        super.renderMenu();

        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // SET CURRENT STAFF NAME
        super.setCurrentStaffName();

    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S */

    @FXML
    public void handleRedirectToEditDetails(){

    }

    @FXML
    public void handleRedirectToAddAccounts(){

    }

    @FXML
    public void handleRedirectToManageAccounts(){

    }

}
