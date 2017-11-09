package hr.math.watchlist.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.squareup.picasso.Picasso;

import java.util.List;

import hr.math.watchlist.AppPreferences;
import hr.math.watchlist.R;
import hr.math.watchlist.dialogs.LoadingDialog;
import hr.math.watchlist.model.Cast;
import hr.math.watchlist.model.Entries;
import hr.math.watchlist.model.Genre;
import hr.math.watchlist.model.Movie;
import hr.math.watchlist.model.SearchCast;
import hr.math.watchlist.network.Api;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WatchlistMovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("http://api.themoviedb.org/3").build();
    private Api movieDbApi = restadapter.create(Api.class);
    private List<Cast> casts;
    private LoadingDialog progress;
    private Dialog rankDialog;
    private Long watchlistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        watchlistID = (new AppPreferences(this)).getWatchlistID();
        progress = new LoadingDialog(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String id = extras.get("MOVIE_ID").toString();
            From query = (new Select()).from(Movie.class).where("movieID = ?", id);
            if (query.exists()) {
                movie = query.executeSingle();
                progress.show();
                showMovie(id);
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
                    LinearLayout novi = new LinearLayout(WatchlistMovieDetailsActivity.this);
                    novi.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    novi.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView img = new ImageView(WatchlistMovieDetailsActivity.this);
                    Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w300" + cast.getProfilePath()).resize(120, 180).into(img);
                    TextView tx = new TextView(WatchlistMovieDetailsActivity.this);
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

                //Show comment if exist
                showComment();
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
    public void voteMovie(View v){
        rankDialog = new Dialog(WatchlistMovieDetailsActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(0);
        ratingBar.setStepSize(1.0f);

        final EditText comment = (EditText) rankDialog.findViewById(R.id.text_comment);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText("Vote movie:");

        if (new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).exists()){
            Entries ent = new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).executeSingle();
            comment.setText(ent.getComment());
        }


        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).exists())) {
                            Entries ent = new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).executeSingle();
                            new Update(Entries.class).set("vote = ?", (int)ratingBar.getRating()).where("id=?", ent.getId()).execute();
                            new Update(Entries.class).set("comment = ?", comment.getText()).where("id=?", ent.getId()).execute();

                        }
                        //refresh comment
                        showComment();
                        rankDialog.dismiss();
                    }
                }
        );
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    public void showComment()
    {
        //Comment
        if ((new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).exists())) {
            Entries ent = new Select().from(Entries.class).where("movie=?", movie.getId()).where("watchlist=?", watchlistID).executeSingle();
            if(ent.getComment().equals("")){
                LinearLayout lina = (LinearLayout) findViewById(R.id.section_comment);
                lina.setVisibility(View.GONE);
                View linija = (View) findViewById(R.id.line_after_comment);
                linija.setVisibility(View.GONE);
            }
            else{
                LinearLayout lina = (LinearLayout) findViewById(R.id.section_comment);
                TextView tekst = (TextView) findViewById(R.id.text_comment_result);
                tekst.setText(ent.getComment());
                lina.setVisibility(View.VISIBLE);
                View linija = (View) findViewById(R.id.line_after_comment);
                linija.setVisibility(View.VISIBLE);
            }
        }
    }
}
