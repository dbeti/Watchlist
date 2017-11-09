package hr.math.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by domagoj on 01.02.16..
 */
public class AppPreferences {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String MYPREFS = "preferences";
    public AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MYPREFS, Context.MODE_PRIVATE);
    }

    public void setWatchlistID(long id) {
        sharedPreferences.edit().putLong("WATCHLIST_ID", id).apply();
    }

    public long getWatchlistID() {
        return sharedPreferences.getLong("WATCHLIST_ID", 0);
    }
}
