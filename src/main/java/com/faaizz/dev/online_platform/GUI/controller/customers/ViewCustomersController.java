package com.faaizz.dev.online_platform.GUI.controller.customers;

import javafx.application.Platform;
import java.util.HashMap;
import java.util.Map;


public class ViewCustomersController extends GenericCustomersController{

    public void initialize() throws Exception {

        super.initialize();

        Map<String, String> post_data = new HashMap<>();

        Platform.runLater( new Runnable(){
        
            @Override
            public void run() {
                try{
                    loadCustomers(post_data, 1);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } );


    }

}


