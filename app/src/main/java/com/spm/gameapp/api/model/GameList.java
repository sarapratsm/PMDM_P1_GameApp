
package com.spm.gameapp.api.model;

import java.util.List;
import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class GameList {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private Object previous;
    @SerializedName("results")
    @Expose
    private List<Game> games;
    @SerializedName("seo_title")
    @Expose
    private String seoTitle;
    @SerializedName("seo_description")
    @Expose
    private String seoDescription;
    @SerializedName("seo_keywords")
    @Expose
    private String seoKeywords;
    @SerializedName("seo_h1")
    @Expose
    private String seoH1;
    @SerializedName("noindex")
    @Expose
    private Boolean noindex;
    @SerializedName("nofollow")
    @Expose
    private Boolean nofollow;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("filters")
    @Expose
    private Filters filters;
    @SerializedName("nofollow_collections")
    @Expose
    private List<String> nofollowCollections;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public List<Game> getResults() {
        return games;
    }

    public void setResults(List<Game> games) {
        this.games = games;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoH1() {
        return seoH1;
    }

    public void setSeoH1(String seoH1) {
        this.seoH1 = seoH1;
    }

    public Boolean getNoindex() {
        return noindex;
    }

    public void setNoindex(Boolean noindex) {
        this.noindex = noindex;
    }

    public Boolean getNofollow() {
        return nofollow;
    }

    public void setNofollow(Boolean nofollow) {
        this.nofollow = nofollow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public List<String> getNofollowCollections() {
        return nofollowCollections;
    }

    public void setNofollowCollections(List<String> nofollowCollections) {
        this.nofollowCollections = nofollowCollections;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GameList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("next");
        sb.append('=');
        sb.append(((this.next == null)?"<null>":this.next));
        sb.append(',');
        sb.append("previous");
        sb.append('=');
        sb.append(((this.previous == null)?"<null>":this.previous));
        sb.append(',');
        sb.append("results");
        sb.append('=');
        sb.append(((this.games == null)?"<null>":this.games));
        sb.append(',');
        sb.append("seoTitle");
        sb.append('=');
        sb.append(((this.seoTitle == null)?"<null>":this.seoTitle));
        sb.append(',');
        sb.append("seoDescription");
        sb.append('=');
        sb.append(((this.seoDescription == null)?"<null>":this.seoDescription));
        sb.append(',');
        sb.append("seoKeywords");
        sb.append('=');
        sb.append(((this.seoKeywords == null)?"<null>":this.seoKeywords));
        sb.append(',');
        sb.append("seoH1");
        sb.append('=');
        sb.append(((this.seoH1 == null)?"<null>":this.seoH1));
        sb.append(',');
        sb.append("noindex");
        sb.append('=');
        sb.append(((this.noindex == null)?"<null>":this.noindex));
        sb.append(',');
        sb.append("nofollow");
        sb.append('=');
        sb.append(((this.nofollow == null)?"<null>":this.nofollow));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("filters");
        sb.append('=');
        sb.append(((this.filters == null)?"<null>":this.filters));
        sb.append(',');
        sb.append("nofollowCollections");
        sb.append('=');
        sb.append(((this.nofollowCollections == null)?"<null>":this.nofollowCollections));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
