package com.faaizz.dev.online_platform.GUI.controller.dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MiniDialogController {

    @FXML
    private HBox loading_hbox;

    @FXML
    private Label dialog_text_label;

    @FXML
    public void handleExit(){
        loading_hbox.getScene().getWindow().hide();
    }

    public void setDialog_text_label(String to_set){
        dialog_text_label.setText(to_set);
    }

}
