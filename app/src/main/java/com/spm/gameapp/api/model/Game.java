
package com.spm.gameapp.api.model;

import java.util.List;
import javax.annotation.processing.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Game {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("tba")
    @Expose
    private Boolean tba;
    @SerializedName("background_image")
    @Expose
    private String backgroundImage;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("rating_top")
    @Expose
    private Integer ratingTop;
    @SerializedName("ratings")
    @Expose
    private List<Rating> ratings;
    @SerializedName("ratings_count")
    @Expose
    private Integer ratingsCount;
    @SerializedName("reviews_text_count")
    @Expose
    private Integer reviewsTextCount;
    @SerializedName("added")
    @Expose
    private Integer added;
    @SerializedName("added_by_status")
    @Expose
    private AddedByStatus addedByStatus;
    @SerializedName("metacritic")
    @Expose
    private Integer metacritic;
    @SerializedName("playtime")
    @Expose
    private Integer playtime;
    @SerializedName("suggestions_count")
    @Expose
    private Integer suggestionsCount;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("user_game")
    @Expose
    private Object userGame;
    @SerializedName("reviews_count")
    @Expose
    private Integer reviewsCount;
    @SerializedName("saturated_color")
    @Expose
    private String saturatedColor;
    @SerializedName("dominant_color")
    @Expose
    private String dominantColor;
    @SerializedName("platforms")
    @Expose
    private List<Platform> platforms;
    @SerializedName("parent_platforms")
    @Expose
    private List<ParentPlatform> parentPlatforms;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;
    @SerializedName("stores")
    @Expose
    private List<Store> stores;
    @SerializedName("clip")
    @Expose
    private Object clip;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags;
    @SerializedName("esrb_rating")
    @Expose
    private EsrbRating esrbRating;
    @SerializedName("short_screenshots")
    @Expose
    private List<ShortScreenshot> shortScreenshots;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("description_raw")
    @Expose
    private String descriptionRaw;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionRaw() {
        return descriptionRaw;
    }

    public void setDescriptionRaw(String descriptionRaw) {
        this.descriptionRaw = descriptionRaw;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public Boolean getTba() {
        return tba;
    }

    public void setTba(Boolean tba) {
        this.tba = tba;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingTop() {
        return ratingTop;
    }

    public void setRatingTop(Integer ratingTop) {
        this.ratingTop = ratingTop;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public Integer getReviewsTextCount() {
        return reviewsTextCount;
    }

    public void setReviewsTextCount(Integer reviewsTextCount) {
        this.reviewsTextCount = reviewsTextCount;
    }

    public Integer getAdded() {
        return added;
    }

    public void setAdded(Integer added) {
        this.added = added;
    }

    public AddedByStatus getAddedByStatus() {
        return addedByStatus;
    }

    public void setAddedByStatus(AddedByStatus addedByStatus) {
        this.addedByStatus = addedByStatus;
    }

    public Integer getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(Integer metacritic) {
        this.metacritic = metacritic;
    }

    public Integer getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }

    public Integer getSuggestionsCount() {
        return suggestionsCount;
    }

    public void setSuggestionsCount(Integer suggestionsCount) {
        this.suggestionsCount = suggestionsCount;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Object getUserGame() {
        return userGame;
    }

    public void setUserGame(Object userGame) {
        this.userGame = userGame;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getSaturatedColor() {
        return saturatedColor;
    }

    public void setSaturatedColor(String saturatedColor) {
        this.saturatedColor = saturatedColor;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<ParentPlatform> getParentPlatforms() {
        return parentPlatforms;
    }

    public void setParentPlatforms(List<ParentPlatform> parentPlatforms) {
        this.parentPlatforms = parentPlatforms;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public Object getClip() {
        return clip;
    }

    public void setClip(Object clip) {
        this.clip = clip;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public EsrbRating getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(EsrbRating esrbRating) {
        this.esrbRating = esrbRating;
    }

    public List<ShortScreenshot> getShortScreenshots() {
        return shortScreenshots;
    }

    public void setShortScreenshots(List<ShortScreenshot> shortScreenshots) {
        this.shortScreenshots = shortScreenshots;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Game.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("slug");
        sb.append('=');
        sb.append(((this.slug == null)?"<null>":this.slug));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("released");
        sb.append('=');
        sb.append(((this.released == null)?"<null>":this.released));
        sb.append(',');
        sb.append("tba");
        sb.append('=');
        sb.append(((this.tba == null)?"<null>":this.tba));
        sb.append(',');
        sb.append("backgroundImage");
        sb.append('=');
        sb.append(((this.backgroundImage == null)?"<null>":this.backgroundImage));
        sb.append(',');
        sb.append("rating");
        sb.append('=');
        sb.append(((this.rating == null)?"<null>":this.rating));
        sb.append(',');
        sb.append("ratingTop");
        sb.append('=');
        sb.append(((this.ratingTop == null)?"<null>":this.ratingTop));
        sb.append(',');
        sb.append("ratings");
        sb.append('=');
        sb.append(((this.ratings == null)?"<null>":this.ratings));
        sb.append(',');
        sb.append("ratingsCount");
        sb.append('=');
        sb.append(((this.ratingsCount == null)?"<null>":this.ratingsCount));
        sb.append(',');
        sb.append("reviewsTextCount");
        sb.append('=');
        sb.append(((this.reviewsTextCount == null)?"<null>":this.reviewsTextCount));
        sb.append(',');
        sb.append("added");
        sb.append('=');
        sb.append(((this.added == null)?"<null>":this.added));
        sb.append(',');
        sb.append("addedByStatus");
        sb.append('=');
        sb.append(((this.addedByStatus == null)?"<null>":this.addedByStatus));
        sb.append(',');
        sb.append("metacritic");
        sb.append('=');
        sb.append(((this.metacritic == null)?"<null>":this.metacritic));
        sb.append(',');
        sb.append("playtime");
        sb.append('=');
        sb.append(((this.playtime == null)?"<null>":this.playtime));
        sb.append(',');
        sb.append("suggestionsCount");
        sb.append('=');
        sb.append(((this.suggestionsCount == null)?"<null>":this.suggestionsCount));
        sb.append(',');
        sb.append("updated");
        sb.append('=');
        sb.append(((this.updated == null)?"<null>":this.updated));
        sb.append(',');
        sb.append("userGame");
        sb.append('=');
        sb.append(((this.userGame == null)?"<null>":this.userGame));
        sb.append(',');
        sb.append("reviewsCount");
        sb.append('=');
        sb.append(((this.reviewsCount == null)?"<null>":this.reviewsCount));
        sb.append(',');
        sb.append("saturatedColor");
        sb.append('=');
        sb.append(((this.saturatedColor == null)?"<null>":this.saturatedColor));
        sb.append(',');
        sb.append("dominantColor");
        sb.append('=');
        sb.append(((this.dominantColor == null)?"<null>":this.dominantColor));
        sb.append(',');
        sb.append("platforms");
        sb.append('=');
        sb.append(((this.platforms == null)?"<null>":this.platforms));
        sb.append(',');
        sb.append("parentPlatforms");
        sb.append('=');
        sb.append(((this.parentPlatforms == null)?"<null>":this.parentPlatforms));
        sb.append(',');
        sb.append("genres");
        sb.append('=');
        sb.append(((this.genres == null)?"<null>":this.genres));
        sb.append(',');
        sb.append("stores");
        sb.append('=');
        sb.append(((this.stores == null)?"<null>":this.stores));
        sb.append(',');
        sb.append("clip");
        sb.append('=');
        sb.append(((this.clip == null)?"<null>":this.clip));
        sb.append(',');
        sb.append("tags");
        sb.append('=');
        sb.append(((this.tags == null)?"<null>":this.tags));
        sb.append(',');
        sb.append("esrbRating");
        sb.append('=');
        sb.append(((this.esrbRating == null)?"<null>":this.esrbRating));
        sb.append(',');
        sb.append("shortScreenshots");
        sb.append('=');
        sb.append(((this.shortScreenshots == null)?"<null>":this.shortScreenshots));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }


}
