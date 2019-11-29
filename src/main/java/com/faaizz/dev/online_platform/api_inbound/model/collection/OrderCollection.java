package com.faaizz.dev.online_platform.api_inbound.model.collection;

import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.Order;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;

import java.util.List;

public class OrderCollection extends ResourceCollection {

    /**
     * This Class represents a collection of <code>Staff</code>s returned from an API call.
     *  The <b>meta</b> field stores the details of returned products as a <code>Meta</code> object.
     *  The <b>links</b> field stores details of navigation links as a <code>Links</code> object.
     */

    private List<Order> orders;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public List<Order> getOrders(){
        return this.orders;
    }

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public OrderCollection(List<Order> orders, Meta meta, Links links) {
        this.orders = orders;
        this.setMeta(meta);
        this.setLinks(links);
    }


    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        //PRODUCTS
        tempSB.append("\n\n\nORDERS: " + "\n");

        orders.forEach( order->{
            tempSB.append(order.toString() + "\n\n");
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
