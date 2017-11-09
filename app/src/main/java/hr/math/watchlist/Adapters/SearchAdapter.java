package hr.math.watchlist.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hr.math.watchlist.R;
import hr.math.watchlist.model.Search;

/**
 * Created by domagoj on 21.01.16..
 */

public class SearchAdapter extends ArrayAdapter<Search> {
    private String url="http://image.tmdb.org/t/p/w300";
    private Context context;
    private List<Search> searchList;

    public SearchAdapter(Context context, int resource, List<Search> objects) {
        super(context, resource, objects);
        this.context = context;
        this.searchList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item,parent,false);

        hr.math.watchlist.model.Search search = searchList.get(position);
        TextView tv = (TextView) view.findViewById(R.id.list_item_name);
        tv.setText(search.getTitle() + "\n(" + search.getReleaseDate().split("-")[0] + ")");

        ImageView img = (ImageView) view.findViewById(R.id.list_item_image);
        Picasso.with(getContext()).load(url+search.getPosterPath()).into(img);
        return view;
    }
}