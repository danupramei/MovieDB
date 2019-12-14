package com.example.moviedb.Util;

import android.provider.BaseColumns;

public final class DbContract {
    private DbContract() {
    }
    /* Inner class that defines the table contents */
    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_MOVIES = "movies";
        public static final String TABLE_SERIES = "series";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_PATH_IMG = "path_img";
    }

}
