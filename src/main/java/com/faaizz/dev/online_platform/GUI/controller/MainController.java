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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private final String STAFF= "view/staff/edit.fxml";
    private final String CUSTOMERS= "view/customers/view.fxml";

    /*========================================================================================*/
    @FXML
    protected BorderPane root_border_pane;

    @FXML
    protected Label staff_name;

    @FXML
    protected VBox center_vbox;

    @FXML
    protected VBox pagination_vbox;


    /** MENU CONTROLS */
    protected VBox top_menu_vbox;
    protected GridPane header_and_buttons_gpane;
    protected Label title_label;
    protected Button settings_button;
    protected Button logout_button;
    protected Button close_button;
    protected Button products_button;
    protected Button orders_button;
    protected Button customers_button;
    protected Button staff_button;
    /**
     * This method sets up the top menu
     */
    protected void setupMenu(){

        top_menu_vbox= new VBox();
        GridPane header_and_buttons_gpane= new GridPane();

        // Title Label
        title_label= new Label("STAFF PORTAL");
        title_label.setPadding(new Insets(5));
        title_label.getStyleClass().addAll("dark-color-style", "big-header-font");
        // Add to gridpane
        header_and_buttons_gpane.add(title_label, 0, 0);
        GridPane.setHalignment(title_label, HPos.CENTER);
        GridPane.setHgrow(title_label, Priority.ALWAYS);


        // Settings Button
        settings_button= new Button();
        settings_button.getStyleClass().add("header-buttons");
        settings_button.setMaxWidth(10);
        settings_button.setOnAction(event -> {
            try {
                handleSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Get button image
        //Image settings_image= new Image(getClass().getResourceAsStream("img/settings.png"));
        Image settings_image= new Image(MainController.class.getResourceAsStream("img/settings.png"));
        ImageView settings_iview= new ImageView(settings_image);
        settings_button.setGraphic(settings_iview);
        settings_button.setTooltip(new Tooltip("settings"));
        settings_button.setAlignment(Pos.CENTER_RIGHT);
        // Add to gridpane
        header_and_buttons_gpane.add(settings_button, 1, 0);


        // Login Button
        logout_button= new Button();
        logout_button.getStyleClass().add("header-buttons");
        logout_button.setMaxWidth(10);
        logout_button.setOnAction(event -> {
            try {
                handleLogout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Get button image
        //Image logout_image= new Image(getClass().getResourceAsStream("../../view/img/logout.png"));
        Image logout_image= new Image(MainController.class.getResourceAsStream("img/logout.png"));
        ImageView logout_iview= new ImageView(logout_image);
        logout_button.setGraphic(logout_iview);
        logout_button.setTooltip(new Tooltip("logout"));
        logout_button.setAlignment(Pos.CENTER_RIGHT);
        // Add to gridpane
        header_and_buttons_gpane.add(logout_button, 2, 0);


        // Close Button
        close_button= new Button();
        close_button.getStyleClass().add("header-buttons");
        close_button.setMaxWidth(10);
        close_button.setOnAction(event -> {
            handleExit();
        });
        // Get button image
        //Image close_image= new Image(getClass().getResourceAsStream("../../view/img/close.png"));
        Image close_image= new Image(MainController.class.getResourceAsStream("img/close.png"));
        ImageView close_iview= new ImageView(close_image);
        close_button.setGraphic(close_iview);
        close_button.setTooltip(new Tooltip("exit"));
        close_button.setAlignment(Pos.CENTER_RIGHT);
        // Add to gridpane
        header_and_buttons_gpane.add(close_button, 3, 0);

        header_and_buttons_gpane.getStyleClass().add("dark-color-style");

        // ADD GRIDPANE TO VBOX
        top_menu_vbox.getChildren().add(header_and_buttons_gpane);


        // Sections Gridpane
        GridPane sections_gridpane= new GridPane();
        

        // Products Button
        products_button= new Button("PRODUCTS");
        products_button.getStyleClass().addAll("section-buttons", "big-body-font");
        products_button.setPrefWidth(200);
        products_button.setMaxWidth(200);
        GridPane.setHalignment(products_button, HPos.LEFT);
        GridPane.setHgrow(products_button, Priority.ALWAYS);
        products_button.setOnAction(event -> {
            try{
                handleSectionChangeToProducts();
            }catch(Exception e){
                e.printStackTrace();
            }            
        });
        // Add to gridpane
        sections_gridpane.add(products_button, 0, 0);
        

        // Orders Button
        orders_button= new Button("ORDERS");
        orders_button.getStyleClass().addAll("section-buttons", "big-body-font");
        orders_button.setPrefWidth(200);
        orders_button.setMaxWidth(200);
        GridPane.setHalignment(orders_button, HPos.LEFT);
        GridPane.setHgrow(orders_button, Priority.ALWAYS);
        orders_button.setOnAction(event -> {
            try{
                handleSectionChangeToOrders();
            }catch(Exception e){
                e.printStackTrace();
            }            
        });
        // Add to gridpane
        sections_gridpane.add(orders_button, 1, 0);
        

        // Customers Button
        customers_button= new Button("CUSTOMERS");
        customers_button.getStyleClass().addAll("section-buttons", "big-body-font");
        customers_button.setPrefWidth(200);
        customers_button.setMaxWidth(200);
        GridPane.setHalignment(customers_button, HPos.LEFT);
        GridPane.setHgrow(customers_button, Priority.ALWAYS);
        customers_button.setOnAction(event -> {
            try{
                handleSectionChangeToCustomers();
            }catch(Exception e){
                e.printStackTrace();
            }            
        });
        // Add to gridpane
        sections_gridpane.add(customers_button, 2, 0);
        

        // Staff Button
        staff_button= new Button("STAFF");
        staff_button.getStyleClass().addAll("section-buttons", "big-body-font");
        staff_button.setPrefWidth(200);
        staff_button.setMaxWidth(200);
        GridPane.setHalignment(staff_button, HPos.LEFT);
        GridPane.setHgrow(staff_button, Priority.ALWAYS);
        staff_button.setOnAction(event -> {
            try{
                handleSectionChangeToManageStaff();
            }catch(Exception e){
                e.printStackTrace();
            }            
        });
        // Add to gridpane
        sections_gridpane.add(staff_button, 3, 0);

        // Gridpane padding
        sections_gridpane.setPadding(new Insets(15));

        // ADD GRIDPANE TO VBOX
        top_menu_vbox.getChildren().add(sections_gridpane);


    }


    /**
     * This method renders the menu
     */
    protected void renderMenu(){

        // SHOW MENU
        root_border_pane.setTop(top_menu_vbox);

    }

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
    public void handleSettings()throws IOException {

        // DISPLAY LOADING DIALOG
        // Create dialog
        Dialog settings_dialog = new Dialog();
        settings_dialog.initOwner(root_border_pane.getScene().getWindow());
        // Create FXML Loader
        FXMLLoader settings_dialog_loader = new FXMLLoader();
        //settings_dialog_loader.setLocation(getClass().getResource("../../view/settings/settings.fxml"));
        settings_dialog_loader.setLocation(MainController.class.getResource("settings/settings.fxml"));
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
        //mini_dialog_loader.setLocation(getClass().getResource("../../view/dialogs/mini_dialog.fxml"));
        mini_dialog_loader.setLocation(MainController.class.getResource("dialogs/mini_dialog.fxml"));
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
