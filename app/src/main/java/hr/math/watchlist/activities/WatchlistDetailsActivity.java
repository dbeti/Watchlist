package hr.math.watchlist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import hr.math.watchlist.Adapters.MovieAdapter;
import hr.math.watchlist.AppPreferences;
import hr.math.watchlist.model.Entries;
import hr.math.watchlist.model.Movie;
import hr.math.watchlist.model.Watchlist;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import hr.math.watchlist.R;

public class WatchlistDetailsActivity extends AppCompatActivity {

    private List<Movie> movies;
    private long watchlistID;
    private MovieAdapter movieAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        watchlistID = (new AppPreferences(this)).getWatchlistID();

        listView = (ListView) findViewById(R.id.listView3);

        From from = (new Select()).from(Entries.class).where("watchlist = ?", watchlistID);
        if(from.exists()) {
            Entries entry = from.executeSingle();

            movies = entry.movies(watchlistID);
            if (movies.size() > 0) {
                movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.list_item_star, WatchlistDetailsActivity.this.movies);
                listView.setAdapter(movieAdapter);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(WatchlistDetailsActivity.this, WatchlistMovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID", movies.get(position).getMovieID());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(WatchlistDetailsActivity.this);
                builder.setTitle("Are you sure you want to delete this movie?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Entries entry = (new Select()).from(Entries.class).where("watchlist = ?", watchlistID).where("movie = ?", movies.get(pos).getId()).executeSingle();
                        entry.delete();
                        movies.remove(pos);
                        movieAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(WatchlistDetailsActivity.this, SearchMoviesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
