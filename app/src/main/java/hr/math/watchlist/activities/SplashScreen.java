package hr.math.watchlist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import hr.math.watchlist.R;

/**
 * Created by crompir on 09.02.16..
 */
public class SplashScreen extends Activity {

    //timer
    private static int SPLASH_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, WatchlistsActivity.class);
                startActivity(i);

                //close activity
                finish();
            }
        }, SPLASH_TIME);
    }
}
