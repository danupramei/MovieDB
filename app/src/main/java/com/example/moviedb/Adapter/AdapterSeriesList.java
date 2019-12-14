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
import com.example.moviedb.Model.SeriesList;
import com.example.moviedb.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moviedb.Util.ImageGlide.setImageResize;
import static com.example.moviedb.Util.VariableGlobal.TV_TYPE;


public class AdapterSeriesList extends RecyclerView.Adapter<AdapterSeriesList.MyviewHolder> {
    private List<SeriesList> seriesLists;
    private Context context;

    OnSeriesListClickListener clickListener;

    public interface OnSeriesListClickListener {
        void onItemSeriesClicked(int id,
                                String path_img,
                                String judul,
                                String media_type,
                                View iv_poster,
                                View tv_title
        );
    }

    public void setOnItemClickListener(OnSeriesListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public AdapterSeriesList(List<SeriesList> seriesLists, Context context) {
        this.seriesLists = seriesLists;
        this.context = context;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gmbr_item) ImageView gmbr_item;
        @BindView(R.id.txt_rate) TextView txt_rate;
        @BindView(R.id.txt_judul) TextView txt_judul;
        @BindView(R.id.card_item)
        CardView card_item;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poster, parent, false);
        return new AdapterSeriesList.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        SeriesList series =  seriesLists.get(position);
        holder.txt_judul.setText(series.getName());
        holder.txt_rate.setText(String.valueOf(series.getVoteAverage()));
        setImageResize(context, series.getPosterPath(), holder.gmbr_item);
        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemSeriesClicked(series.getId(),
                        series.getPosterPath(),
                        series.getName(),
                        TV_TYPE,
                        holder.gmbr_item,
                        holder.txt_judul);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesLists == null ? 0 : seriesLists.size();
    }

    public void addItems(List<SeriesList> postItems) {
        seriesLists.addAll(postItems);
        notifyDataSetChanged();
    }

    public void clear() {
        seriesLists.clear();
        notifyDataSetChanged();
    }
}
