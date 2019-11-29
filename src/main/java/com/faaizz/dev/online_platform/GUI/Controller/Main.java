package com.faaizz.dev.online_platform.GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * All other controllers inherit this class. It defines functions that are general to all scenes
 */

public class Main {

    /*========================================================================================*/
    @FXML
    private BorderPane root_border_pane;

    /*========================================================================================*/
    @FXML
    public void handleExit(){
        root_border_pane.getScene().getWindow().hide();
    }

}
