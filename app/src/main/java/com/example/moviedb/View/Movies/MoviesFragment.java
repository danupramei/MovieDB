package com.example.moviedb.View.Movies;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.Adapter.AdapterMoviesList;
import com.example.moviedb.Adapter.AdapterSortList;
import com.example.moviedb.R;
import com.example.moviedb.Util.BaseFragment;
import com.example.moviedb.Util.PaginationListener;
import com.example.moviedb.View.Detail.DetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.moviedb.Util.PaginationListener.FIRST_PAGE;
import static com.example.moviedb.Util.StatusView.STATUS_GAGAL;
import static com.example.moviedb.Util.StatusView.STATUS_NOCONNECTION;
import static com.example.moviedb.Util.StatusView.STATUS_NO_DATA;
import static com.example.moviedb.Util.StatusView.STATUS_PROGRESS;
import static com.example.moviedb.Util.StatusView.STATUS_SUKSES;
import static com.example.moviedb.Util.VariableGlobal.DAILY;
import static com.example.moviedb.Util.VariableGlobal.POPULAR;
import static com.example.moviedb.Util.VariableGlobal.TAG_ID;
import static com.example.moviedb.Util.VariableGlobal.TAG_IMG;
import static com.example.moviedb.Util.VariableGlobal.TAG_MEDIA_TYPE;
import static com.example.moviedb.Util.VariableGlobal.TAG_TITLE;
import static com.example.moviedb.Util.VariableGlobal.TOP_RATED;
import static com.example.moviedb.Util.VariableGlobal.WEEKLY;

public class MoviesFragment extends BaseFragment implements MoviesPreserter.ViewMoviesList,
        AdapterMoviesList.OnMoviesListClickListener {
    private MoviesPreserter presenter;
    private int currentPage = FIRST_PAGE;
    private String searchKey = "";
    private String sorting = "Tranding Daily";

    @BindView(R.id.search_bar) LinearLayout search_bar;
    @BindView(R.id.recycler_movies) RecyclerView recycler_movies;
    @BindView(R.id.et_search_movies) EditText et_search_movies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie, container, false);
        ButterKnife.bind(this, root);

        //Presenter
        presenter = new MoviesPreserter(this, getContext());
        presenter.loadMoviesTranding(DAILY, currentPage);

        et_search_movies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                currentPage = FIRST_PAGE;
                searchKey = editable.toString();
                cekLoad();
            }
        });
        return root;
    }

    @OnClick(R.id.btn_sort_movies)
    void showListSortMovies(){
        getDialogSortList(new AdapterSortList(sorting));
    }

    @Override
    public void setAdapterMovies(AdapterMoviesList mAdapter, int status, int pageSize) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        if (currentPage == FIRST_PAGE){
            recycler_movies.setLayoutManager(layoutManager);
            recycler_movies.setHasFixedSize(true);
            recycler_movies.setNestedScrollingEnabled(true);
            recycler_movies.setAdapter(mAdapter);
        }

        switch (status){
            case STATUS_PROGRESS:
                showProgressDialog();
                break;
            case STATUS_SUKSES:
                hideProgressDialog();
                mAdapter.setOnItemClickListener(MoviesFragment.this);
                recycler_movies.addOnScrollListener(new PaginationListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        currentPage++;
                        if (currentPage <= pageSize){
                            cekLoad();
                        }
                    }
                });
                break;
            case STATUS_NO_DATA:
                hideProgressDialog();
                break;
            case STATUS_GAGAL:
                hideProgressDialog();
                break;
            case STATUS_NOCONNECTION:
                noConnection();
                break;
        }
    }

    @Override
    public void getDialogSortList(AdapterSortList mAdapter) {
        mAdapter.setChangeSorting(new AdapterSortList.OnChangeListener() {
            @Override
            public void onChanged(String result) {
                currentPage = FIRST_PAGE;
                searchKey = "";
                et_search_movies.setText(searchKey);
                sorting = result;
                cekLoad();
            }
        });
        super.getDialogSortList(mAdapter);
    }

    @Override
    public void cekLoad() {
        if (searchKey == null || searchKey.equals("")){
            switch (sorting){
                case "Tranding Daily":
                    presenter.loadMoviesTranding(DAILY, currentPage);
                    break;
                case "Tranding Weekly":
                    presenter.loadMoviesTranding(WEEKLY, currentPage);
                    break;
                case "Popular":
                    presenter.loadMoviesBySort(POPULAR, currentPage);
                    break;
                case "Top Rated":
                    presenter.loadMoviesBySort(TOP_RATED, currentPage);
                    break;
            }
        } else {
            presenter.findMovie(searchKey, currentPage);
        }
    }

    @Override
    public void noConnection() {
        hideProgressDialog();
    }

    @Override
    public void onItemMovieClicked(int id, String path_img, String judul, String media_type,
                                   View iv_poster, View tv_title) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(TAG_ID, id);
        intent.putExtra(TAG_IMG, path_img);
        intent.putExtra(TAG_TITLE, judul);
        intent.putExtra(TAG_MEDIA_TYPE, media_type);

        Pair<View, String> p1 = Pair.create((View) iv_poster, "profile");
        Pair<View, String> p2 = Pair.create((View) tv_title, "title");
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), p1, p2);

        startActivity(intent, options.toBundle());
    }
}