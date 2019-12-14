package com.example.moviedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.Model.MoviesList;
import com.example.moviedb.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moviedb.Util.ImageGlide.setImageResize;
import static com.example.moviedb.Util.VariableGlobal.MOVIE_TYPE;


public class AdapterMoviesList extends RecyclerView.Adapter<AdapterMoviesList.MyviewHolder> {
    private Context context;
    private List<MoviesList> moviesListData;

    OnMoviesListClickListener clickListener;

    public interface OnMoviesListClickListener {
        void onItemMovieClicked(int id,
                                String path_img,
                                String judul,
                                String media_type,
                                View iv_poster,
                                View tv_title
        );
    }

    public void setOnItemClickListener(OnMoviesListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public AdapterMoviesList(List<MoviesList> moviesListData, Context context) {
        this.moviesListData = moviesListData;
        this.context = context;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gmbr_item) ImageView gmbr_item;
        @BindView(R.id.txt_rate) TextView txt_rate;
        @BindView(R.id.txt_judul) TextView txt_judul;
        @BindView(R.id.card_item) CardView card_item;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyviewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        MoviesList item = moviesListData.get(position);
        holder.txt_judul.setText(item.getTitle());
        holder.txt_rate.setText(String.valueOf(item.getVoteAverage()));
        setImageResize(context, item.getPosterPath(), holder.gmbr_item);
        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemMovieClicked(item.getId(),
                        item.getPosterPath(),
                        item.getTitle(),
                        MOVIE_TYPE,
                        holder.gmbr_item,
                        holder.txt_judul);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesListData == null ? 0 : moviesListData.size();
    }

    public void addItems(List<MoviesList> postItems) {
        moviesListData.addAll(postItems);
        notifyDataSetChanged();
    }

    public void clear() {
        moviesListData.clear();
        notifyDataSetChanged();
    }
}
