package hr.math.watchlist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import hr.math.watchlist.R;
import hr.math.watchlist.model.Entries;
import hr.math.watchlist.model.Watchlist;

/**
 * Created by domagoj on 31.01.16..
 */
public class WatchlistAdapter extends ArrayAdapter<Watchlist> {
    private Context context;
    private List<Watchlist> watchlistList;
    private int resource;

    public WatchlistAdapter(Context context, int resource, List<Watchlist> objects) {
        super(context, resource, objects);
        this.context = context;
        this.watchlistList = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        Watchlist watchlist = watchlistList.get(position);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.BLACK);
        tv.setText(watchlist.getName());

        int size = (new Select()).from(Entries.class).where("watchlist = ?", watchlist.getId()).execute().size();
        tv = (TextView) view.findViewById(android.R.id.text2);
        tv.setText("Number of movies" + " : " + Integer.toString(size));
        tv.setTextColor(Color.BLACK);
        return view;
    }
}
