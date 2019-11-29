package com.faaizz.dev.online_platform.api_inbound.model.collection;

import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Links;
import com.faaizz.dev.online_platform.api_inbound.model.collection.supplement.Meta;

public class ResourceCollection {

    /**
     * This Class represents a collection of <code>Resource</code>s returned from an API call.
     *  The <b>meta</b> field stores the details of returned products as a <code>Meta</code> object.
     *  The <b>links</b> field stores details of navigation links as a <code>Links</code> object.
     */
    private Meta meta;
    private Links links;

    /*========================================================================================*/
    /*  G   E   T   T   E   R   S   */

    public Meta getMeta() {
        return meta;
    }

    public Links getLinks() {
        return links;
    }

    /*========================================================================================*/
    /*  S   E   T   T   E   R   S   */

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
