package hr.math.watchlist.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import hr.math.watchlist.Adapters.SearchAdapter;
import hr.math.watchlist.R;
import hr.math.watchlist.dialogs.LoadingDialog;
import hr.math.watchlist.model.SearchList;
import hr.math.watchlist.network.Api;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchMoviesActivity extends AppCompatActivity {

    private List<hr.math.watchlist.model.Search> search;
    private final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("http://api.themoviedb.org/3").build();
    private Api movieDbApi = restadapter.create(Api.class);
    private LoadingDialog progress;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new LoadingDialog(this);
        listView = (ListView) findViewById(R.id.listView);
        searchView = (SearchView) findViewById(R.id.searchView);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        handleIntent(getIntent());
        searchView.clearFocus();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchMoviesActivity.this, SearchMovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID", search.get(position).getId().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        searchView.clearFocus();
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (query.length() != 0) {
                progress.show();
                movieDbApi.getSearch(query, new Callback<SearchList>() {
                    @Override
                    public void success(SearchList searches, Response response) {
                        SearchMoviesActivity.this.search = searches.getResults();
                        SearchAdapter search = new SearchAdapter(getApplicationContext(), R.layout.list_item, SearchMoviesActivity.this.search);
                        listView.setAdapter(search);
                        progress.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Search failed :-(", Toast.LENGTH_SHORT).show();
                        Log.d("Error: ", error.getLocalizedMessage());
                        progress.dismiss();
                    }
                });
            }
        }
    }

}
