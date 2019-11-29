package com.faaizz.dev.online_platform.api_inbound.model.collection;

import com.faaizz.dev.online_platform.api_inbound.model.Staff;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;

import java.util.List;

public class StaffCollection extends ResourceCollection {

    /**
     * This Class represents a collection of <code>Staff</code>s returned from an API call.
     *  The <b>meta</b> field stores the details of returned products as a <code>Meta</code> object.
     *  The <b>links</b> field stores details of navigation links as a <code>Links</code> object.
     */

    private List<Staff> staffs;


    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */
    public List<Staff> getStaffs(){
        return this.staffs;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public StaffCollection(List<Staff> staffs, Meta meta, Links links) {
        this.staffs = staffs;
        this.setMeta(meta);
        this.setLinks(links);
    }

    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        //PRODUCTS
        tempSB.append("STAFF: " + "\n");

        staffs.forEach( staff->{
            tempSB.append(staff.toString() + "\n");
        });

        //META
        tempSB.append("\n" + "META: " + "\n");
        tempSB.append(this.getMeta().toString());

        //LINKS
        tempSB.append("\n" + "LINKS: " + "\n");
        tempSB.append(this.getLinks().toString());

        return tempSB.toString();

    }
}
