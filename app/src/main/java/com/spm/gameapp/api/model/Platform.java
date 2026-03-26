
package com.spm.gameapp.api.model;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Platform {

    @SerializedName("platform")
    @Expose
    private Platform__1 platform;
    @SerializedName("released_at")
    @Expose
    private String releasedAt;
    @SerializedName("requirements_en")
    @Expose
    private RequirementsEn requirementsEn;
    @SerializedName("requirements_ru")
    @Expose
    private RequirementsRu requirementsRu;

    public Platform__1 getPlatform() {
        return platform;
    }

    public void setPlatform(Platform__1 platform) {
        this.platform = platform;
    }

    public String getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(String releasedAt) {
        this.releasedAt = releasedAt;
    }

    public RequirementsEn getRequirementsEn() {
        return requirementsEn;
    }

    public void setRequirementsEn(RequirementsEn requirementsEn) {
        this.requirementsEn = requirementsEn;
    }

    public RequirementsRu getRequirementsRu() {
        return requirementsRu;
    }

    public void setRequirementsRu(RequirementsRu requirementsRu) {
        this.requirementsRu = requirementsRu;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Platform.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("platform");
        sb.append('=');
        sb.append(((this.platform == null)?"<null>":this.platform));
        sb.append(',');
        sb.append("releasedAt");
        sb.append('=');
        sb.append(((this.releasedAt == null)?"<null>":this.releasedAt));
        sb.append(',');
        sb.append("requirementsEn");
        sb.append('=');
        sb.append(((this.requirementsEn == null)?"<null>":this.requirementsEn));
        sb.append(',');
        sb.append("requirementsRu");
        sb.append('=');
        sb.append(((this.requirementsRu == null)?"<null>":this.requirementsRu));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
