package com.example.moviedb.View.Movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.moviedb.Adapter.AdapterMoviesList;
import com.example.moviedb.Model.MoviesList;
import com.example.moviedb.Rest.ApiClient;
import com.example.moviedb.Rest.ApiInterface;
import com.example.moviedb.Util.DbContract;
import com.example.moviedb.Util.DbHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.moviedb.Util.DbContract.MovieEntry.COLUMN_NAME_SORTING;
import static com.example.moviedb.Util.DbContract.MovieEntry.COLUMN_NAME_ID;
import static com.example.moviedb.Util.DbContract.MovieEntry.COLUMN_NAME_PATH_IMG;
import static com.example.moviedb.Util.DbContract.MovieEntry.COLUMN_NAME_RATING;
import static com.example.moviedb.Util.DbContract.MovieEntry.COLUMN_NAME_TITLE;
import static com.example.moviedb.Util.DbContract.MovieEntry.TABLE_MOVIES;
import static com.example.moviedb.Util.StatusView.STATUS_GAGAL;
import static com.example.moviedb.Util.StatusView.STATUS_NOCONNECTION;
import static com.example.moviedb.Util.StatusView.STATUS_PROGRESS;
import static com.example.moviedb.Util.StatusView.STATUS_SUKSES;
import static com.example.moviedb.Util.VariableGlobal.API_KEY;
import static com.example.moviedb.Util.VariableGlobal.DAILY;


public class MoviesPreserter {
    private Context context;
    private ViewMoviesList view;
    private AdapterMoviesList adapterMoviesList;
    private ApiInterface mApiMovie = ApiClient.getClient().create(ApiInterface.class);
    private DbHelper dbHelper;

    public MoviesPreserter(ViewMoviesList view, Context context){
        this.view = view;
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    void loadMoviesTranding(String time, int currentPage) {
        if (currentPage == 1){
            adapterMoviesList = new AdapterMoviesList(readSQLMovie(time), context);
            view.setAdapterMovies(adapterMoviesList, STATUS_PROGRESS, 1);
        }

        mApiMovie.getMoviesTranding(time, API_KEY, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MoviesList moviesList) {
                        if (moviesList.getResults() != null) {
                            if (currentPage == 1) {
                                insertToSQLMovie(moviesList, time);
                                adapterMoviesList = new AdapterMoviesList(moviesList.getResults(), context);
                            } else {
                                adapterMoviesList.addItems(moviesList.getResults());
                            }
                            view.setAdapterMovies(adapterMoviesList, STATUS_SUKSES, moviesList.getTotalPages());
                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            view.setAdapterMovies(adapterMoviesList, STATUS_NOCONNECTION,1);
                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void loadMoviesBySort(String sort, int currentPage) {
        if (currentPage == 1){
            adapterMoviesList = new AdapterMoviesList(readSQLMovie(sort), context);
            view.setAdapterMovies(adapterMoviesList, STATUS_PROGRESS, 1);
        }

        mApiMovie.getSortMovies(sort, API_KEY, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MoviesList moviesList) {
                        if (moviesList.getResults() != null) {
                            if (currentPage == 1) {
                                insertToSQLMovie(moviesList, sort);
                                adapterMoviesList = new AdapterMoviesList(moviesList.getResults(), context);
                            } else {
                                adapterMoviesList.addItems(moviesList.getResults());
                            }
                            view.setAdapterMovies(adapterMoviesList, STATUS_SUKSES, moviesList.getTotalPages());
                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            view.setAdapterMovies(adapterMoviesList, STATUS_NOCONNECTION,1);
                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void findMovie(String keyword, int currentPage) {
        mApiMovie.searchMovies(API_KEY, keyword, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MoviesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MoviesList moviesList) {
                        if (moviesList.getResults() != null) {
                            if (currentPage == 1) {
                                adapterMoviesList = new AdapterMoviesList(moviesList.getResults(), context);
                            } else {
                                adapterMoviesList.addItems(moviesList.getResults());
                            }
                            view.setAdapterMovies(adapterMoviesList, STATUS_SUKSES, moviesList.getTotalPages());

                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            view.setAdapterMovies(adapterMoviesList, STATUS_NOCONNECTION,1);
                        } else {
                            view.setAdapterMovies(adapterMoviesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void insertToSQLMovie(MoviesList moviesList, String sort){
        String[] wheres = {sort};
        dbHelper.deleteAll(TABLE_MOVIES, wheres);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        for (int i = 0; i < moviesList.getResults().size(); i++) {
            int id = moviesList.getResults().get(i).getId();
            String title = moviesList.getResults().get(i).getTitle();
            Double rated = moviesList.getResults().get(i).getVoteAverage();
            String img = moviesList.getResults().get(i).getPosterPath();

            values.put(COLUMN_NAME_ID, id);
            values.put(COLUMN_NAME_TITLE, title);
            values.put(COLUMN_NAME_RATING, rated);
            values.put(COLUMN_NAME_PATH_IMG, img);
            values.put(COLUMN_NAME_SORTING, sort);
            db.insert(TABLE_MOVIES, null, values);
        }
    }

    List<MoviesList> readSQLMovie(String sort){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_RATING,
                COLUMN_NAME_PATH_IMG
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_NAME_SORTING + " = ?";
        String[] wheres = {sort};

        Cursor cursor = db.query (
                TABLE_MOVIES,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                wheres,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<MoviesList> moviesLists = new ArrayList<>();
        while(cursor.moveToNext()) {
                int id = cursor.getInt(1);
                String title = cursor.getString(2);
                Double rated = cursor.getDouble(3);
                String img = cursor.getString(4);
                MoviesList movies = new MoviesList(img, id, title, rated);
                moviesLists.add(movies);
        }
        cursor.close();
        return moviesLists;
    }

    public interface ViewMoviesList {
        void setAdapterMovies(AdapterMoviesList mAdapter, int status, int pageSize);
    }
}
