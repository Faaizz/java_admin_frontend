package com.faaizz.dev.online_platform.api_inbound.model.collection;

import java.util.List;

import com.faaizz.dev.online_platform.api_inbound.model.Trend;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;

public class TrendCollection extends ResourceCollection {

    /**
     * This Class represents a collection of <code>Trends</code>s returned from an API call.
     *  The <b>meta</b> field stores the details of returned trends as a <code>Meta</code> object.
     *  The <b>links</b> field stores details of navigation links as a <code>Links</code> object.
     */

    private List<Trend> trends; 

    public TrendCollection(List<Trend> trends, Meta meta, Links links) {
        this.trends = trends;
        this.setMeta(meta);
        this.setLinks(links);
    }

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public List<Trend> getTrends() {
        return trends;
    }


    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        //TRENDS
        tempSB.append("TRENDS: " + "\n");

        trends.forEach( product->{
            tempSB.append(product.toString() + "\n\n");
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