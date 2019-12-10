package com.faaizz.dev.online_platform.GUI.controller.dialogs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class MiniDialogController {

    @FXML
    private HBox loading_hbox;

    @FXML
    private Label dialog_text_label;

    @FXML
    private VBox dialog_vbox;

    @FXML
    private Button close_button;

    @FXML
    public void handleExit(){
        loading_hbox.getScene().getWindow().hide();
    }

    public void setDialog_text_label(String to_set){
        dialog_text_label.setText(to_set);
    }

    public void setDialog_vbox_content(Node node){

        dialog_vbox.getChildren().setAll(node);
    }

    public void disableCloseButton(){
        close_button.setDisable(true);
    }

    public void enableCloseButton(){
        close_button.setDisable(false);
    }

}
