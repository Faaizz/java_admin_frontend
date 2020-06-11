package com.faaizz.dev.online_platform.GUI.controller.products.trends;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.GUI.controller.validators.Validators;
import com.faaizz.dev.online_platform.api_inbound.model.Trend;
import com.faaizz.dev.online_platform.api_inbound.model.collection.TrendCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableTrend;
import com.faaizz.dev.online_platform.api_outbound.platform.TrendResource;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class ManageTrendsController extends GenericTrendController {

    @FXML
    protected VBox decoy_labels_vbox;

    @FXML
    protected Button search_trends_button;

    public void initialize() throws Exception {
        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // Select the first item by default
        gender_dropdown.getSelectionModel().selectFirst();

    }


    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleSearchTrend() throws IOException {

        // SETUP TREND RESOURCE
        TrendResource trendResource= new TrendResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
        // SETUP SEARCH PARAMETERS
        Map<String, String> post_data= new HashMap<>();

        post_data.put("gender", (String)gender_dropdown.getSelectionModel().getSelectedItem());

        if(!name_textfield.getText().isEmpty()){
            post_data.put("name", name_textfield.getText());
        }

        // SHOW LOADING MINI DIALOG
        // Call inherited method from MainController class
        MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

        Runnable search_runnable= new Runnable() {
            @Override
            public void run() {
                Platform.runLater( ()->{
                    try {

                        //PERFORM SEARCH
                        String matched_trends_string= trendResource.search(post_data);

                        //PARSE RESPONSE
                        TrendCollection matched_trends= APIParser.getInstance().parseMultiTrendResponse(matched_trends_string);

                        // IF NO TREND IS FOUND
                        if(matched_trends.getTrends().size() <= 0){

                            mini_dialog_controller.enableCloseButton();

                            mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY TREND WITH THE SPECIFIED SEARCH PARAMETERS.");
                        
                        }
                        // OTHERWISE
                        else{

                            // DISPLAY MATCHED TRENDS
                            displayTrends(matched_trends.getTrends(), matched_trends.getMeta(), post_data);

                            // REMOVE LOADING DIALOG
                            mini_dialog_controller.handleExit();
                        }
                    } catch (Exception e) {
                        mini_dialog_controller.enableCloseButton();
                        mini_dialog_controller.setDialog_text_label("An Error Occurred\n" + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        };

        // EXECUTE RUNNABLE
        search_runnable.run();

    }

    
    /*========================================================================================*/
    /*  H   A   N   D   L   E       S   E   A   R   C   H       R   E   S   U   L   T   S    */


    private void displayTrends(List<Trend> matched_trends, Meta page_meta, Map<String, String> post_data) throws IOException {

        // SETUP NEW CONTENT NODE
        FlowPane flow_pane= new FlowPane();
        flow_pane.setAlignment(Pos.TOP_LEFT);
        flow_pane.setVgap(30);
        flow_pane.setHgap(30);

        // CREATE SINGLE TREND VBOX FOR EACH TREND AND ADD TO flow_pane CHILDREN
        for(Trend trend : matched_trends) {

            Platform.runLater(
                new Runnable(){
                
                    @Override
                    public void run() {
                        VBox v_box= new VBox();
                        v_box.getStyleClass().add("single-product-vbox");
                        v_box.setMaxWidth(150);
                        v_box.setMinWidth(150);

                        ImageView image_view= new ImageView();

                        // BUILD IMAGE URL BY APPENDING THE API BASE URL AND THE PATH TO THE IMAGES TO THE FIRST TREND IMAGE NAME
                        StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(trend.getImages().get(0));

                        Image image= new Image(image_urlSB.toString(), 150, 0, true, false);
                        image_view.setImage(image);

                        v_box.getChildren().add(image_view);

                        // IMAGE LABELS
                        VBox v_box_inner= new VBox();
                        v_box_inner.setMaxWidth(150);
                        v_box_inner.setMinWidth(150);

                        Label trend_id= new Label(String.valueOf(trend.getId()));
                        trend_id.setTextAlignment(TextAlignment.LEFT);

                        Label trend_name= new Label(trend.getName());
                        trend_name.setTextAlignment(TextAlignment.LEFT);
                        trend_name.getStyleClass().add("mid-body-font");

                        // APPEND trend_id AND trend_name TO v_box_inner
                        v_box_inner.getChildren().add(trend_id);
                        v_box_inner.getChildren().add(trend_name);

                        // APPEND v_box_inner TO v_box
                        v_box.getChildren().add(v_box_inner);

                        // SET onMouseClickHandler TO HANDLE CLICKING OF A TREND
                        v_box.setOnMouseClicked( new EventHandler<MouseEvent>(){


                            @Override
                            public void handle(MouseEvent event) {
                                
                                singleTrendLoad(trend, page_meta, post_data);

                            }

                            
                        } );

                        // APPEND v_box TO FlowPane
                        flow_pane.getChildren().add(v_box);
                    }
                }
            );

        }

        // ATTACH flow_pane TO scroll_pane
        content_scrollpane.setContent(flow_pane);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadNewTrends);

    }

    private void loadNewTrends(Map<String, String> post_data, int page_number) {

        // SETUP TREND RESOURCE
        TrendResource trendResource= new TrendResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
        // SET REQUEST PAGE
        trendResource.setPage_number(page_number);

        try{

            // SHOW LOADING MINI DIALOG
            // Call inherited method from MainController class
            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

            Runnable search_runnable= new Runnable() {
                @Override
                public void run() {
                    Platform.runLater( ()->{
                        try {

                            //PERFORM SEARCH
                            String matched_trends_string= trendResource.search(post_data);

                            //PARSE RESPONSE
                            TrendCollection matched_trends= APIParser.getInstance().parseMultiTrendResponse(matched_trends_string);

                            // IF NO TREND IS FOUND
                            if(matched_trends.getTrends().size() <= 0){
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY TREND WITH THE SPECIFIED SEARCH PARAMETERS.");
                            }
                            // OTHERWISE
                            else{

                                // DISPLAY MATCHED TRENDS
                                displayTrends(matched_trends.getTrends(), matched_trends.getMeta(), post_data);

                                // REMOVE LOADING DIALOG
                                mini_dialog_controller.handleExit();
                            }

                        } catch (Exception e) {
                            mini_dialog_controller.enableCloseButton();
                            mini_dialog_controller.setDialog_text_label("An Error Occurred\n" + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            };

            // EXECUTE RUNNABLE
            search_runnable.run();

        }catch(IOException e){
            e.printStackTrace();
        }


    }

    /*========================================================================================*/
    /*  H   A   N   D   L   E       S   I   N   G   L   E       T   R   E   N   D    */

    /**
     * IMPORTANT: It is very important to note that validation done before UPDATE action is performed on LOCAL 
     * variable nodes (TextFields, TextArea, ComboBoxes, etc) and not on the inherited variable nodes from GenericProductController
     * @param trend
     */
    public void singleTrendLoad(Trend trend, Meta page_meta, Map<String, String> post_data){

        // Reset image_files_list
        image_files_map= new HashMap<>();

        // REMOVE PAGINATION
        if( h_box_pagination != null ){
            center_vbox.getChildren().remove(h_box_pagination);
        }
        // REMOVE PAGINATION
        if( h_box_pagination_direct != null ){
            center_vbox.getChildren().remove(h_box_pagination_direct);
        }

        // TOPMOST VBOX
        VBox top_vbox= new VBox();
        top_vbox.getStyleClass().add("sub-section-style");
        top_vbox.setSpacing(10);
        top_vbox.setAlignment(Pos.CENTER);
        top_vbox.setPadding(new Insets( 0, 0, 15, 0 ));

        
        // BACK HBOX
        HBox h_box_back= new HBox();
        h_box_back.getStyleClass().add("sub-section-style");
        h_box_back.setAlignment(Pos.CENTER_LEFT);
        h_box_back.setPadding(new Insets( 5, 5, 5, 5 ));
        // BACK BUTTON
        Button back_button= new Button("< back to search results");
        back_button.setAlignment(Pos.CENTER);
        back_button.getStyleClass().add("file-chooser-button");
        back_button.setMinHeight(35);
        back_button.setPrefHeight(35);
        back_button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                try{
                    // Call inherited method from MainController class to display loading dialog
                    MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                    // ADD TREND TO DATABASE IN SEPARATE THREAD
                    Runnable return_to_search_runnable = new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                try {
                                    // ATTEMPT TO LOAD SEARCH RESULTS
                                   loadNewTrends(post_data, page_meta.getCurrent_page());
                                    // If successful, remove loading dialog
                                    mini_dialog_controller.handleExit();
                                } catch (Exception e) {
                                    mini_dialog_controller.enableCloseButton();
                                    mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                                }
                            });
                        }
                    };

                    // ATTEMPT TO LOAD SEARCH RESULTS
                    return_to_search_runnable.run();


                }catch(Exception e){
                    e.printStackTrace();
                }
                    
            }

        });


        h_box_back.getChildren().add(back_button);
        top_vbox.getChildren().add(h_box_back);

        // TOPMOST CONTENT HBOX
        HBox h_box1= new HBox();
        h_box1.getStyleClass().add("sub-section-style");
        h_box1.setSpacing(30);
        h_box1.setAlignment(Pos.CENTER);

        // L    A   B   E   L   S
        VBox labels_vbox= new VBox();
        labels_vbox.setSpacing(10);

        Label id_label= new Label("ID:");
        id_label.setAlignment(Pos.CENTER_RIGHT);
        id_label.setMinHeight(30);
        id_label.setPrefHeight(30);
        id_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(id_label);

        Label name_label= new Label("Name:");
        name_label.setAlignment(Pos.CENTER_RIGHT);
        name_label.setMinHeight(30);
        name_label.setPrefHeight(30);
        name_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(name_label);

        Label description_label= new Label("Description:");
        description_label.setAlignment(Pos.CENTER_RIGHT);
        description_label.setMinHeight(70);
        description_label.setPrefHeight(70);
        description_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(description_label);

        Label gender_label= new Label("Gender:");
        gender_label.setAlignment(Pos.CENTER_RIGHT);
        gender_label.setMinHeight(30);
        gender_label.setPrefHeight(30);
        gender_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(gender_label);

        // DECOY LABELS VBOX
        VBox decoy_labels_vbox= new VBox();
        this.decoy_labels_vbox= decoy_labels_vbox;

        // ADD VBOX TO TOPMOST HBOX
        h_box1.getChildren().add(labels_vbox);


        //  T   E   X   T   F   I   E   L   D   S
        List<TextField> validation_text_textfields= new ArrayList<>();

        VBox textfields_vbox= new VBox();
        textfields_vbox.setSpacing(10);

        TextField id_textfield= new TextField(String.valueOf(trend.getId()));
        id_textfield.setDisable(true);
        id_textfield.setMinHeight(30);
        id_textfield.setPrefHeight(30);
        id_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(id_textfield);
        validation_text_textfields.add(id_textfield);

        TextField name_textfield= new TextField(trend.getName());
        name_textfield.setMinHeight(30);
        name_textfield.setPrefHeight(30);
        name_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(name_textfield);
        validation_text_textfields.add(name_textfield);

        TextArea description_textarea= new TextArea(trend.getDescription());
        description_textarea.setMinHeight(70);
        description_textarea.setPrefHeight(70);
        description_textarea.getStyleClass().add("big-body-textarea");
        textfields_vbox.getChildren().add(description_textarea);

        ComboBox gender_dropdown= new ComboBox<>();
        gender_dropdown.getStyleClass().add("big-body-combobox");
        this.gender_dropdown= gender_dropdown;
        textfields_vbox.getChildren().add(gender_dropdown);

        // POPULATE COMBO BOXES
        super.initializeGender();

        // SELECT APPROPRAITE VALUES
        gender_dropdown.getSelectionModel().select(trend.getGender());

        // IMAGES
        HBox view_and_replace_hbox= new HBox();
        view_and_replace_hbox.setSpacing(10);

        VBox view_vbox= new VBox();
        view_vbox.setSpacing(10);

        // EVENT HANDLER TO HANDLE IMAGE VIEW
        EventHandler<ActionEvent> handle_image_view= new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {

                // GET IMAGE NUMBER
                Button button= (Button)event.getSource();

                int image_number= 1;

                switch (button.getText()){

                    case "View Image One": image_number= 0;
                        break;

                    case "View Image Two": image_number= 1;
                        break;

                    case "View Image Three": image_number= 2;
                        break;

                }

                int finalImage_number = image_number;

                Runnable search_runnable= new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater( ()->{
                            try {

                                // SHOW LOADING MINI DIALOG
                                // Call inherited method from MainController class
                                MiniDialogController mini_dialog_controller= showLoadingMiniDialog();
                                mini_dialog_controller.enableCloseButton();

                                ImageView image_view= new ImageView();

                                // BUILD IMAGE URL BY APPENDING THE API BASE URL AND THE PATH TO THE IMAGES TO THE FIRST TREND IMAGE NAME
                                StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(trend.getImages().get(finalImage_number));

                                Image image= new Image(image_urlSB.toString(), 150, 0, true, false);
                                image_view.setImage(image);

                                mini_dialog_controller.setDialog_vbox_content(image_view);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                };

                // EXECUTE RUNNABLE
                search_runnable.run();
            }
        };

        Button image_one_view_image= new Button("View Image One");
        image_one_view_image.setTextAlignment(TextAlignment.RIGHT);
        image_one_view_image.getStyleClass().add("file-chooser-button");
        image_one_view_image.getStyleClass().add("image-view-button");
        image_one_view_image.setMinHeight(35);
        image_one_view_image.setPrefHeight(35);
        image_one_view_image.setMaxWidth(1000000000);
        image_one_view_image.setOnAction(handle_image_view);
        view_vbox.getChildren().add(image_one_view_image);

        Button image_two_view_image= new Button("View Image Two");
        image_two_view_image.setTextAlignment(TextAlignment.RIGHT);
        image_two_view_image.getStyleClass().add("file-chooser-button");
        image_two_view_image.getStyleClass().add("image-view-button");
        image_two_view_image.setMinHeight(35);
        image_two_view_image.setPrefHeight(35);
        image_two_view_image.setMaxWidth(1000000000);
        image_two_view_image.setOnAction(handle_image_view);
        view_vbox.getChildren().add(image_two_view_image);

        Button image_three_view_image= new Button("View Image Three");
        image_three_view_image.setTextAlignment(TextAlignment.RIGHT);
        image_three_view_image.getStyleClass().add("file-chooser-button");
        image_three_view_image.getStyleClass().add("image-view-button");
        image_three_view_image.setMinHeight(35);
        image_three_view_image.setPrefHeight(35);
        image_three_view_image.setMaxWidth(1000000000);
        image_three_view_image.setOnAction(handle_image_view);
        view_vbox.getChildren().add(image_three_view_image);

        view_and_replace_hbox.getChildren().add(view_vbox);

        VBox replace_vbox= new VBox();
        replace_vbox.setSpacing(10);

        List<Button> validation_image_buttons= new ArrayList<>();

        Button image_one_file_chooser= new Button("Replace Image One");
        image_one_file_chooser.setTextAlignment(TextAlignment.RIGHT);
        image_one_file_chooser.getStyleClass().add("file-chooser-button");
        image_one_file_chooser.setMinHeight(35);
        image_one_file_chooser.setPrefHeight(35);
        image_one_file_chooser.setMaxWidth(1000000000);
        image_one_file_chooser.setOnAction(this::handleFileChooser);
        replace_vbox.getChildren().add(image_one_file_chooser);
        validation_image_buttons.add(image_one_file_chooser);

        Button image_two_file_chooser= new Button("Replace Image Two");
        image_two_file_chooser.setTextAlignment(TextAlignment.RIGHT);
        image_two_file_chooser.getStyleClass().add("file-chooser-button");
        image_two_file_chooser.setMinHeight(35);
        image_two_file_chooser.setPrefHeight(35);
        image_two_file_chooser.setMaxWidth(1000000000);
        image_two_file_chooser.setOnAction(this::handleFileChooser);
        replace_vbox.getChildren().add(image_two_file_chooser);
        validation_image_buttons.add(image_two_file_chooser);

        Button image_three_file_chooser= new Button("Replace Image Three");
        image_three_file_chooser.setTextAlignment(TextAlignment.RIGHT);
        image_three_file_chooser.getStyleClass().add("file-chooser-button");
        image_three_file_chooser.setMinHeight(35);
        image_three_file_chooser.setPrefHeight(35);
        image_three_file_chooser.setMaxWidth(1000000000);
        image_three_file_chooser.setOnAction(this::handleFileChooser);
        replace_vbox.getChildren().add(image_three_file_chooser);
        validation_image_buttons.add(image_three_file_chooser);

        view_and_replace_hbox.getChildren().add(replace_vbox);
        view_and_replace_hbox.setPadding(new Insets( 0, 0, 15, 0 ));

        textfields_vbox.getChildren().add(view_and_replace_hbox);


        // OPTIONS HBOX
        HBox actions_hbox= new HBox();
        actions_hbox.setSpacing(10);
        actions_hbox.setAlignment(Pos.CENTER_LEFT);

        // UPDATE TREND BUTTON
        Button update_trend_button= new Button("Update Trend");
        update_trend_button.setAlignment(Pos.CENTER);
        update_trend_button.getStyleClass().addAll("mid-body-font", "dark-color-style", "dark-color-button");
        update_trend_button.setMinHeight(35);
        update_trend_button.setPrefHeight(35);
        update_trend_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnable= ()->{

                    // PERFORM TREND UPDATE IN SEPERATE THREAD
                    Platform.runLater(()->{

                        // VALIDATE TEXT ENTRIES
                        int[] validation_problems= {0};
                        
                        // PERFORM VALIDATION
                        Validators.validateTextFields(validation_text_textfields, validation_problems);

                        // VALIDATE DESCRIPTION TEXTAREA
                        List<TextArea> textareas= new ArrayList<>();
                        textareas.add(description_textarea);
                        Validators.validateTextAreas(textareas, validation_problems);

                        // VALIDATE DROPDOWNS
                        List<ComboBox> dropdowns= new ArrayList<>();
                        dropdowns.add(gender_dropdown);
                        Validators.validateDropdowns(dropdowns, validation_problems);


                        // IF VALIDATION SUCEEDS
                        if(validation_problems[0] == 0){

                            // SETUP TREND RESOURCE
                            TrendResource trendResource = new TrendResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());


                            // CREATE UploadableTrend INSTANCE
                            UploadableTrend to_update = new UploadableTrend(
                                name_textfield.getText(),
                                description_textarea.getText(),
                                (String) gender_dropdown.getSelectionModel().getSelectedItem(),
                                (image_files_map.containsKey("one") ? image_files_map.get("one") : null)
                            );

                            // ATTEMPT UPDATE
                            try{

                                // Call inherited method from MainController class to display loading dialog
                                MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                                // ADD TREND TO DATABASE IN SEPARATE THREAD
                                Runnable update_trend_runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Platform.runLater(() -> {
                                            try {
                                                trendResource.update(Integer.valueOf(id_textfield.getText()) ,to_update);
                                                // If successful, remove loading dialog
                                                mini_dialog_controller.enableCloseButton();
                                                mini_dialog_controller.setDialog_text_label("Trend Updated Successfully");


                                            } catch (Exception e) {
                                                mini_dialog_controller.enableCloseButton();
                                                mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                                            }
                                        });
                                    }
                                };

                                // ATTEMPT UPDATE
                                update_trend_runnable.run();


                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            

                        }

                    });

                };

                runnable.run();
            }
        });
        
        actions_hbox.getChildren().add(update_trend_button);

        // DELETE TREND BUTTON
        Button delete_trend_button= new Button();
        delete_trend_button.setAlignment(Pos.CENTER);
        delete_trend_button.getStyleClass().addAll("mid-body-font", "dark-color-style", "dark-color-button");
        delete_trend_button.setMinHeight(35);
        delete_trend_button.setPrefHeight(35);
        delete_trend_button.setMaxWidth(10);
        delete_trend_button.setGraphic(new ImageView(new Image(MainController.class.getResourceAsStream("img/delete.png"))));
        delete_trend_button.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                
                Runnable runnable= ()->{

                    // PERFORM TREND DELETE IN SEPERATE THREAD
                    Platform.runLater(()->{

                        // ATTEMPT DELETE
                        try{

                            // SETUP TREND RESOURCE
                            TrendResource trendResource = new TrendResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());


                            // Call inherited method from MainController class to display loading dialog
                            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                            // ADD TREND TO DATABASE IN SEPARATE THREAD
                            Runnable delete_trend_runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Platform.runLater(() -> {
                                        try {
                                            trendResource.delete(Integer.valueOf(id_textfield.getText()));
                                            // If successful, remove loading dialog
                                            mini_dialog_controller.enableCloseButton();
                                            mini_dialog_controller.setDialog_text_label("Delete Successful");
                                            // GO BACK TO SEARCH PAGE
                                            handleRedirectToManageTrends();

                                        } catch (Exception e) {
                                            mini_dialog_controller.enableCloseButton();
                                            mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                                        }
                                    });
                                }
                            };

                            // ATTEMPT UPDATE
                            delete_trend_runnable.run();


                        }catch(Exception e){
                            e.printStackTrace();
                        }
                            

                    });

                };

                runnable.run();
            }
            
        } );

        actions_hbox.getChildren().add(delete_trend_button);


        textfields_vbox.getChildren().add(actions_hbox);

        // ADD VBOX TO TOPMOST HBOX
        h_box1.getChildren().add(textfields_vbox);

        // ADD h_box1 to top_vbox
        top_vbox.getChildren().add(h_box1);


        // SET content_scrollpane CONTENT TO TOPMOST HBOS
        content_scrollpane.setContent(top_vbox);

    }


}
