package com.example.moviedb.View.Series;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.moviedb.Adapter.AdapterSeriesList;
import com.example.moviedb.Model.SeriesList;
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

import static com.example.moviedb.Util.DbContract.MovieEntry.TABLE_SERIES;
import static com.example.moviedb.Util.StatusView.STATUS_GAGAL;
import static com.example.moviedb.Util.StatusView.STATUS_NOCONNECTION;
import static com.example.moviedb.Util.StatusView.STATUS_PROGRESS;
import static com.example.moviedb.Util.StatusView.STATUS_SUKSES;
import static com.example.moviedb.Util.VariableGlobal.API_KEY;


public class SeriesPreserter {
    private Context context;
    private ViewSeriesList view;
    private AdapterSeriesList adapterSeriesList;
    private ApiInterface mApiSeries = ApiClient.getClient().create(ApiInterface.class);
    private DbHelper dbHelper;

    public SeriesPreserter(ViewSeriesList view, Context context){
        this.view = view;
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    void loadSeriesTranding(String time) {
        view.setAdapterSeries(adapterSeriesList, STATUS_PROGRESS, 1);
        adapterSeriesList = new AdapterSeriesList(readSQLMovie(), context);
        view.setAdapterSeries(adapterSeriesList, 0, 1);
        mApiSeries.getSeriesTranding(time, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SeriesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SeriesList seriesList) {
                        if (seriesList != null) {
                            insertToSQLMovie(seriesList);
                            adapterSeriesList = new AdapterSeriesList(seriesList.getResults(), context);
                            view.setAdapterSeries(adapterSeriesList, STATUS_SUKSES, 1);
                        } else {
                            view.setAdapterSeries(adapterSeriesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            view.setAdapterSeries(adapterSeriesList, STATUS_NOCONNECTION,1);
                        }
                        else {
                            view.setAdapterSeries(adapterSeriesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    void loadSeriesBySort(String sort, int currentPage) {
        if (currentPage == 1){
            view.setAdapterSeries(adapterSeriesList, STATUS_PROGRESS, 1);
        }
        mApiSeries.getSortSeries(sort, API_KEY, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SeriesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SeriesList seriesList) {
                        if (seriesList != null) {
                            if (currentPage == 1) {
                                adapterSeriesList = new AdapterSeriesList(seriesList.getResults(), context);
                            } else {
                                adapterSeriesList.addItems(seriesList.getResults());
                            }
                            view.setAdapterSeries(adapterSeriesList, STATUS_SUKSES, seriesList.getTotalPages());

                        } else {
                            view.setAdapterSeries(adapterSeriesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void findSeries(String keyword, int currentPage) {
        mApiSeries.searchSeries(API_KEY, keyword, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SeriesList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SeriesList seriesList) {
                        if (seriesList != null) {
                            if (currentPage == 1) {
                                adapterSeriesList = new AdapterSeriesList(seriesList.getResults(), context);
                            } else {
                                adapterSeriesList.addItems(seriesList.getResults());
                            }
                            view.setAdapterSeries(adapterSeriesList, STATUS_SUKSES, seriesList.getTotalPages());

                        } else {
                            view.setAdapterSeries(adapterSeriesList, STATUS_GAGAL,1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void insertToSQLMovie(SeriesList seriesLists){
        dbHelper.deleteAll(TABLE_SERIES);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        for (int i = 0; i < seriesLists.getResults().size(); i++) {
            int id = seriesLists.getResults().get(i).getId();
            String title = seriesLists.getResults().get(i).getName();
            Double rated = seriesLists.getResults().get(i).getVoteAverage();
            String img = seriesLists.getResults().get(i).getPosterPath();

            values.put(DbContract.MovieEntry.COLUMN_NAME_ID, id);
            values.put(DbContract.MovieEntry.COLUMN_NAME_TITLE, title);
            values.put(DbContract.MovieEntry.COLUMN_NAME_RATING, rated);
            values.put(DbContract.MovieEntry.COLUMN_NAME_PATH_IMG, img);
            db.insert(DbContract.MovieEntry.TABLE_SERIES, null, values);
        }
    }

    List<SeriesList> readSQLMovie(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DbContract.MovieEntry.COLUMN_NAME_ID,
                DbContract.MovieEntry.COLUMN_NAME_TITLE,
                DbContract.MovieEntry.COLUMN_NAME_RATING,
                DbContract.MovieEntry.COLUMN_NAME_PATH_IMG
        };

        Cursor cursor = db.query(
                DbContract.MovieEntry.TABLE_SERIES,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<SeriesList> seriesLists = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(1);
            String title = cursor.getString(2);
            Double rated = cursor.getDouble(3);
            String img = cursor.getString(4);
            SeriesList series = new SeriesList(title, id, rated, img);
            seriesLists.add(series);
        }
        cursor.close();
        return seriesLists;
    }

    public interface ViewSeriesList {
        void setAdapterSeries(AdapterSeriesList mAdapter, int status, int pageSize);
    }
}
