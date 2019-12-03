package com.faaizz.dev.online_platform.GUI.model;


public class Settings {

    private String api_token;
    private String base_url;
    private String api_path;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getApi_token() {
        return api_token;
    }

    public String getBase_url() {
        return base_url;
    }

    public String getApi_path() {
        return api_path;
    }
    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public void setApi_path(String api_path) {
        this.api_path = api_path;
    }


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R */
    

    public Settings(String base_url, String api_path, String api_token) {
        this.api_token = api_token;
        this.base_url = base_url;
        this.api_path = api_path;
    }
    
}
