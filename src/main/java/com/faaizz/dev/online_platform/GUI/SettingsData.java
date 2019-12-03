package com.faaizz.dev.online_platform.GUI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.faaizz.dev.online_platform.GUI.model.Settings;
import com.google.gson.Gson;

/**
 * This class holds current application settings
 */
public class SettingsData {

    private static Settings settings;

    public static void loadSettings() throws IOException {


        // STRING BUILDER
        StringBuilder jsonSB= new StringBuilder();

        try(BufferedReader file_reader= new BufferedReader(new InputStreamReader(new FileInputStream("settings/main.json")))){
            // LOAD SETTINGS FILE

            // TEMPORARY STRING
            String line;

            // WHILE THERE'S STILL CONTENT IN THE FILE
            while( (line= file_reader.readLine()) != null ) {
                jsonSB.append(line);
            }

        }catch(Exception e){
            throw e;
        }

        // JSON STRING
        String json_string= jsonSB.toString();

        // LOAD SETTINGS FROM LOCAL JSON FILE (settings/main.json)
        Gson gson= new Gson();

        // CREATE Settings OBJECT
        settings= gson.fromJson(json_string, Settings.class);

    }

    public static Settings getSettings(){
        return settings;
    }

}
