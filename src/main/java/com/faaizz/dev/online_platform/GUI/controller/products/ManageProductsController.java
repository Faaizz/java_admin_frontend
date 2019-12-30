package com.faaizz.dev.online_platform.GUI.controller.products;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.MainController;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.collection.ProductCollection;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.model.UploadableProduct;
import com.faaizz.dev.online_platform.api_outbound.platform.ProductResource;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ManageProductsController extends GenericProductController {

    @FXML
    protected TextField min_price_textfield;
    @FXML
    protected TextField max_price_textfield;
    @FXML
    protected TextField brand_textfield;
    @FXML
    protected Button search_products_button;

    public void initialize() throws Exception {
        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();

        // Select the first item by default
        section_dropdown.getSelectionModel().selectFirst();
        handleSectionChange(null);
        handleSub_sectionChange(null);

        // Set "ENTER" key event on brand_textfield to trigger login attempt
        brand_textfield.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    handleSearchProduct();;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        );

    }


    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleSearchProduct() throws IOException {
        // VALIDATE ENTRIES
        if(validateSearchFields()){

            // SUCCESS VALIDATION
            // SETUP PRODUCT RESOURCE
            ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
            // SETUP SEARCH PARAMETERS
            Map<String, String> post_data= new HashMap<>();

            post_data.put("section", (String)section_dropdown.getSelectionModel().getSelectedItem());
            post_data.put("sub_section", (String)sub_section_dropdown.getSelectionModel().getSelectedItem());
            post_data .put("category", (String)category_dropdown.getSelectionModel().getSelectedItem());

            if(!min_price_textfield.getText().isEmpty()){
                post_data.put("min_price", min_price_textfield.getText());
            }

            if(!max_price_textfield.getText().isEmpty()){
                post_data.put("max_price", max_price_textfield.getText());
            }


            if(!name_textfield.getText().isEmpty()){
                post_data.put("name", name_textfield.getText());
            }
            
            if(!brand_textfield.getText().isEmpty()){
                post_data.put("brand", brand_textfield.getText());
            }

            if(!color_textfield.getText().isEmpty()){
                post_data.put("color", color_textfield.getText());
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
                            String matched_products_string= productResource.search(post_data);

                            //PARSE RESPONSE
                            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

                            // IF NO PRODUCT IS FOUND
                            if(matched_products.getProducts().size() <= 0){

                                mini_dialog_controller.enableCloseButton();

                                mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY PRODUCT WITH THE SPECIFIED SEARCH PARAMETERS.");
                            
                            }
                            // OTHERWISE
                            else{

                                // DISPLAY MATCHED PRODUCTS
                                displayProducts(matched_products.getProducts(), matched_products.getMeta(), post_data);
    
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
    }

    public boolean validateSearchFields(){

        final int[] validation_problems = {0}; // Integer to keep count of invalid entries. Array is used because of lambda compatibility
        final String CSS_RED_BORDERS= "red_borders";

        /*========================================================================================*/
        // VALIDATE PRICE FIELDS AND SECTION DROPDOWNS
        List<TextField> textfields= new ArrayList<>();

        textfields.add(min_price_textfield);
        textfields.add(max_price_textfield);

        // VALIDATE NUMBERS
        List<TextField> expect_numbers= new ArrayList<>();
        expect_numbers.add(min_price_textfield);
        expect_numbers.add(max_price_textfield);

        // Loop through expect_numbers
        expect_numbers.forEach( textfield ->{

            try{
                if(!textfield.getText().isEmpty()){
                    // Try to parse text content to number
                    Double.valueOf(textfield.getText());
                }
            }catch(NumberFormatException e){
                // Set red_borders css class
                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                }
                // Increment validation_problems
                validation_problems[0]++;
            }

        } );


        // VALIDATE DROPDOWNS
        List<ComboBox> dropdowns= new ArrayList<>();
        dropdowns.add(section_dropdown);
        dropdowns.add(sub_section_dropdown);
        dropdowns.add(category_dropdown);

        // Loop through dropdowns
        dropdowns.forEach( dropdown ->{
            // If no item is selected, add CSS red_borders class
            if(dropdown.getSelectionModel().getSelectedItem() == null){
                if(!dropdown.getStyleClass().contains(CSS_RED_BORDERS)){
                    dropdown.getStyleClass().add(CSS_RED_BORDERS);
                }

                // Increment validation_problems
                validation_problems[0]++;
            }else{
                // Remove CSS red_borders
                dropdown.getStyleClass().remove(CSS_RED_BORDERS);
            }
        } );

        return (validation_problems[0] <=0);
    }


    /*========================================================================================*/
    /*  H   A   N   D   L   E       S   E   A   R   C   H       R   E   S   U   L   T   S    */


    private void displayProducts(List<Product> matched_products, Meta page_meta, Map<String, String> post_data) throws IOException {

        // SETUP NEW CONTENT NODE
        FlowPane flow_pane= new FlowPane();
        flow_pane.setAlignment(Pos.TOP_LEFT);
        flow_pane.setVgap(30);
        flow_pane.setHgap(30);

        // CREATE SINGLE PRODUCT VBOX FOR EACH PRODUCT AND ADD TO flow_pane CHILDREN
        for(Product product : matched_products) {

            VBox v_box= new VBox();
            v_box.getStyleClass().add("single-product-vbox");
            v_box.setMaxWidth(150);
            v_box.setMinWidth(150);

            ImageView image_view= new ImageView();

            // BUILD IMAGE URL BY APPENDING THE API BASE URL AND THE PATH TO THE IMAGES TO THE FIRST PRODUCT IMAGE NAME
            StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(product.getImages().get(0));

            // GET IMAGE AS BufferedStream
            CloseableHttpClient http_client= HttpClients.createDefault();
            HttpGet http_get= new HttpGet(image_urlSB.toString());

            CloseableHttpResponse response = http_client.execute(http_get);
            HttpEntity entity = response.getEntity();

            BufferedInputStream image_imput_stream = new BufferedInputStream(entity.getContent());


            Image image= new Image(image_imput_stream, 150, 0, true, false);
            image_view.setImage(image);

            v_box.getChildren().add(image_view);

            // IMAGE LABELS
            VBox v_box_inner= new VBox();
            v_box_inner.setMaxWidth(150);
            v_box_inner.setMinWidth(150);

            Label product_id= new Label(String.valueOf(product.getId()));
            product_id.setTextAlignment(TextAlignment.LEFT);

            Label product_name= new Label(product.getName());
            product_name.setTextAlignment(TextAlignment.LEFT);
            product_name.getStyleClass().add("mid-body-font");

            // APPEND product_id AND product_name TO v_box_inner
            v_box_inner.getChildren().add(product_id);
            v_box_inner.getChildren().add(product_name);

            // APPEND v_box_inner TO v_box
            v_box.getChildren().add(v_box_inner);

            // SET onMouseClickHandler TO HANDLE CLICKING OF A PRODUCT
            v_box.setOnMouseClicked( new EventHandler<MouseEvent>(){


                @Override
                public void handle(MouseEvent event) {
                    
                    singleProductLoad(product, page_meta, post_data);

                }

                
            } );

            // APPEND v_box TO FlowPane
            flow_pane.getChildren().add(v_box);

        }

        // ATTACH flow_pane TO scroll_pane
        content_scrollpane.setContent(flow_pane);

        // SETUP PAGINATION
        setupPagination(page_meta, post_data, this::loadNewProducts);

    }

    private void loadNewProducts(Map<String, String> post_data, int page_number) {

        // SETUP PRODUCT RESOURCE
        ProductResource productResource= new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());
        // SET REQUEST PAGE
        productResource.setPage_number(page_number);

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
                            String matched_products_string= productResource.search(post_data);

                            //PARSE RESPONSE
                            ProductCollection matched_products= APIParser.getInstance().parseMultiProductResponse(matched_products_string);

                            // IF NO PRODUCT IS FOUND
                            if(matched_products.getProducts().size() <= 0){
                                mini_dialog_controller.enableCloseButton();
                                mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY PRODUCT WITH THE SPECIFIED SEARCH PARAMETERS.");
                            }
                            // OTHERWISE
                            else{

                                // DISPLAY MATCHED PRODUCTS
                                displayProducts(matched_products.getProducts(), matched_products.getMeta(), post_data);

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
    /*  H   A   N   D   L   E       S   I   N   G   L   E       P   R   O   D   U   C   T    */

    /**
     * IMPORTANT: It is very important to note that validation done before UPDATE action is performed on LOCAL 
     * variable nodes (TextFields, TextArea, ComboBoxes, etc) and not on the inherited variable nodes from GenericProductController
     * @param product
     */
    public void singleProductLoad(Product product, Meta page_meta, Map<String, String> post_data){

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

                    // ADD PRODUCT TO DATABASE IN SEPARATE THREAD
                    Runnable return_to_search_runnable = new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                try {
                                    // ATTEMPT TO LOAD SEARCH RESULTS
                                   loadNewProducts(post_data, page_meta.getCurrent_page());
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

        Label brand_label= new Label("Brand:");
        brand_label.setAlignment(Pos.CENTER_RIGHT);
        brand_label.setMinHeight(30);
        brand_label.setPrefHeight(30);
        brand_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(brand_label);

        Label description_label= new Label("Description:");
        description_label.setAlignment(Pos.CENTER_RIGHT);
        description_label.setMinHeight(70);
        description_label.setPrefHeight(70);
        description_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(description_label);

        Label section_label= new Label("Section:");
        section_label.setAlignment(Pos.CENTER_RIGHT);
        section_label.setMinHeight(30);
        section_label.setPrefHeight(30);
        section_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(section_label);

        Label sub_section_label= new Label("Sub-section:");
        sub_section_label.setAlignment(Pos.CENTER_RIGHT);
        sub_section_label.setMinHeight(30);
        sub_section_label.setPrefHeight(30);
        sub_section_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(sub_section_label);

        Label category_label= new Label("Category:");
        category_label.setAlignment(Pos.CENTER_RIGHT);
        category_label.setMinHeight(30);
        category_label.setPrefHeight(30);
        category_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(category_label);

        Label color_label= new Label("Color:");
        color_label.setAlignment(Pos.CENTER_RIGHT);
        color_label.setMinHeight(30);
        color_label.setPrefHeight(30);
        color_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(color_label);

        Label price_label= new Label("Price:");
        price_label.setAlignment(Pos.CENTER_RIGHT);
        price_label.setMinHeight(30);
        price_label.setPrefHeight(30);
        price_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(price_label);

        Label material_label= new Label("Material:");
        material_label.setAlignment(Pos.CENTER_RIGHT);
        material_label.setMinHeight(30);
        material_label.setPrefHeight(30);
        material_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(material_label);

        Label size_label= new Label("Size:");
        size_label.setAlignment(Pos.CENTER_RIGHT);
        size_label.setMinHeight(30);
        size_label.setPrefHeight(30);
        size_label.getStyleClass().add("mid-body-font");
        labels_vbox.getChildren().add(size_label);

        // DECOY LABELS VBOX
        VBox decoy_labels_vbox= new VBox();
        this.decoy_labels_vbox= decoy_labels_vbox;

        // ADD VBOX TO TOPMOST HBOX
        h_box1.getChildren().add(labels_vbox);


        //  T   E   X   T   F   I   E   L   D   S
        List<TextField> validation_text_textfields= new ArrayList<>();

        VBox textfields_vbox= new VBox();
        textfields_vbox.setSpacing(10);

        TextField id_textfield= new TextField(String.valueOf(product.getId()));
        id_textfield.setDisable(true);
        id_textfield.setMinHeight(30);
        id_textfield.setPrefHeight(30);
        id_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(id_textfield);
        validation_text_textfields.add(id_textfield);

        TextField name_textfield= new TextField(product.getName());
        name_textfield.setMinHeight(30);
        name_textfield.setPrefHeight(30);
        name_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(name_textfield);
        validation_text_textfields.add(name_textfield);

        TextField brand_textfield= new TextField(product.getBrand());
        brand_textfield.setMinHeight(30);
        brand_textfield.setPrefHeight(30);
        brand_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(brand_textfield);
        validation_text_textfields.add(brand_textfield);

        TextArea description_textarea= new TextArea(product.getDescription());
        description_textarea.setMinHeight(70);
        description_textarea.setPrefHeight(70);
        description_textarea.getStyleClass().add("big-body-textarea");
        textfields_vbox.getChildren().add(description_textarea);

        ComboBox section_dropdown= new ComboBox<>();
        section_dropdown.getStyleClass().add("big-body-combobox");
        this.section_dropdown= section_dropdown;
        section_dropdown.setOnAction(this::handleSectionChange);
        textfields_vbox.getChildren().add(section_dropdown);

        ComboBox sub_section_dropdown= new ComboBox<>();
        sub_section_dropdown.getStyleClass().add("big-body-combobox");
        this.sub_section_dropdown= sub_section_dropdown;
        sub_section_dropdown.setOnAction(this::handleSub_sectionChange);
        textfields_vbox.getChildren().add(sub_section_dropdown);

        ComboBox category_dropdown= new ComboBox<>();
        category_dropdown.getStyleClass().add("big-body-combobox");
        this.category_dropdown= category_dropdown;
        textfields_vbox.getChildren().add(category_dropdown);

        // POPULATE COMBO BOXES
        super.initializeSections();

        // SELECT APPROPRAITE VALUES
        section_dropdown.getSelectionModel().select(product.getSection());
        sub_section_dropdown.getSelectionModel().select(product.getSub_section());
        category_dropdown.getSelectionModel().select(product.getCategory());

        TextField color_textfield= new TextField(product.getColor());
        color_textfield.setMinHeight(30);
        color_textfield.setPrefHeight(30);
        color_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(color_textfield);
        validation_text_textfields.add(color_textfield);

        TextField price_textfield= new TextField(String.valueOf(product.getPrice()));
        price_textfield.setMinHeight(30);
        price_textfield.setPrefHeight(30);
        price_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(price_textfield);

        TextField material_textfield= new TextField(product.getMaterial());
        material_textfield.setMinHeight(30);
        material_textfield.setPrefHeight(30);
        material_textfield.getStyleClass().add("big-body-textfield");
        textfields_vbox.getChildren().add(material_textfield);
        validation_text_textfields.add(material_textfield);


        // SIZE AND QUANTITY

        List<TextField> validation_sizes= new ArrayList<>();
        List<TextField> validation_quantities= new ArrayList<>();

        VBox size_and_quantity_vbox= new VBox();
        size_and_quantity_vbox.setSpacing(10);
        this.size_and_quantity_vbox= size_and_quantity_vbox;

        HBox size_and_quantity_hbox= new HBox();

        TextField size_textfield= new TextField(product.getOptions().get(0).getSize());
        size_textfield.setMinHeight(30);
        size_textfield.setPrefHeight(30);
        size_textfield.setMinWidth(70);
        size_textfield.setPrefWidth(70);
        size_textfield.getStyleClass().add("big-body-textfield");
        size_and_quantity_hbox.getChildren().add(size_textfield);
        validation_sizes.add(size_textfield);

        Label quantity_label= new Label("Quantity:");
        quantity_label.setAlignment(Pos.CENTER_RIGHT);
        quantity_label.setMinHeight(30);
        quantity_label.setPrefHeight(30);
        quantity_label.getStyleClass().add("mid-body-font");
        quantity_label.setPadding(new Insets(0, 15, 0, 25));
        size_and_quantity_hbox.getChildren().add(quantity_label);

        TextField quantity_textfield= new TextField(String.valueOf(product.getOptions().get(0).getQuantity()));
        quantity_textfield.setMinHeight(30);
        quantity_textfield.setPrefHeight(30);
        quantity_textfield.setMinWidth(70);
        quantity_textfield.setPrefWidth(70);
        quantity_textfield.getStyleClass().add("big-body-textfield");
        size_and_quantity_hbox.getChildren().add(quantity_textfield);
        validation_quantities.add(quantity_textfield);

        // ADD HBOX TO VBOX
        size_and_quantity_vbox.getChildren().add(size_and_quantity_hbox);

        // SET ADDITIONAL SIZES AND QUANTITIES
        for(int i=1; i < product.getOptions().size(); i++){

            HBox temp_size_and_quantity_hbox= new HBox();

            TextField temp_size_textfield= new TextField(product.getOptions().get(i).getSize());
            temp_size_textfield.setMinHeight(30);
            temp_size_textfield.setPrefHeight(30);
            temp_size_textfield.setMinWidth(70);
            temp_size_textfield.setPrefWidth(70);
            temp_size_textfield.getStyleClass().add("big-body-textfield");
            temp_size_and_quantity_hbox.getChildren().add(temp_size_textfield);
            validation_sizes.add(temp_size_textfield);

            Label temp_quantity_label= new Label("Quantity:");
            temp_quantity_label.setAlignment(Pos.CENTER_RIGHT);
            temp_quantity_label.setMinHeight(30);
            temp_quantity_label.setPrefHeight(30);
            temp_quantity_label.getStyleClass().add("mid-body-font");
            temp_quantity_label.setPadding(new Insets(0, 15, 0, 25));
            temp_size_and_quantity_hbox.getChildren().add(temp_quantity_label);

            TextField temp_quantity_textfield= new TextField(String.valueOf(product.getOptions().get(i).getQuantity()));
            temp_quantity_textfield.setMinHeight(30);
            temp_quantity_textfield.setPrefHeight(30);
            temp_quantity_textfield.setMinWidth(70);
            temp_quantity_textfield.setPrefWidth(70);
            temp_quantity_textfield.getStyleClass().add("big-body-textfield");
            temp_size_and_quantity_hbox.getChildren().add(temp_quantity_textfield);
            validation_quantities.add(temp_quantity_textfield);

            size_and_quantity_vbox.getChildren().add(temp_size_and_quantity_hbox);

        }

        textfields_vbox.getChildren().add(size_and_quantity_vbox);


        // ADD SIZE AND QUANTITY BUTTON
        Button add_size_button= new Button("Add Another Size");
        add_size_button.setAlignment(Pos.CENTER);
        add_size_button.getStyleClass().addAll("mid-body-font", "dark-color-style", "dark-color-button");
        add_size_button.setMinHeight(35);
        add_size_button.setPrefHeight(35);
        add_size_button.setOnAction(this::handleAddSize);
        textfields_vbox.getChildren().add(add_size_button);

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

                                // BUILD IMAGE URL BY APPENDING THE API BASE URL AND THE PATH TO THE IMAGES TO THE FIRST PRODUCT IMAGE NAME
                                StringBuilder image_urlSB= new StringBuilder().append("http://").append(SettingsData.getSettings().getBase_url().strip()).append("/storage/").append(product.getImages().get(finalImage_number));

                                // GET IMAGE AS BufferedStream
                                CloseableHttpClient http_client= HttpClients.createDefault();
                                HttpGet http_get= new HttpGet(image_urlSB.toString());

                                CloseableHttpResponse response = http_client.execute(http_get);
                                HttpEntity entity = response.getEntity();

                                BufferedInputStream image_imput_stream = new BufferedInputStream(entity.getContent());

                                Image image= new Image(image_imput_stream, 150, 0, true, false);
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

        // UPDATE PRODUCT BUTTON
        Button update_product_button= new Button("Update Product");
        update_product_button.setAlignment(Pos.CENTER);
        update_product_button.getStyleClass().addAll("mid-body-font", "dark-color-style", "dark-color-button");
        update_product_button.setMinHeight(35);
        update_product_button.setPrefHeight(35);
        update_product_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Runnable runnable= ()->{

                    // PERFORM PRODUCT UPDATE IN SEPERATE THREAD
                    Platform.runLater(()->{

                        // VALIDATE TEXT ENTRIES
                        int validation_errors= 0;
                        
                        // ADD ADDITIONAL SIZES IF THEY EXIST
                        for(TextField size : size_textfields_list){

                            // IF THE CURRENT ELEMENT IS NULL (CAN RESULT FROM size_textfields_list CONTAINING TextFields
                            // THAT SHOULD HAVE BEEN LOADED FROM AN FXML FILE), THEN SKIP TO THE NEXT ITERATION
                            if(size == null){
                                continue;
                            }

                            validation_sizes.add(size);

                        }

                        // ADD SIZES
                        validation_sizes.forEach( size->{
                            validation_text_textfields.add(size);
                        } );

                        // PERFORM VALIDATION
                        for( TextField textfield: validation_text_textfields){

                            // IF THE CURRENT ELEMENT IS NULL (CAN RESULT FROM size_textfields_list CONTAINING TextFields
                            // THAT SHOULD HAVE BEEN LOADED FROM AN FXML FILE), THEN SKIP TO THE NEXT ITERATION
                            if(textfield == null){
                                continue;
                            }

                            // CHECK IF EMPTY
                            if(textfield.getText().isEmpty()){
                                // INCREMENT VALIDATION ERRORS COUNT
                                validation_errors++;
                                // ADD CSS_RED_BORDERS IF IT HASN'T BEEN ADDED ALREADY
                                if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                                    textfield.getStyleClass().add(CSS_RED_BORDERS);
                                }
                            }
                            // OTHERWISE, IF NOT EMPTY
                            else{
                                // REMOVE CSS_RED_BORDERS
                                if(textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                                    textfield.getStyleClass().remove(CSS_RED_BORDERS);
                                }

                            }

                        }

                        // VALIDATE PRICE
                        if(price_textfield.getText().isEmpty()){
                           // INCREMENT VALIDATION ERRORS COUNT
                           validation_errors++;
                           // ADD CSS_RED_BORDERS IF IT HASN'T BEEN ADDED ALREADY
                           if(!price_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                               price_textfield.getStyleClass().add(CSS_RED_BORDERS);
                           }
                        }
                        // OTHERWISE IF IT'S NOT EMPTY
                        else{
                            // CHECK IF AN INVALID DOUBLE HAS BEEN ENTERED
                            try{
                                Double.valueOf(price_textfield.getText());

                                // VALID PRICE
                                // REMOVE CSS_RED_BORDERS IF THEY HAVE BEEN ADDED PRIOR
                                if(price_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                                    price_textfield.getStyleClass().remove(CSS_RED_BORDERS);
                                }

                            }
                            catch(NumberFormatException e){
                                // INCREMENT VALIDATION ERRORS COUNT
                                validation_errors++;
                                // ADD CSS_RED_BORDERS IF IT HASN'T BEEN ADDED ALREADY
                                if(!price_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                                    price_textfield.getStyleClass().add(CSS_RED_BORDERS);
                                }
                            }

                        }

                        // VALIDATE QUANTITIES
                        // ADD ADDITIONAL QUANTITIES IF THEY EXIST
                        for( TextField quantity : quantity_textfields_list ){
                            // IF THE CURRENT ELEMENT IS NULL (CAN RESULT FROM size_textfields_list CONTAINING TextFields
                            // THAT SHOULD HAVE BEEN LOADED FROM AN FXML FILE), THEN SKIP TO THE NEXT ITERATION
                            if(quantity == null){
                                continue;
                            }
                            validation_quantities.add(quantity);
                        }

                        // PERFORM VALIDATION
                        for( TextField quantity : validation_quantities ){

                            if(quantity.getText().isEmpty()){
                                // INCREMENT VALIDATION ERRORS COUNT
                                validation_errors++;
                                // ADD CSS_RED_BORDERS IF IT HASN'T BEEN ADDED ALREADY
                                if(!quantity.getStyleClass().contains(CSS_RED_BORDERS)){
                                    quantity.getStyleClass().add(CSS_RED_BORDERS);
                                }
                            }
                            // OTHERWISE IF IT'S NOT EMPTY
                            else{
                                // CHECK IF AN INVALID DOUBLE HAS BEEN ENTERED
                                try{
                                    Integer.valueOf(quantity.getText());

                                    // VALID PRICE
                                    // REMOVE CSS_RED_BORDERS IF THEY HAVE BEEN ADDED PRIOR
                                    if(quantity.getStyleClass().contains(CSS_RED_BORDERS)){
                                        quantity.getStyleClass().remove(CSS_RED_BORDERS);
                                    }

                                }
                                catch(NumberFormatException e){
                                    // INCREMENT VALIDATION ERRORS COUNT
                                    validation_errors++;
                                    // ADD CSS_RED_BORDERS IF IT HASN'T BEEN ADDED ALREADY
                                    if(!quantity.getStyleClass().contains(CSS_RED_BORDERS)){
                                        quantity.getStyleClass().add(CSS_RED_BORDERS);
                                    }
                                }

                            }

                        }

                        // VALIDATE DESCRIPTION TEXTAREA
                        if(description_textarea.getText().isEmpty()){
                            // Add CSS red_borders class
                            if(!description_textarea.getStyleClass().contains(CSS_RED_BORDERS)){
                                description_textarea.getStyleClass().add(CSS_RED_BORDERS);
                            }

                            // Increment validation_problems
                            validation_errors++;
                        }else{
                            // Remove CSS red_borders class
                            description_textarea.getStyleClass().remove(CSS_RED_BORDERS);
                        }

                        // VALIDATE DROPDOWNS
                        List<ComboBox> dropdowns= new ArrayList<>();
                        dropdowns.add(section_dropdown);
                        dropdowns.add(sub_section_dropdown);
                        dropdowns.add(category_dropdown);

                        // Loop through dropdowns
                        for(ComboBox dropdown : dropdowns){
                            // If no item is selected, add CSS red_borders class
                            if(dropdown.getSelectionModel().getSelectedItem() == null){
                                if(!dropdown.getStyleClass().contains(CSS_RED_BORDERS)){
                                    dropdown.getStyleClass().add(CSS_RED_BORDERS);
                                }

                                // Increment validation_problems
                                validation_errors++;
                            }else{
                                // Remove CSS red_borders
                                dropdown.getStyleClass().remove(CSS_RED_BORDERS);
                            }
                        }


                        // IF VALIDATION SUCEEDS
                        if(validation_errors == 0){

                            // SETUP PRODUCT RESOURCE
                            ProductResource productResource = new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());

                            // SETUP SIZES AND CORRESPONDING QUANTITIES
                            List<Map<String, String>> options = new ArrayList<>();

                            // Get number of sizes
                            int sizes_index = validation_sizes.size();

                            // Loop through sizes
                            for (int i = 0; i < sizes_index; i++) {

                                // Setup Map of size-quantit pair
                                Map<String, String> temp_map = new HashMap<>();
                                // Set size field
                                temp_map.put("size", validation_sizes.get(i).getText());
                                // Set quantity field
                                temp_map.put("quantity", validation_quantities.get(i).getText());

                                // Add to options list
                                options.add(temp_map);

                            }

                            // CREATE UploadableProduct INSTANCE
                            UploadableProduct to_update = new UploadableProduct(
                                name_textfield.getText(),
                                brand_textfield.getText(),
                                description_textarea.getText(),
                                (String) section_dropdown.getSelectionModel().getSelectedItem(),
                                (String) sub_section_dropdown.getSelectionModel().getSelectedItem(),
                                (String) category_dropdown.getSelectionModel().getSelectedItem(),
                                color_textfield.getText(),
                                price_textfield.getText(),
                                material_textfield.getText(), options,
                                (image_files_map.containsKey("one") ? image_files_map.get("one") : null),
                                (image_files_map.containsKey("two") ? image_files_map.get("two") : null),
                                (image_files_map.containsKey("three") ? image_files_map.get("three") : null)
                            );
                            System.out.println(to_update.getImage_one());

                            // ATTEMPT UPDATE
                            try{

                                // Call inherited method from MainController class to display loading dialog
                                MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                                // ADD PRODUCT TO DATABASE IN SEPARATE THREAD
                                Runnable    update_product_runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Platform.runLater(() -> {
                                            try {
                                                productResource.update(Integer.valueOf(id_textfield.getText()) ,to_update);
                                                // If successful, remove loading dialog
                                                mini_dialog_controller.enableCloseButton();
                                                mini_dialog_controller.setDialog_text_label("Product Updated Successfully");


                                            } catch (Exception e) {
                                                mini_dialog_controller.enableCloseButton();
                                                mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                                            }
                                        });
                                    }
                                };

                                // ATTEMPT UPDATE
                                update_product_runnable.run();


                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            

                        }

                    });

                };

                runnable.run();
            }
        });
        
        actions_hbox.getChildren().add(update_product_button);

        // DELETE PRODUCT BUTTON
        Button delete_product_button= new Button();
        delete_product_button.setAlignment(Pos.CENTER);
        delete_product_button.getStyleClass().addAll("mid-body-font", "dark-color-style", "dark-color-button");
        delete_product_button.setMinHeight(35);
        delete_product_button.setPrefHeight(35);
        delete_product_button.setMaxWidth(10);
        delete_product_button.setGraphic(new ImageView(new Image(MainController.class.getResourceAsStream("img/delete.png"))));
        delete_product_button.setOnAction( new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                
                Runnable runnable= ()->{

                    // PERFORM PRODUCT UPDATE IN SEPERATE THREAD
                    Platform.runLater(()->{

                        // ATTEMPT UPDATE
                        try{

                            // SETUP PRODUCT RESOURCE
                            ProductResource productResource = new ProductResource(SettingsData.getSettings().getBase_url(), SettingsData.getSettings().getApi_path(), SettingsData.getSettings().getApi_token());


                            // Call inherited method from MainController class to display loading dialog
                            MiniDialogController mini_dialog_controller= showLoadingMiniDialog();

                            // ADD PRODUCT TO DATABASE IN SEPARATE THREAD
                            Runnable delete_product_runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Platform.runLater(() -> {
                                        try {
                                            productResource.delete(Integer.valueOf(id_textfield.getText()));
                                            // If successful, remove loading dialog
                                            mini_dialog_controller.enableCloseButton();
                                            mini_dialog_controller.setDialog_text_label("Delete Successful");
                                            // GO BACK TO SEARCH PAGE
                                            handleRedirectToManageProducts();

                                        } catch (Exception e) {
                                            mini_dialog_controller.enableCloseButton();
                                            mini_dialog_controller.setDialog_text_label("An error occurred\n" + e.getMessage());
                                        }
                                    });
                                }
                            };

                            // ATTEMPT UPDATE
                            delete_product_runnable.run();


                        }catch(Exception e){
                            e.printStackTrace();
                        }
                            

                    });

                };

                runnable.run();
            }
            
        } );

        actions_hbox.getChildren().add(delete_product_button);


        textfields_vbox.getChildren().add(actions_hbox);

        // ADD VBOX TO TOPMOST HBOX
        h_box1.getChildren().add(textfields_vbox);

        // ADD h_box1 to top_vbox
        top_vbox.getChildren().add(h_box1);


        // SET content_scrollpane CONTENT TO TOPMOST HBOS
        content_scrollpane.setContent(top_vbox);

    }


}
