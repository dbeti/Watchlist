package hr.math.watchlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import hr.math.watchlist.R;
import hr.math.watchlist.activities.WatchlistsActivity;
import hr.math.watchlist.model.Watchlist;

/**
 * Created by domagoj on 31.01.16..
 */
public class CreateWatchlistDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.create_watchlist, null);

        builder.setView(view)
                .setTitle(R.string.dialog_create_wathclist_title)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String editTextValue = ((EditText) view.findViewById(R.id.watchlistName)).getText().toString();

                        if (editTextValue.length() != 0) {
                            Watchlist watchlist = new Watchlist(editTextValue);
                            WatchlistsActivity activity = (WatchlistsActivity) getActivity();
                            activity.watchlistList.add(watchlist);
                            activity.watchlistAdapter.notifyDataSetChanged();

                            watchlist.save();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateWatchlistDialog.this.getDialog().cancel();
                    }
                });
        builder.create();

        return builder.create();
    }
}
