
package com.spm.gameapp.model;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Store {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store")
    @Expose
    private Store__1 store;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Store__1 getStore() {
        return store;
    }

    public void setStore(Store__1 store) {
        this.store = store;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Store.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("store");
        sb.append('=');
        sb.append(((this.store == null)?"<null>":this.store));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
