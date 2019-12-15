package com.example.moviedb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.Model.VideoDetail;
import com.example.moviedb.R;

import java.util.List;

import static com.example.moviedb.Util.ImageGlide.setImageResize;
import static com.example.moviedb.Util.ImageGlide.setImageThumb;

public class AdapterVideoYoutube extends RecyclerView.Adapter<AdapterVideoYoutube.MyViewHolder> {

    private List<VideoDetail> videoDetails;
    Context context;

    OnVideoListClickListener clickListener;

    public interface OnVideoListClickListener {
        void onItemVideoClicked(String id_key);
    }

    public void setOnItemClickListener(OnVideoListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public AdapterVideoYoutube(List<VideoDetail> videoDetails, Context context) {
        super();
        this.videoDetails = videoDetails;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView gambar;
        public RelativeLayout layoutVideo;

        public MyViewHolder(View view) {
            super(view);
            gambar = (ImageView) view.findViewById(R.id.video_thumbnail);
            layoutVideo = (RelativeLayout) view.findViewById(R.id.layoutVideo);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_video, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VideoDetail video = videoDetails.get(position);


        if (video.getSite().equalsIgnoreCase("YouTube")) {
            System.out.println("VIDEOOOO "+video.getKey());
            setImageThumb(context, "http://img.youtube.com/vi/" + video.getKey() + "/default.jpg",
                    holder.gambar);
        }

        holder.layoutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemVideoClicked(video.getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoDetails.size();
    }
}