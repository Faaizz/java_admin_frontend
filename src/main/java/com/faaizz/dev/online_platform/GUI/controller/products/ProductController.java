package com.faaizz.dev.online_platform.GUI.controller.products;

import com.faaizz.dev.online_platform.GUI.controller.MainController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController extends MainController {

    protected static Map<String, Map<String, List<String>>> sections;

    @FXML
    protected BorderPane root_border_pane;

    @FXML
    protected ComboBox section_dropdown;
    @FXML
    protected ComboBox sub_section_dropdown;
    @FXML
    protected ComboBox category_dropdown;


    @FXML
    protected TextField name_textfield;
    @FXML
    protected TextField brand_textfield;
    @FXML
    protected TextField color_textfield;


    public void initialize() throws Exception {

        /*
         * =============================================================================
         * ===========
         */
        /* S E C T I O N S S U B S E C T I O N S C A T E G O R I E S */

        // ORGANISE SECTIONS, SUB SECTIONS, AND CATEGORIES FOR THEIR RESPECTIVE
        // COMBOBOXES
        List<String> male_clothing_options = FXCollections.observableArrayList("T-Shirts", "Shorts", "Shirts",
                "Trousers", "Sweatshirts & Hoodies", "Sweaters, Jackets, & Coats", "Underwear");
        List<String> female_clothing_options = FXCollections.observableArrayList("Tops", "Dresses", "Skirts",
                "Leggings & Vests", "Shorts", "Shirts", "Trousers", "Sweatshirts & Hoodies",
                "Sweaters, Jackets, & Coats", "Underwear & Lingerie");

        Map<String, List<String>> clothing_section = new HashMap<>();
        clothing_section.put("male", male_clothing_options);
        clothing_section.put("female", female_clothing_options);

        List<String> male_shoes_options = FXCollections.observableArrayList("Oxford", "Loafers", "Sneakers", "Boots",
                "Sandals & Slippers");
        List<String> female_shoes_options = FXCollections.observableArrayList("Flats", "Heels & Pumps",
                "Sandals & Slippers", "Sneakers", "Boots");

        Map<String, List<String>> shoes_section = new HashMap<>();
        shoes_section.put("male", male_shoes_options);
        shoes_section.put("female", female_shoes_options);

        sections = new HashMap<>();
        sections.put("clothing", clothing_section);
        sections.put("shoes", shoes_section);
        sections.put("accessories", null);
        sections.put("bags & watches", null);

        List<String> sections_list = FXCollections.observableArrayList("clothing", "shoes", "accessories",
                "bags & watches");

        // Set categories as ComboBox options
        section_dropdown.getItems().clear();
        section_dropdown.getItems().addAll(sections_list);


    }

    /*
     * =============================================================================
     * ===========
     */
    /* A C T I O N H A N D L E R S */

    @FXML
    public void handleSectionChange() {

        // Get selected section string
        String selected_section = (String) section_dropdown.getSelectionModel().getSelectedItem();

        // Check if selected section has an underlying Map<String, List<String>>
        if (sections.get(selected_section) != null) {
            // If there's an underlying list of sub sections, set it as underlying map keys
            // for the sub_section_dropdown
            sub_section_dropdown.getItems().setAll(sections.get(selected_section).keySet());
        }
        // Otherwise, set section name as sub_section
        else {
            sub_section_dropdown.getItems().setAll(selected_section);
        }

        // Select first item by default
        sub_section_dropdown.getSelectionModel().selectFirst();


    }

    @FXML
    public void handleSub_sectionChange() {

        // Get selected section string
        String selected_section = (String) section_dropdown.getSelectionModel().getSelectedItem();

        // Get selected sub section
        String selected_sub_section = (String) sub_section_dropdown.getSelectionModel().getSelectedItem();

        // Check if selected sub_section has an underlying List of categories
        if ((sections.get(selected_section) != null)
                && (sections.get(selected_section).get(selected_sub_section) != null)) {
            // Set category_dropdown items as the underlying list
            category_dropdown.getItems().setAll(sections.get(selected_section).get(selected_sub_section));
        }
        // Otherwise, set category as section name
        else {
            category_dropdown.getItems().setAll(selected_section);
        }

        // Select first category by default
        category_dropdown.getSelectionModel().selectFirst();

    }

}
