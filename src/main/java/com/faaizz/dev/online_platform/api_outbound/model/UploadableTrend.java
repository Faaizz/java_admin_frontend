package com.faaizz.dev.online_platform.api_outbound.model;

import java.io.File;

public class UploadableTrend {

    private String name;
    private String description;
    private String gender;
    private File image_one;


    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return gender;
    }

    public File getImage_one() {
        return image_one;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public UploadableTrend(String name, String description, String gender, File image_one) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.image_one = image_one;
    }


    
    

}