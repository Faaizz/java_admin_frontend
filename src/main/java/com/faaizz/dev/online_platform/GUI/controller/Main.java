package com.faaizz.dev.online_platform.GUI.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * All other controllers inherit this class. It defines functions that are general to all scenes
 */

public class Main {

    /*========================================================================================*/
    @FXML
    protected BorderPane root_border_pane;

    /*========================================================================================*/
    @FXML
    public void handleExit(){
        root_border_pane.getScene().getWindow().hide();
    }

}
