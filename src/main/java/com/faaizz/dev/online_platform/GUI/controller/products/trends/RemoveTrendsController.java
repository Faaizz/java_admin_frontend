package com.faaizz.dev.online_platform.GUI.controller.products.trends;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faaizz.dev.online_platform.GUI.SettingsData;
import com.faaizz.dev.online_platform.GUI.controller.dialogs.MiniDialogController;
import com.faaizz.dev.online_platform.api_inbound.model.collection.TrendCollection;
import com.faaizz.dev.online_platform.api_inbound.platform.APIParser;
import com.faaizz.dev.online_platform.api_outbound.platform.TrendResource;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RemoveTrendsController extends ManageTrendsController {

    @FXML
    protected TextField brand_textfield;

    public void initialize() throws Exception {
        // INITIALIZE SECTIONS, SUB SECTIONS AND CATEGORIES
        super.initialize();
    }

    @FXML
    public void handleDeleteTrends() throws IOException {

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

                            mini_dialog_controller.enableCloseButton();

                            // IF NO TREND IS FOUND
                            if(matched_trends.getTrends().size() <= 0){
                                mini_dialog_controller.setDialog_text_label("SORRY. COULD NOT FIND ANY TREND WITH THE SPECIFIED SEARCH PARAMETERS.");
                            }
                            // OTHERWISE
                            else{
                                
                                //DELETE MATCHED TRENDS
                                int no_of_pages= matched_trends.getMeta().getLast_page();

                                // LIST TO STORE IDS OF MATCHED TRENDS
                                List<Integer> ids= new ArrayList<>();

                                // IF RESULT IS A SINGLE PAGE, GET TREND ID OF MATCHED TRENDS
                                if(no_of_pages == 1){

                                    matched_trends.getTrends().forEach( trend-> {
                                        ids.add(trend.getId());
                                    });

                                }
                                // OTHERWISE
                                else{

                                    // CHECK FIRST PAGE
                                    matched_trends.getTrends().forEach( trend-> {
                                        ids.add(trend.getId());
                                    });

                                    // THEN LOOP THROUGH EVERY OTHER PAGE
                                    for(int page_number= 2; page_number<= no_of_pages; page_number++){

                                        // SET RESULT PAGE
                                        trendResource.setPage_number(page_number);

                                        // GET NEW TRENDS
                                        matched_trends_string= trendResource.search(post_data);

                                        // PARSE TRENDS
                                        matched_trends= APIParser.getInstance().parseMultiTrendResponse(matched_trends_string);

                                        // ADD IDS TO LIST
                                        matched_trends.getTrends().forEach( trend-> {
                                            ids.add(trend.getId());
                                        });

                                    }


                                }


                                // PERFORM MASS DELETE
                                int[] ids_ints= new int[ids.size()];
                                // GET IDS INTO AN Int ARRAY
                                for(int i=0; i<ids.size(); i++){
                                    ids_ints[i]= ids.get(i);
                                }

                                // RESET PAGE TO 1
                                trendResource.setPage_number(1);
                                // PERFORM MASS DELETE
                                trendResource.massDelete(ids_ints);
                                
                                mini_dialog_controller.setDialog_text_label(ids_ints.length + " trends deleted successfully.");
                            }


                        }
                        catch (Exception e) {
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