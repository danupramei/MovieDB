package com.example.moviedb.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class ImageGlide {
    static String BASE_URL_IMG = "https://image.tmdb.org/t/p/";
    static String POSTER_LIST_SIZE = "w92";
    static String POSTER_DETAIL_SIZE = "w500";

    public static void setImageResize(Context context, String url, ImageView img) {
        try {
            if (!url.equals("")) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(false);
                Glide.with(context)
                        .load(BASE_URL_IMG + POSTER_LIST_SIZE + url)
                        .apply(options)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                // Gambar siap di show
                                img.setImageDrawable(resource);
                            }

                            @Override
                            public void onLoadFailed(Drawable errorDrawable) {
                                // Gambar gagal diload
                                img.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                // Gambar gagal mulai diload
                                img.setImageDrawable(placeholder);
                            }

                        });
            } else {

            }
        } catch (Exception e) {
        }
    }

    public static void setImage(Context context, String url, ImageView img) {
        try {
            if (!url.equals("")) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(false);
                Glide.with(context)
                        .load(BASE_URL_IMG + POSTER_DETAIL_SIZE + url)
                        .apply(options)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                // Gambar siap di show
                                img.setImageDrawable(resource);
                            }

                            @Override
                            public void onLoadFailed(Drawable errorDrawable) {
                                // Gambar gagal diload
                                img.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                // Gambar gagal mulai diload
                                img.setImageDrawable(placeholder);
                            }

                        });
            } else {

            }
        } catch (Exception e) {
        }
    }
}
