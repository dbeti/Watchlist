package hr.math.watchlist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

import hr.math.watchlist.AppPreferences;
import hr.math.watchlist.R;
import hr.math.watchlist.model.Entries;
import hr.math.watchlist.model.Movie;

/**
 * Created by domagoj on 01.02.16..
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private String url="http://image.tmdb.org/t/p/w300";
    private Context context;
    private List<Movie> moviesList;
    private Long watchlistID;

    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        this.context = context;
        this.moviesList = objects;
        watchlistID = new AppPreferences(context).getWatchlistID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_star, parent, false);

        Movie movie = moviesList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.list_item_name_1);
        tv.setText(movie.getTitle() + "\n(" + movie.getReleaseDate().split("-")[0] + ")");

        ImageView img = (ImageView) view.findViewById(R.id.list_item_image_1);
        Picasso.with(getContext()).load(url+movie.getPosterPath()).into(img);

        TextView tx = (TextView) view.findViewById(R.id.text_vote_list);
        if ((new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).exists())) {
            Entries entry = new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).executeSingle();
            ImageView star = (ImageView) view.findViewById(R.id.image_star);
            if(entry.getVote()==0){
                star.setImageResource(android.R.drawable.star_big_off);
            }
            else{
                star.setImageResource(android.R.drawable.star_big_on);
            }
            tx.setText(entry.getVote()+"/5");
        }
        return view;
    }
}
