package com.example.moviedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterSortList extends RecyclerView.Adapter<AdapterSortList.CustomViewHolder> {
    private List<String> data = new ArrayList<String>();
    private String dataCecked;

    OnListClickListener clickListener;
    OnChangeListener itemListener;

    public interface OnListClickListener {
        void onClicked();
    }

    public interface OnChangeListener {
        void onChanged(String result);
    }

    public void setOnItemClickListener(OnListClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setChangeSorting(OnChangeListener itemListener) {
        this.itemListener = itemListener;
    }

    public AdapterSortList(String dataCecked) {
        this.dataCecked = dataCecked;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox) CheckBox checkBox;
        @BindView(R.id.wrap_checkbox)
        LinearLayout wrap_checkbox;
        @BindView(R.id.txt_checkbox)
        TextView txt_checkbox;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            data.add("Tranding Daily");
            data.add("Tranding Weekly");
            data.add("Popular");
            data.add("Top Rated");
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sort, viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.txt_checkbox.setText(data.get(position));
        holder.wrap_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.setChecked(true);
                itemListener.onChanged(data.get(position));
                clickListener.onClicked();
            }
        });

        holder.checkBox.setChecked(false);
        if (dataCecked.equals(data.get(position))) {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
