package hr.math.watchlist.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

import hr.math.watchlist.Adapters.WatchlistAdapter;
import hr.math.watchlist.AppPreferences;
import hr.math.watchlist.R;
import hr.math.watchlist.dialogs.CreateWatchlistDialog;
import hr.math.watchlist.model.Entries;
import hr.math.watchlist.model.Watchlist;

public class WatchlistsActivity extends AppCompatActivity {

    public List<Watchlist> watchlistList;
    public WatchlistAdapter watchlistAdapter;
    private ListView listView;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlists);
        appPreferences = new AppPreferences(this);
        watchlistList = (new Select()).all().from(Watchlist.class).execute();
        watchlistAdapter = new WatchlistAdapter(getApplicationContext(), android.R.layout.simple_list_item_2, watchlistList);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(watchlistAdapter);

        List<Entries> entries = (new Select()).from(Entries.class).execute();
        for (Entries entry:
             entries) {
            Log.d("entries", entry.getMovie().getTitle() + entry.getWatchlist().getName());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                appPreferences.setWatchlistID(watchlistList.get(position).getId());
                Intent intent = new Intent(WatchlistsActivity.this, WatchlistDetailsActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(WatchlistsActivity.this);
                builder.setTitle("Are you sure you want to delete this movie?");
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Watchlist watchlist = (new Select()).from(Watchlist.class).where("Id = ?", watchlistList.get(pos).getId()).executeSingle();
                        watchlist.delete();
                        watchlistList.remove(pos);
                        watchlistAdapter.notifyDataSetChanged();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateWatchlistDialog dialog = new CreateWatchlistDialog();
                FragmentManager manager = getFragmentManager();
                dialog.show(manager, "create");
            }
        });
    }
}
