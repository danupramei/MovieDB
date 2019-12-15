package com.example.moviedb.View.Detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moviedb.Adapter.AdapterDetailImage;
import com.example.moviedb.Adapter.AdapterVideoYoutube;
import com.example.moviedb.Model.Detail;
import com.example.moviedb.Model.ImagesDetail;
import com.example.moviedb.Model.VideoDetail;
import com.example.moviedb.R;
import com.example.moviedb.Util.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

import static com.example.moviedb.Util.ImageGlide.setImage;
import static com.example.moviedb.Util.StatusView.STATUS_GAGAL;
import static com.example.moviedb.Util.StatusView.STATUS_NOCONNECTION;
import static com.example.moviedb.Util.StatusView.STATUS_NO_DATA;
import static com.example.moviedb.Util.StatusView.STATUS_PROGRESS;
import static com.example.moviedb.Util.StatusView.STATUS_SUKSES;
import static com.example.moviedb.Util.VariableGlobal.MOVIE_TYPE;
import static com.example.moviedb.Util.VariableGlobal.TAG_ID;
import static com.example.moviedb.Util.VariableGlobal.TAG_IMG;
import static com.example.moviedb.Util.VariableGlobal.TAG_MEDIA_TYPE;
import static com.example.moviedb.Util.VariableGlobal.TAG_TITLE;
import static com.example.moviedb.Util.VariableGlobal.TAG_YOUTUBE_KEY;
import static com.example.moviedb.Util.VariableGlobal.TV_TYPE;

public class DetailActivity extends AppCompatActivity implements DetailPreserter.ViewDetail,
        AdapterVideoYoutube.OnVideoListClickListener {
    private DetailPreserter presenter;
    String path_poster;
    String media_type;
    String releadeText;
    int id;

    @BindView(R.id.indicator_detail_product) CircleIndicator indicator;
    @BindView(R.id.back) ImageView back;
    @BindView(R.id.gmbr_item) ImageView gmbr_item;
    @BindView(R.id.txt_detail_judul) TextView txt_detail_judul;
    @BindView(R.id.txt_release) TextView txt_release;
    @BindView(R.id.txt_rating_detail) TextView txt_rating_detail;
    @BindView(R.id.txt_deskripsi) TextView txt_deskripsi;
    @BindView(R.id.txt_title_detail) TextView txt_title_detail;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.collapseApp) AppBarLayout collapseApp;
    @BindView(R.id.img_detail) ViewPager mPager;
    @BindView(R.id.content_video) LinearLayout content_video;
    @BindView(R.id.recycler_video_detail) RecyclerView recycler_video_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.black));
        collapsing_toolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparant));
        collapseApp.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.COLLAPSED){
                    txt_title_detail.setVisibility(View.VISIBLE);
                    back.setColorFilter(ContextCompat.getColor(DetailActivity.this,
                            R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    txt_title_detail.setVisibility(View.INVISIBLE);
                    back.setColorFilter(ContextCompat.getColor(DetailActivity.this,
                            R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            path_poster = "";
            media_type = "";
        } else {
            id = extras.getInt(TAG_ID);
            media_type = extras.getString(TAG_MEDIA_TYPE);
            txt_detail_judul.setText(extras.getString(TAG_TITLE));
            setImage(this, extras.getString(TAG_IMG), gmbr_item);
        }

        if (media_type.equals(MOVIE_TYPE)){
            txt_title_detail.setText("Detail Movie");
            releadeText = "Release Date: ";
        } else if (media_type.equals(TV_TYPE)){
            txt_title_detail.setText("Detail TV Series");
            releadeText = "First Air Date: ";
        }

        // Presenter
        presenter = new DetailPreserter(this, DetailActivity.this);
        presenter.loadDetail(id, media_type);
        presenter.loadBackdrop(id, media_type);
        presenter.loadTrailer(id, media_type);

    }

    @OnClick(R.id.back)
    void goBack(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public void showDetail(Detail detail, int status) {
        switch (status){
            case STATUS_PROGRESS:
                break;
            case STATUS_SUKSES:
                txt_release.setText(releadeText+detail.getReleaseDate());
                txt_rating_detail.setText(detail.getVoteAverage()+"/10");
                txt_deskripsi.setText(detail.getOverview());
                break;
            case STATUS_NO_DATA:
                break;
            case STATUS_GAGAL:
                break;
            case STATUS_NOCONNECTION:
                break;
        }
    }

    @Override
    public void showImageSlide(ImagesDetail imagesDetail) {
        mPager.setAdapter(new AdapterDetailImage(DetailActivity.this, imagesDetail.getBackdrops()));
        if (imagesDetail.getBackdrops() != null && imagesDetail.getBackdrops().size() > 1) {
            indicator.setViewPager(mPager);
        }
    }

    @Override
    public void showVideoTrailer(VideoDetail videoDetail) {
        if (videoDetail.getResults().size() <= 0) {
            content_video.setVisibility(View.GONE);
        } else {
            content_video.setVisibility(View.VISIBLE);
        }
        AdapterVideoYoutube mAdapter = new AdapterVideoYoutube(videoDetail.getResults(), DetailActivity.this);
        recycler_video_detail.setHasFixedSize(true);
        recycler_video_detail.setNestedScrollingEnabled(false);
        recycler_video_detail.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recycler_video_detail.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemVideoClicked(String id_key) {
        Intent intent = new Intent(this, YPlayerActivity.class);
        Bundle detail = new Bundle();
        detail.putString(TAG_YOUTUBE_KEY, id_key);
        intent.putExtras(detail);
        startActivity(intent);
    }
}
