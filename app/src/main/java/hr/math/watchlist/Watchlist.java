package hr.math.watchlist;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 * Created by crompir on 30.01.16..
 */
public class Watchlist extends Application {

    @Override
    public void onCreate(){
        super.onCreate();


        //Initialization
        ActiveAndroid.initialize(this);
    }
}