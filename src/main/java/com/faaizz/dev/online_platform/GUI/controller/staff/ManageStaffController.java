package com.faaizz.dev.online_platform.GUI.controller.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.collection.StaffCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ManageStaffController extends GenericStaffController{

    @FXML
    private VBox anchor_vbox;

    @FXML
    protected ScrollPane content_scrollpane;

    public void initialize() throws Exception {

        super.initialize();


        // LOAD STAFF
        Platform.runLater( ()->{

            loadStaff(new HashMap<String, String>(), 1);

        } );



    }


    /*========================================================================================*/
    /*  H   A   N   D   L   E       S   E   A   R   C   H       R   E   S   U   L   T   S    */

    private void displayStaff(List<Staff> matched_staff, Meta page_meta, MiniDialogController mini_dialog_controller){
        
        
        for(Staff current_staff : matched_staff){

            VBox current_single_staff= new SingleStaff(current_staff);
            HBox anchor_hbox= new HBox();
            anchor_hbox.getStyleClass().addAll("sub-section-style", "transparent-background");
            anchor_hbox.setSpacing(30);
            anchor_hbox.setAlignment(Pos.CENTER);
            anchor_hbox.setPadding(new Insets(10));
            anchor_hbox.getChildren().add(current_single_staff);

            // ADD TO anchor_vbox
            anchor_vbox.getChildren().add(anchor_hbox);

        }


        // SETUP PAGINATION
        setupPagination(page_meta, new HashMap<String, String>(), this::loadStaff);

        // Reset ScrollPane to top
        content_scrollpane.setVvalue(0);

        // REMOVE LOADING DIALOG
        mini_dialog_controller.handleExit();


    }


    private void loadStaff(Map<String, String> post_data, int page_number){

        try{

            // CREATE NEW STAFF RESOURCE
            StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

            // SET CURRENT PAGE
            staff_resource.setPage_number(page_number);


            // SHOW LOADING DIALOG
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();


            // GET STAFF
            Runnable load_staff_runnable= new Runnable(){
            
                @Override
                public void run() {
                    
                    Platform.runLater( ()->{

                        try{

                            // ATTEMPT TO GET STAFF
                            String matched_staff_string= staff_resource.all();

                            // PARSE RESULT
                            StaffCollection matched_staff_collection= APIParser.getInstance().parseMultiStaffResponse(matched_staff_string);

                            // IF THERE ARE NO MATCHES
                            if(matched_staff_collection.getStaffs().size() <= 0){

                                mini_dialog_controller.setDialog_text_label("NO STAFF FOUND.");

                            }

                            // DISPLAY RESULT
                            displayStaff(matched_staff_collection.getStaffs(), matched_staff_collection.getMeta(), mini_dialog_controller);



                        }catch(Exception e){

                            mini_dialog_controller.setDialog_text_label(e.getMessage());
                            mini_dialog_controller.enableCloseButton();

                            e.printStackTrace();

                        }

                    } );

                }
            };


            // RUN
            load_staff_runnable.run();



        }catch(Exception e){

            e.printStackTrace();

        }

    }


    /*========================================================================================*/
    /*  E   V   E   N   T       H   A   N   D   L   E   R   S   */
    private void handleDelete(String staff_email){

        try{

            // DISPLAY LOADING DIALOG
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            // CREATE NEW STAFF RESOURCE
            StaffResource staff_resource= new StaffResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());


            // CREATE Runnable
            Runnable delete_staff_runnable= new Runnable(){
            
                @Override
                public void run() {
                    
                    Platform.runLater( ()->{

                        try{

                            // ATTEMPT DELETE
                            staff_resource.delete(staff_email);

                            // SUCCESS
                            mini_dialog_controller.setDialog_text_label("STAFF WITH EMAIL " + staff_email + " DELETED SUCCESSFULLY.");

                            // ENABLE CLOSE
                            mini_dialog_controller.enableCloseButton();



                        }catch(Exception e){

                            mini_dialog_controller.setDialog_text_label(e.getMessage());
                            mini_dialog_controller.enableCloseButton();
                            e.printStackTrace();

                        }


                    } );

                }
            };

            // RUN
            delete_staff_runnable.run();



        }catch(Exception e){
            e.printStackTrace();
        }

    }


    /*========================================================================================*/
    /*  I   N   N   E   R       C   L   A   S   S    */


    class SingleStaff extends VBox{

        SingleStaff(Staff current_staff){

            this.setSpacing(20);
            this.setPrefWidth(500);
            this.setMaxWidth(500);

            GridPane gridpane= new GridPane();
            gridpane.setPrefWidth(10000000);

            // LABELS VBOX
            VBox text_vbox= new VBox();
            text_vbox.setSpacing(5);

            Label name_label= new Label();
            StringBuilder name_SB= new StringBuilder(current_staff.getFirst_name());
            name_SB.append(" ").append(current_staff.getLast_name());
            name_label.setText(name_SB.toString());
            name_label.getStyleClass().addAll("mid-body-font", "boldened");

            text_vbox.getChildren().add(name_label);

            Label email_label= new Label();
            email_label.setText(current_staff.getEmail());
            email_label.getStyleClass().add("mid-body-font");

            text_vbox.getChildren().add(email_label);

            Label gender_label= new Label();
            gender_label.setText(current_staff.getGender());
            gender_label.getStyleClass().add("mid-body-font");

            text_vbox.getChildren().add(gender_label);

            Label phone_number_label= new Label();
            phone_number_label.setText((current_staff.getPhone_numbers().size() > 0) ? current_staff.getPhone_numbers().get(0) : "");
            phone_number_label.getStyleClass().add("mid-body-font");

            text_vbox.getChildren().add(phone_number_label);

            Label alt_phone_number_label= new Label();
            alt_phone_number_label.setText((current_staff.getPhone_numbers().size() > 1) ? current_staff.getPhone_numbers().get(1) : "");
            alt_phone_number_label.getStyleClass().add("mid-body-font");

            // IF alt_phone_numer IS NOT EMPTY, ADD TO text_vbox
            if(!alt_phone_number_label.getText().equals("")){
                text_vbox.getChildren().add(alt_phone_number_label);
            }

            Label pending_orders_label= new Label();
            StringBuilder pending_SB= new StringBuilder("Pending Orders: ");
            pending_SB.append(current_staff.getPending_orders());
            pending_orders_label.setText(pending_SB.toString());
            pending_orders_label.getStyleClass().add("mid-body-font");

            text_vbox.getChildren().add(pending_orders_label);

            // Add text_vbox to gridpane
            gridpane.getChildren().add(text_vbox);
            GridPane.setColumnIndex(text_vbox, 0);
            GridPane.setRowIndex(text_vbox, 0);
            GridPane.setHgrow(text_vbox, Priority.ALWAYS);


            // DELETE VBOX
            VBox delete_vbox= new VBox();

            Button delete_button= new Button();
            delete_button.getStyleClass().addAll("file-chooser-button", "image-view-button");
            delete_button.setAlignment(Pos.TOP_RIGHT);
            delete_button.setPrefWidth(2);
            delete_button.setMaxWidth(2);
            Image delete_icon= new Image(MainController.class.getResourceAsStream("img/delete.png"));
            ImageView delete_IV= new ImageView(delete_icon);
            delete_button.setGraphic(delete_IV);
            delete_button.setTooltip(new Tooltip("delete"));
            delete_button.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent event) {
                    handleDelete(current_staff.getEmail());
                }
            });

            delete_vbox.getChildren().add(delete_button);

            // Add delete_vbox to gridpane
            gridpane.getChildren().add(delete_vbox);
            GridPane.setColumnIndex(delete_vbox, 1);
            GridPane.setRowIndex(delete_vbox, 0);


            // Add gridpane to SingleStaff 
            this.getChildren().add(gridpane);


            // CREATE SEPERATION BAR
            Label sep_bar= new Label("\'");
            sep_bar.setMaxWidth(1000000);
            sep_bar.setPrefHeight(2);
            sep_bar.setMinHeight(2);
            sep_bar.getStyleClass().add("darker-background");


            // Add sep_bar to SingleStaff
            this.getChildren().add(sep_bar);

            // Set padding
            this.setPadding(new Insets(10));


        }

    }

}
