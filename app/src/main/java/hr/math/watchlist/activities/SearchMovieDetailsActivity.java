package hr.math.watchlist.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.List;

import hr.math.watchlist.AppPreferences;
import hr.math.watchlist.R;
import hr.math.watchlist.dialogs.LoadingDialog;
import hr.math.watchlist.model.Genre;
import hr.math.watchlist.model.Movie;
import hr.math.watchlist.model.*;

import hr.math.watchlist.network.Api;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchMovieDetailsActivity extends AppCompatActivity {

    private final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("http://api.themoviedb.org/3").build();
    private Api movieDbApi = restadapter.create(Api.class);
    private Movie movie;
    private Long watchlistID;
    private Watchlist watchlist;
    private List<Cast> casts;
    private LoadingDialog progress;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new LoadingDialog(this);
        watchlistID = (new AppPreferences(this)).getWatchlistID();
        watchlist = new Select().from(Watchlist.class).where("id = ?", watchlistID).executeSingle();

        extras = getIntent().getExtras();
        if (extras != null) {
            final String id = extras.get("MOVIE_ID").toString();
            progress.show();

            From query = (new Select()).from(Movie.class).where("movieID = ?", id);
            if (query.exists()) {
                movie = query.executeSingle();
                showMovie(id);

            } else {
                movieDbApi.getMovie(id, new Callback<Movie>() {

                    @Override
                    public void success(Movie result, Response response) {
                        movie = result;
                        showMovie(id);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Something went wrong :-(", Toast.LENGTH_SHORT).show();
                        Log.d("Error: ", error.getLocalizedMessage());
                        progress.dismiss();
                    }
                });
            }
        }
    }

    public void showMovie(String id) {
        movieDbApi.getCast(id, new Callback<SearchCast>() {
            @Override
            public void success(SearchCast searchCast, Response response) {
                LinearLayout lin = (LinearLayout) findViewById(R.id.layout_cast);

                casts = searchCast.getResults();

                for (Cast cast : casts) {
                    LinearLayout novi = new LinearLayout(SearchMovieDetailsActivity.this);
                    novi.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    novi.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView img = new ImageView(SearchMovieDetailsActivity.this);
                    Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w300" + cast.getProfilePath()).resize(120, 180).into(img);
                    TextView tx = new TextView(SearchMovieDetailsActivity.this);
                    tx.setText(cast.getName() + "\n" + "(" + cast.getCharacter() + ")");
                    tx.setPadding(5, 0, 0, 0);
                    novi.addView(img);
                    novi.addView(tx);
                    novi.setPadding(5, 5, 5, 5);
                    lin.addView(novi);
                }

                ImageView img = (ImageView) findViewById(R.id.imageViewPoster);
                ImageView img1 = (ImageView) findViewById(R.id.imageViewPoster1);
                Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w300" + movie.getPosterPath()).into(img);

                //set Backdrop
                Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500" + movie.getBackdropPath()).fit().centerCrop().into(img1);
                img1.setBackgroundColor(Color.BLACK);
                img1.setAlpha(127);

                TextView tv = (TextView) findViewById(R.id.textViewTitle);
                tv.setText("Title: " + movie.getTitle());
                tv = (TextView) findViewById(R.id.textViewOriginalTitle);
                tv.setText("Original title: " + movie.getOriginalTitle());
                tv = (TextView) findViewById(R.id.textViewPlot);
                tv.setText(movie.getOverview());
                tv = (TextView) findViewById(R.id.textViewYear);
                tv.setText("Release date: " + movie.getReleaseDate().split("-")[0]);

                tv = (TextView) findViewById(R.id.textViewGenre);

                String genres = movie.getGenre();
                if (genres == null) {
                    genres = "";
                    for (Genre gen : movie.getGenres()) {
                        genres = genres + gen.getName() + " ";
                    }
                }
                tv.setText("Genre: " + genres);

                tv = (TextView) findViewById(R.id.textViewVote);
                tv.setText("Vote average: " + Double.toString(movie.getVoteAverage()));

                progress.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong :-(", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", error.getLocalizedMessage());
                progress.dismiss();
            }

        });
    }
    public void saveMovie() {
        Movie mov;

        //ako je unutra vec pada, zbog genre, primati string...
        From query = (new Select()).from(Movie.class).where("movieID = ?", movie.getMovieID());
        if (query.exists()) {
            mov = query.executeSingle();
        } else {
            mov = new Movie(movie.getTitle(), movie.getOriginalTitle(), movie.getVoteAverage(),
                    movie.getPosterPath(), movie.getOverview(), movie.getReleaseDate(),
                    movie.getGenres(), movie.getMovieID(), movie.getBackdropPath());

            mov.save();
        }
        if(!(new Select().from(Entries.class).where("movie=?",mov.getId()).where("watchlist=?",watchlist.getId()).exists())) {
            Entries entry = new Entries(watchlist, mov);
            entry.save();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        //check if movie is in database
        if (extras != null) {
            String id = extras.getString("MOVIE_ID");
            Movie mov;
            From query = new Select().from(Movie.class).where("movieID = ?", id);
            if(query.exists()){
                mov = query.executeSingle();
                if(new Select().from(Entries.class).where("movie = ?", mov.getId()).where("watchlist = ?", watchlist.getId()).exists()) {
                    menu.findItem(R.id.action_add_movie).setVisible(false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_movie) {
            //save movie to database
            saveDialog(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveDialog(final MenuItem item)
    {
        AlertDialog alert = new AlertDialog.Builder(SearchMovieDetailsActivity.this)
                .setTitle("Add movie to watchlist")
                .setMessage("Are you sure you want to add movie to this watchlist?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveMovie();
                        Toast.makeText(SearchMovieDetailsActivity.this, "Movie added...", Toast.LENGTH_SHORT).show();
                        item.setVisible(false);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //not saved
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
