package com.faaizz.dev.online_platform.GUI.controller;

import com.faaizz.dev.online_platform.GUI.InstanceData;
import com.faaizz.dev.online_platform.GUI.Main;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.model.supplement.StaffPrivilegeLevel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * All other controllers inherit this class. It defines functions that are general to all scenes
 */

public class MainController {

    protected final String CSS_RED_BORDERS= "red_borders";

    private final String PRODUCTS= "view/products/manage.fxml";
    private final String ORDERS= "view/orders/workload.fxml";
    private final String STAFF= "";
    private final String CUSTOMERS= "view/customers/view.fxml";

    /*========================================================================================*/
    @FXML
    protected BorderPane root_border_pane;

    @FXML
    protected Label staff_name;

    @FXML
    protected Button settings_button;

    @FXML
    protected VBox center_vbox;

    @FXML
    protected VBox pagination_vbox;

    /**
     * This method renders the application's footer
     */
    private void renderFooter(){

        Label footer_text= new Label("COPYRIGHT © 2019");
        footer_text.getStyleClass().add("dark-color-style");
        footer_text.setAlignment(Pos.CENTER);
        footer_text.setPrefWidth(1000000000);
        footer_text.setPadding(new Insets(5, 5, 5, 5));

        // SET root_border_pane <bottom> TO FOOTER
        root_border_pane.setBottom(footer_text);

    }

    // INITIALIZE BLOCK
    public void initialize() throws Exception {

        // DISPLAY FOOTER
        this.renderFooter();

    }

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

    @FXML
    public void handleSettings() throws IOException {

        // DISPLAY LOADING DIALOG
        // Create dialog
        Dialog settings_dialog = new Dialog();
        settings_dialog.initOwner(root_border_pane.getScene().getWindow());
        // Create FXML Loader
        FXMLLoader settings_dialog_loader = new FXMLLoader();
        settings_dialog_loader.setLocation(getClass().getResource("../../view/settings/settings.fxml"));
        // Load FXML
        settings_dialog.getDialogPane().setContent(settings_dialog_loader.load());
        settings_dialog.initStyle(StageStyle.UNDECORATED);
        settings_dialog.show();

    }


    @FXML
    public void  handleSectionChangeToProducts() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(PRODUCTS);
    }


    @FXML
    public void  handleSectionChangeToOrders() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(ORDERS);
    }


    @FXML
    public void  handleSectionChangeToCustomers() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(CUSTOMERS);
    }


    @FXML
    public void  handleSectionChangeToManageStaff() throws IOException, AuthenticationException {
        // GET INSTANCE OF Main AND PERFORM REDIRECTION
        Main.getInstance().redirectToPage(STAFF);
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


    public void verifyAdminAuthorization() throws Exception {

        // CHECK IF AUTHENTICATED STAFF IS ADMIN
        if( !(InstanceData.getCurrentStaff().getPrivilege_level().equals(StaffPrivilegeLevel.ADMIN)) ){

            // IF NOT ADMIN, LOGOUT THE USER AND REDIRECT TO LOGIN PAGE
            Main.getInstance().logout();

            // THEN THROW AN EXCEPTION
            // throw new AuthenticationException("Failed Authorization. \nPlease login as admin");
        }


    }


    /**
     * DISPLAYS PAGINATION BUTTONS AND ASSIGN THE CORRESPONDING CONSUMER HANDLER FOR EACH
     * @param page_meta
     * @param post_data
     * @param fetchFunction
     */
    protected HBox h_box_pagination;
    protected HBox h_box_pagination_direct;
    protected void setupPagination(Meta page_meta, Map<String, String> post_data, BiConsumer<Map<String, String>, Integer> fetchFunction){

        // OUTER HBox
        h_box_pagination= new HBox();
        h_box_pagination.setAlignment(Pos.CENTER);
        h_box_pagination.setSpacing(5);
        h_box_pagination.setPadding(new Insets(15, 15, 0, 15));

        Button first= new Button("first");
        first.getStyleClass().add("pagination-button");
        first.getStyleClass().add("mid-body-font");
        first.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                fetchFunction.accept(post_data, 1);
            }
        } );


        Button prev= new Button("<");
        prev.getStyleClass().add("pagination-button");
        prev.getStyleClass().add("mid-body-font");
        prev.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                fetchFunction.accept(post_data, (page_meta.getCurrent_page() - 1));
            }

        } );

        // IF SEARCH IS AT FIRST PAGE, DISABLE first AND prev
        if(page_meta.getCurrent_page() == 1){
            first.setDisable(true);
            prev.setDisable(true);
        }

        StringBuilder tempSB= new StringBuilder().append(page_meta.getCurrent_page()).append(" of ").append(page_meta.getLast_page());
        Label page_number= new Label(tempSB.toString());
        page_number.getStyleClass().add("mid-body-font");

        Button next= new Button(">");
        next.getStyleClass().add("pagination-button");
        next.getStyleClass().add("mid-body-font");
        next.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                fetchFunction.accept(post_data, (page_meta.getCurrent_page() + 1));
            }

        } );

        Button last= new Button("last");
        last.getStyleClass().add("pagination-button");
        last.getStyleClass().add("mid-body-font");
        last.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                fetchFunction.accept(post_data, (page_meta.getLast_page()));
            }

        } );

        // IF SEARCH IS AT LAST PAGE, DISABLE last AND next
        if(page_meta.getCurrent_page() == page_meta.getLast_page()){
            last.setDisable(true);
            next.setDisable(true);
        }


        // APPEND PAGINATION BUTTONS TO h_box
        h_box_pagination.getChildren().add(first);
        h_box_pagination.getChildren().add(prev);
        h_box_pagination.getChildren().add(page_number);
        h_box_pagination.getChildren().add(next);
        h_box_pagination.getChildren().add(last);


        // EMPTY pagination_vbox
        pagination_vbox.getChildren().remove(0, pagination_vbox.getChildren().size());

        // ADD h_box TO pagination_vbox
        pagination_vbox.getChildren().add(h_box_pagination);



        // DIRECT PAGE NAVIGATION
        h_box_pagination_direct= new HBox();
        h_box_pagination_direct.setAlignment(Pos.CENTER);
        h_box_pagination_direct.setSpacing(5);
        h_box_pagination_direct.setPadding(new Insets(15, 15, 0, 15));

        HBox h_box_inner= new HBox();
        h_box_inner.setAlignment(Pos.CENTER);
        h_box_inner.setSpacing(5);

        TextField search_tf= new TextField();
        search_tf.getStyleClass().add("pagination-textfield");
        search_tf.setPrefWidth(50);

        Button go_button= new Button("GO");
        go_button.getStyleClass().add("pagination-button");
        go_button.getStyleClass().add("mid-body-font");
        go_button.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                try{

                    // VALIDATE PAGE VALUE
                    int page_number= Integer.valueOf(search_tf.getText());

                    // IF SPECIFIED PAGE VALUE EXCEEDS THE LAST PAGE, SET RED BORDERS
                    if( page_number > page_meta.getLast_page() ){
                        if(!search_tf.getStyleClass().contains(CSS_RED_BORDERS)){
                            search_tf.getStyleClass().add(CSS_RED_BORDERS);
                        }
                    }
                    // OTHERWISE
                    else{

                        if(search_tf.getStyleClass().contains(CSS_RED_BORDERS)){
                            search_tf.getStyleClass().remove(CSS_RED_BORDERS);
                        }

                        fetchFunction.accept(post_data, page_number);

                    }

                }
                catch(NumberFormatException e){
                    // INVALID NUMBER, SET RED BORDERS
                    if(!search_tf.getStyleClass().contains(CSS_RED_BORDERS)){
                        search_tf.getStyleClass().add(CSS_RED_BORDERS);
                        e.printStackTrace();
                    }

                }

            }

        } );


        // ADD TO hbox_inner
        h_box_inner.getChildren().add(search_tf);
        h_box_inner.getChildren().add(go_button);

        // ADD TO h_box_direct
        h_box_pagination_direct.getChildren().add(h_box_inner);

        // ADD h_box TO pagination_vbox
        pagination_vbox.getChildren().add(h_box_pagination_direct);


    }


}
