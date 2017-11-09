package hr.math.watchlist.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by domagoj on 21.01.16..
 */

public class Search {
    private String title;
    private Integer id;
    @SerializedName("poster_path") private String posterPath;
    @SerializedName("release_date") private String releaseDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getReleaseDate() { return releaseDate; }
    public void  setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
