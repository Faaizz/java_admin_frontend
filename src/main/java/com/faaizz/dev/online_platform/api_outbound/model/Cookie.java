package com.faaizz.dev.online_platform.api_outbound.model;

import java.io.Serializable;

public class Cookie implements Serializable {

    static final long serialVersionUID = 42L;

    private String value;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public String getValue() {
        return this.value;
    }


    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public Cookie(String value){

        this.value= value;

    }

}
