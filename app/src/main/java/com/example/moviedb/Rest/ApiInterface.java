package com.example.moviedb.Rest;

import com.example.moviedb.Model.Detail;
import com.example.moviedb.Model.ImagesDetail;
import com.example.moviedb.Model.MoviesList;
import com.example.moviedb.Model.SeriesList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    // TRANDING
    @GET("trending/movie/{time_window}")
    Observable<MoviesList> getMoviesTranding(@Path("time_window") String time_window, @Query("api_key") String api_key);
    @GET("trending/tv/{time_window}")
    Observable<SeriesList> getSeriesTranding(@Path("time_window") String time_window, @Query("api_key") String api_key);

    // MOVIES
    @GET("movie/{sort}")
    Observable<MoviesList> getSortMovies(@Path("sort") String sort, @Query("api_key") String api_key, @Query("page") int page);
    @GET("search/movie")
    Observable<MoviesList> searchMovies(@Query("api_key") String api_key, @Query("query") String query, @Query("page") int page);

    // SERIES
    @GET("tv/{sort}")
    Observable<SeriesList> getSortSeries(@Path("sort") String sort, @Query("api_key") String api_key, @Query("page") int page);
    @GET("search/tv")
    Observable<SeriesList> searchSeries(@Query("api_key") String api_key, @Query("query") String query, @Query("page") int page);

    // DETAIL
    @GET("{media}/{detail_id}")
    Observable<Detail> getDetail(@Path("detail_id") int detail_id, @Path("media") String media, @Query("api_key") String api_key);
    @GET("{media}/{detail_id}/images")
    Observable<ImagesDetail> getBackdrop(@Path("detail_id") int detail_id, @Path("media") String media, @Query("api_key") String api_key);
}
