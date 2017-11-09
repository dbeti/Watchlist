package hr.math.watchlist.network;

import java.util.List;

import hr.math.watchlist.model.Movie;
import hr.math.watchlist.model.SearchCast;
import hr.math.watchlist.model.SearchList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by domagoj on 21.01.16..
 */
public interface Api {
    @GET("/search/movie?api_key=114ecbd18347c7d12966597708e0dd12")
    public void getSearch(@Query("query") String query, Callback<SearchList> response);

    @GET("/movie/{id}?api_key=114ecbd18347c7d12966597708e0dd12")
    public void getMovie(@Path("id") String id, Callback<Movie> response);

    @GET("/movie/{id}/credits?api_key=114ecbd18347c7d12966597708e0dd12")
    public void getCast(@Path("id") String id, Callback<SearchCast> response);

}
