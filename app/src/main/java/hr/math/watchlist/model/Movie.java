package hr.math.watchlist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Table(name = "Movie")
public class Movie extends Model {
    @Column(name="title")
    private String title;

    @Column(name="originalTitle")
    @SerializedName("original_title") private String originalTitle;

    @Column(name="voteAverage")
    @SerializedName("vote_average") private double voteAverage;

    @Column(name="posterPath")
    @SerializedName("poster_path") private String posterPath;

    @Column(name="overview")
    private String overview;

    @Column(name = "releaseDate")
    @SerializedName("release_date") private String releaseDate;


    @Column(name="movieID")
    @SerializedName("id") private int movieID;

    @Column(name="backdropPath")
    @SerializedName("backdrop_path") private String backdropPath;

    private List<Genre> genres;

    @Column(name="genre")
    private String genre;

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(int voteAverage) { this.voteAverage = voteAverage; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) {this.originalTitle = originalTitle; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getMovieID(){
        return movieID;
    }

    public void setMovieID(int movieID){
        this.movieID = movieID;
    }
    public Movie()
    {
        super();
    }

    public Movie(String title, String originalTitle, double voteAverage,
                 String posterPath, String overview, String releaseDate,
                 List<Genre> genres, int movieID, String backdropPath)
    {
        super();

        this.title = title;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;

        String temp = "";
        for(Genre gen : genres){
            temp = temp + gen.getName() + " ";
        }
        this.genre = temp;
        this.movieID = movieID;
        this.backdropPath = backdropPath;

    }

    //constructor, when it is in base
    public Movie(String title, String originalTitle, double voteAverage,
                 String posterPath, String overview, String releaseDate,
                 String genre, int movieID, String backdropPath)
    {
        super();

        this.title = title;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.movieID = movieID;
        this.backdropPath = backdropPath;

    }
}
