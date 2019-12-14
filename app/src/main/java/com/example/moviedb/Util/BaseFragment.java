package com.example.moviedb.Util;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb.Adapter.AdapterSortList;
import com.example.moviedb.R;

public class BaseFragment extends Fragment {
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Please Wait...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void getDialogSortList(AdapterSortList mAdapter) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_list_sort, null);
        builderSingle.setView(customLayout);
        RecyclerView recycler_dialog = customLayout.findViewById(R.id.recycler_dialog);

        //recycler_dialog.setHasFixedSize(true);
        recycler_dialog.setNestedScrollingEnabled(false);
        recycler_dialog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler_dialog.setAdapter(mAdapter);

        // create and show the alert dialog
        AlertDialog dialog = builderSingle.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        mAdapter.setOnItemClickListener(new AdapterSortList.OnListClickListener() {
            @Override
            public void onClicked() {
                dialog.dismiss();
            }
        });
    }

    public void cekLoad(){}
    public void noConnection(){}
}
