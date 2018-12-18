package com.example.monu.wetherapp.Other;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Hard-won on 2/11/2018.
 */

public class RecentSearchSuggestions extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "com.dualbrotech.tourmate.others.RecentSearchSuggestions";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public RecentSearchSuggestions(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
