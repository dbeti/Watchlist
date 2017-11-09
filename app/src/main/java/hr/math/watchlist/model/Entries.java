package hr.math.watchlist.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crompir on 31.01.16..
 */
@Table(name="entries")
public class Entries extends Model {
    @Column(name="watchlist", onDelete = Column.ForeignKeyAction.CASCADE)
    private Watchlist watchlist;
    @Column(name="movie")
    private Movie movie;
    @Column(name="vote")
    private int vote;
    @Column(name="comment")
    private String comment;

    public Entries(){
        super();
    }

    public Movie getMovie(){
        return movie;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    public Watchlist getWatchlist(){
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist)
    {
        this.watchlist = watchlist;
    }

    public int getVote(){
        return vote;
    }

    public void setVote(int vote){
        this.vote = vote;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Entries(Watchlist watchlist, Movie movie)
    {
        super();

        this.watchlist = watchlist;
        this.movie = movie;
        this.vote = 0;
        this.comment = "";
    }

    public List<Movie> movies(long watchlistID){
        List<Entries> lista = new ArrayList<Entries>();
        List<Movie> movies = new ArrayList<Movie>();
        if(new Select().from(Entries.class).where("watchlist = ?", watchlistID).exists())
        {
            lista = new Select().from(Entries.class).where("watchlist = ?", watchlistID).execute();
            for(Entries ent : lista){
                movies.add(ent.getMovie());
            }
        }
        return movies;
    }
}