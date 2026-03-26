
package com.spm.gameapp.api.model;

import java.util.List;
import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Year {

    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("filter")
    @Expose
    private String filter;
    @SerializedName("decade")
    @Expose
    private Integer decade;
    @SerializedName("years")
    @Expose
    private List<Year__1> years;
    @SerializedName("nofollow")
    @Expose
    private Boolean nofollow;
    @SerializedName("count")
    @Expose
    private Integer count;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getDecade() {
        return decade;
    }

    public void setDecade(Integer decade) {
        this.decade = decade;
    }

    public List<Year__1> getYears() {
        return years;
    }

    public void setYears(List<Year__1> years) {
        this.years = years;
    }

    public Boolean getNofollow() {
        return nofollow;
    }

    public void setNofollow(Boolean nofollow) {
        this.nofollow = nofollow;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Year.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("from");
        sb.append('=');
        sb.append(((this.from == null)?"<null>":this.from));
        sb.append(',');
        sb.append("to");
        sb.append('=');
        sb.append(((this.to == null)?"<null>":this.to));
        sb.append(',');
        sb.append("filter");
        sb.append('=');
        sb.append(((this.filter == null)?"<null>":this.filter));
        sb.append(',');
        sb.append("decade");
        sb.append('=');
        sb.append(((this.decade == null)?"<null>":this.decade));
        sb.append(',');
        sb.append("years");
        sb.append('=');
        sb.append(((this.years == null)?"<null>":this.years));
        sb.append(',');
        sb.append("nofollow");
        sb.append('=');
        sb.append(((this.nofollow == null)?"<null>":this.nofollow));
        sb.append(',');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
