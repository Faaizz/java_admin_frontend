package com.faaizz.dev.online_platform.api_inbound.model.collection;

import com.faaizz.dev.online_platform.api_inbound.model.Product;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;

import java.util.List;

public class ProductCollection extends ResourceCollection {

    /**
     * This Class represents a collection of <code>Product</code>s returned from an API call.
     *  The <b>meta</b> field stores the details of returned products as a <code>Meta</code> object.
     *  The <b>links</b> field stores details of navigation links as a <code>Links</code> object.
     */

    private List<Product> products;

    /*========================================================================================*/
    /*  C   O   N   S   T   R   U   C   T   O   R   S   */

    public ProductCollection(List<Product> products, Meta meta, Links links) {
        this.products = products;
        this.setMeta(meta);
        this.setLinks(links);
    }

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public List<Product> getProducts() {
        return products;
    }


    /*========================================================================================*/

    @Override
    public String toString(){

        StringBuilder tempSB= new StringBuilder();

        //PRODUCTS
        tempSB.append("PRODUCTS: " + "\n");

        products.forEach( product->{
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
