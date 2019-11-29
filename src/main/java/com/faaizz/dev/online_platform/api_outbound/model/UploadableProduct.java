package com.faaizz.dev.online_platform.api_outbound.model;

import com.faaizz.dev.online_platform.api_inbound.model.supplement.ProductOption;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * This class implements an uploadable <b>Product</b>
 */
public class UploadableProduct {

    private String name;
    private String brand;
    private String description;
    private String section;
    private String sub_section;
    private String category;
    private String color;
    private String price;
    private String material;
    private List<Map<String, String>> options;
    private File image_one;
    private File image_two;
    private File image_three;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getSection() {
        return section;
    }

    public String getSub_section() {
        return sub_section;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getMaterial() {
        return material;
    }

    public String getOptions(){

        Gson gson= new Gson();

        Type typeToken= new TypeToken<List<Map<String, String>>>(){}.getType();

        return gson.toJson(this.options, typeToken);

    }

    public File getImage_one() {
        return image_one;
    }

    public File getImage_two() {
        return image_two;
    }

    public File getImage_three() {
        return image_three;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public UploadableProduct(
            String name, String brand, String description, String section, String sub_section,
            String category, String color, String price, String material, List<Map<String, String>> options,
            File image_one, File image_two, File image_three
    ) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.section = section;
        this.sub_section = sub_section;
        this.category = category;
        this.color = color;
        this.price = price;
        this.material = material;
        this.options = options;
        this.image_one = image_one;
        this.image_two = image_two;
        this.image_three = image_three;
    }
}
