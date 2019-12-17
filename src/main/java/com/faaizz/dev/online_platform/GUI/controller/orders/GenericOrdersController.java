package com.faaizz.dev.online_platform.GUI.controller.orders;

import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

import java.io.IOException;

public class GenericOrdersController extends MainController {

    @FXML
    protected ScrollPane content_scrollpane;

    private final String WORKLOAD= "view/orders/workload.fxml";
    private final String PENDING= "view/orders/pending.fxml";
    private final String UNASSIGNED= "view/orders/unassigned.fxml";
    private final String FAILED= "view/orders/failed.fxml";


    public void initialize() throws Exception {

        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // SET CURRENT STAFF NAME
        super.setCurrentStaffName();

    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S */
    
    @FXML
    protected void handleRedirectToMyWorkload() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(WORKLOAD);
    }

    @FXML
    protected void handleRedirectToPendingOrders() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(PENDING);
    }

    @FXML
    protected void handleRedirectToUnassignedOrders() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(UNASSIGNED);
    }

    @FXML
    protected void handleRedirectToFailedOrders() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(FAILED);
    }

}
