package hr.math.watchlist.dialogs;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by domagoj on 01.02.16..
 */
public class LoadingDialog {
    private ProgressDialog progress;
    
    public LoadingDialog(Context context) {
        progress = new ProgressDialog(context);
    }
    public void show() {

        if (progress != null && !progress.isShowing()) {
            progress.setMessage("Please wait..."); // Insert resource
            progress.show();
        }
        
    }

    public void dismiss() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
