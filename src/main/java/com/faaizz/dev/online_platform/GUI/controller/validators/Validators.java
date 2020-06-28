package com.faaizz.dev.online_platform.GUI.controller.validators;

import java.io.File;
import java.util.List;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Validators {

    protected static final String CSS_RED_BORDERS= "red_borders";
    
    public static void validateTextFields(List<TextField> textfields, int[] validation_problems){

        // Loop through all textfields
        textfields.forEach( textfield->{

            if(textfield != null){

                // If empty, add CSS CSS_RED_BORDERS class
                if(textfield.getText().isEmpty()){
                    if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                        textfield.getStyleClass().add(CSS_RED_BORDERS);
                    }
                    // Increment validation_problems
                    validation_problems[0]++;
                }

                // If it has text, remove red_borders class
                else{
                    textfield.getStyleClass().remove(CSS_RED_BORDERS);
                }
            }
        });

    }

    public static void validateTextAreas(List<TextArea> textareas, int[] validation_problems){

        // Loop through all textareas
        textareas.forEach( textarea->{

            if(textarea != null){

                // VALIDATE TEXTAREA
                if(textarea.getText().isEmpty()){
                    // Add CSS red_borders class
                    if(!textarea.getStyleClass().contains(CSS_RED_BORDERS)){
                        textarea.getStyleClass().add(CSS_RED_BORDERS);
                    }

                    // Increment validation_problems
                    validation_problems[0]++;
                }else{
                    // Remove CSS red_borders class
                    textarea.getStyleClass().remove(CSS_RED_BORDERS);
                }   
            }

        });
        

    }

    public static void validateDropdowns(List<ComboBox> dropdowns, int[] validation_problems){

        // Loop through dropdowns
        dropdowns.forEach( dropdown ->{

            if(dropdown != null){
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
            }
        } );

    }

    public static void validateImageSelect(Map<String, File> image_files_map, Button image_filechooser, int[] validation_problems, String img_text){

        // CHECK IF IMAGE IS SET
        if(!image_files_map.containsKey(img_text)){
            if(!image_filechooser.getStyleClass().contains(CSS_RED_BORDERS)){
                image_filechooser.getStyleClass().add(CSS_RED_BORDERS);
            }
            // Increment validation_problems
            validation_problems[0]++;
        }else{
            // Otherwise remove CSS red borders
            image_filechooser.getStyleClass().remove(CSS_RED_BORDERS);
        }
        
    }

    public static void validatePrices(List<TextField> price_textfields, int[] validation_problems){

        // Loop through
        price_textfields.forEach( price_textfield-> {

            if(price_textfield != null){

                // If empty, add CSS CSS_RED_BORDERS class
                if(price_textfield.getText().isEmpty()){
                    if(!price_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                        price_textfield.getStyleClass().add(CSS_RED_BORDERS);
                    }
                    // Increment validation_problems
                    validation_problems[0]++;
                }
                else{
                    // Validate Price
                    try{
                        // Try to parse text content to double
                        Double.valueOf(price_textfield.getText());

                        // Otherwise remove CSS red borders
                        price_textfield.getStyleClass().remove(CSS_RED_BORDERS);

                    }catch(NumberFormatException e){
                        // Set red_borders css class
                        if(!price_textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                            price_textfield.getStyleClass().add(CSS_RED_BORDERS);
                        }
                        // Increment validation_problems
                        validation_problems[0]++;
                    }
                }
            }
        });        
    }

    public static void validateNumbers(List<TextField> textfields, int[] validation_problems){

        // Loop through
        textfields.forEach( textfield-> {

            if(textfield != null){

                // If empty, add CSS CSS_RED_BORDERS class
                if(textfield.getText().isEmpty()){
                    if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                        textfield.getStyleClass().add(CSS_RED_BORDERS);
                    }
                    // Increment validation_problems
                    validation_problems[0]++;
                }
                else{
                    // Validate Price
                    try{
                        // Try to parse text content to double
                        Integer.valueOf(textfield.getText());

                        // Otherwise remove CSS red borders
                        textfield.getStyleClass().remove(CSS_RED_BORDERS);
                    }catch(NumberFormatException e){
                        // Set red_borders css class
                        if(!textfield.getStyleClass().contains(CSS_RED_BORDERS)){
                            textfield.getStyleClass().add(CSS_RED_BORDERS);
                        }
                        // Increment validation_problems
                        validation_problems[0]++;
                    }
                }
            }
        });        
    }

}