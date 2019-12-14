package com.example.moviedb.View.Detail;

import android.content.Context;

import com.example.moviedb.Adapter.AdapterMoviesList;
import com.example.moviedb.Model.Detail;
import com.example.moviedb.Model.ImagesDetail;
import com.example.moviedb.Rest.ApiClient;
import com.example.moviedb.Rest.ApiInterface;
import com.example.moviedb.Util.DbHelper;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.moviedb.Util.StatusView.STATUS_GAGAL;
import static com.example.moviedb.Util.StatusView.STATUS_NOCONNECTION;
import static com.example.moviedb.Util.StatusView.STATUS_SUKSES;
import static com.example.moviedb.Util.VariableGlobal.API_KEY;


public class DetailPreserter {
    private Context context;
    private ViewDetail view;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public DetailPreserter(ViewDetail view, Context context){
        this.view = view;
        this.context = context;
    }

    void loadDetail(int id, String media) {
        apiInterface.getDetail(id, media, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Detail>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Detail detail) {
                        if (detail != null) {
                            view.showDetail(detail, STATUS_SUKSES);
                        } else {
                            view.showDetail(detail, STATUS_GAGAL);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            view.showDetail(new Detail(), STATUS_NOCONNECTION);
                        } else {
                            view.showDetail(new Detail(), STATUS_GAGAL);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void loadBackdrop(int id, String media) {
        apiInterface.getBackdrop(id, media, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImagesDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ImagesDetail imagesDetail) {
                            view.showImageSlide(imagesDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showImageSlide(new ImagesDetail());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface ViewDetail {
        void showDetail(Detail detail, int status);
        void showImageSlide(ImagesDetail imagesDetail);
    }
}
