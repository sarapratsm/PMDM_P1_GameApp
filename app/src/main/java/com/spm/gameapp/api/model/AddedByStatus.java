
package com.spm.gameapp.api.model;

import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AddedByStatus {

    @SerializedName("yet")
    @Expose
    private Integer yet;
    @SerializedName("owned")
    @Expose
    private Integer owned;
    @SerializedName("beaten")
    @Expose
    private Integer beaten;
    @SerializedName("toplay")
    @Expose
    private Integer toplay;
    @SerializedName("dropped")
    @Expose
    private Integer dropped;
    @SerializedName("playing")
    @Expose
    private Integer playing;

    public Integer getYet() {
        return yet;
    }

    public void setYet(Integer yet) {
        this.yet = yet;
    }

    public Integer getOwned() {
        return owned;
    }

    public void setOwned(Integer owned) {
        this.owned = owned;
    }

    public Integer getBeaten() {
        return beaten;
    }

    public void setBeaten(Integer beaten) {
        this.beaten = beaten;
    }

    public Integer getToplay() {
        return toplay;
    }

    public void setToplay(Integer toplay) {
        this.toplay = toplay;
    }

    public Integer getDropped() {
        return dropped;
    }

    public void setDropped(Integer dropped) {
        this.dropped = dropped;
    }

    public Integer getPlaying() {
        return playing;
    }

    public void setPlaying(Integer playing) {
        this.playing = playing;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AddedByStatus.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("yet");
        sb.append('=');
        sb.append(((this.yet == null)?"<null>":this.yet));
        sb.append(',');
        sb.append("owned");
        sb.append('=');
        sb.append(((this.owned == null)?"<null>":this.owned));
        sb.append(',');
        sb.append("beaten");
        sb.append('=');
        sb.append(((this.beaten == null)?"<null>":this.beaten));
        sb.append(',');
        sb.append("toplay");
        sb.append('=');
        sb.append(((this.toplay == null)?"<null>":this.toplay));
        sb.append(',');
        sb.append("dropped");
        sb.append('=');
        sb.append(((this.dropped == null)?"<null>":this.dropped));
        sb.append(',');
        sb.append("playing");
        sb.append('=');
        sb.append(((this.playing == null)?"<null>":this.playing));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
