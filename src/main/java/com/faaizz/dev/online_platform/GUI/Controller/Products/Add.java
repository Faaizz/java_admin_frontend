package com.faaizz.dev.online_platform.GUI.Controller.Products;

import com.faaizz.dev.online_platform.GUI.Controller.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add extends Main {

    @FXML
    private ComboBox sections_dropdown;

    private static Map<String, Map<String, List<String>>> sections;

    public void initialize(){

        //ORGANISE SECTIONS, SUB SECTIONS, AND CATEGORIES FOR THEIR RESPECTIVE COMBOBOXES
        List<String> male_clothing_options= FXCollections.observableArrayList("T-Shirts", "Shorts", "Shirts",
                "Trousers", "Sweatshirts & Hoodies", "Sweaters, Jackets, & Coats", "Underwear"
        );
        List<String> female_clothing_options= FXCollections.observableArrayList("Tops", "Dresses", "Skirts",
                "Leggings & Vests", "Shorts", "Shirts", "Trousers", "Sweatshirts & Hoodies", "Sweaters, Jackets, & Coats",
                "Underwear & Lingerie"
        );

        Map<String, List<String>> clothing_section= new HashMap<>();
        clothing_section.put("male", male_clothing_options);
        clothing_section.put("female", female_clothing_options);


        List<String> male_shoes_options= FXCollections.observableArrayList("Oxford", "Loafers", "Sneakers",
                "Boots", "Sandals & Slippers"
        );
        List<String> female_shoes_options= FXCollections.observableArrayList("Flats", "Heels & Pumps",
                "Sandals & Slippers", "Sneakers", "Boots"
        );

        Map<String, List<String>> shoes_section= new HashMap<>();
        shoes_section.put("male", male_shoes_options);
        shoes_section.put("female", female_shoes_options);

        sections= new HashMap<>();
        sections.put("clothing", clothing_section);
        sections.put("shoes", shoes_section);
        sections.put("accessories", null);
        sections.put("bags & watches", null);

        List<String> sections_list= FXCollections.observableArrayList("clothing", "shoes", "accessories", "bags & watches");

        // Set categories as ComboBox options
        sections_dropdown.getItems().clear();
        sections_dropdown.getItems().addAll(sections_list);

        // Select the first item by default
        sections_dropdown.getSelectionModel().select(0);

    }

}
