package com.imagesearch.search.model.repository;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.imagesearch.search.model.data.RecentSearch;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;

import javax.inject.Inject;



/**
 * This repository is responsible to store and handle the recent the searches.
 * For simplify it uses {@link SharedPreferences} as storage.
 *
 * @author Gilad Opher
 */
public class RecentSearchesRepository implements PresenterRecentSearchContract{


	/**
	 * The prefs name.
	 */
	private static final String RECENT_SEARCH_PREFS_NAME = "recent_search_pref_name";


	/**
	 * The {@link SharedPreferences} that used for storing the recent search,
	 */
	private SharedPreferences prefs;


	/**
	 * For simplify, the {@link Gson} is used to convert the {@link RecentSearch} to {@link String} for easy storing.
	 */
	private Gson gson;


	/**
	 * The actual object that keep and handle the data.
	 */
	private RecentSearch recentSearch;


	@Inject
	public RecentSearchesRepository(SharedPreferences prefs, Gson gson){
		this.prefs = prefs;
		this.gson = gson;
	}


	/**
	 * Get recent search method. The results are return via {@link GetSearchHistoryCallback}.
	 */
	@Override
	public void getSearchHistory(@NonNull GetSearchHistoryCallback searchHistoryCallback){
		searchHistoryCallback.onHistoryLoaded(recentSearch.getRecentAsList());
	}


	/**
	 * This method invokes whenever a new images were requested.
	 */
	@Override
	public void onNewQuery(String query){
		recentSearch.add(query);
	}


	/**
	 * This method invokes view become not available.
	 */
	@Override
	public void saveHistory(){
		String json = gson.toJson(recentSearch);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(RECENT_SEARCH_PREFS_NAME, json);
		editor.apply();
	}


	@Override
	public void reloadHistory(){
		String json = prefs.getString(RECENT_SEARCH_PREFS_NAME, "");
		if (json.isEmpty())
			recentSearch = new RecentSearch();
		else
			recentSearch = gson.fromJson(json, RecentSearch.class);
	}


	@Override
	public void clearHistory(){
		if (recentSearch != null){
			recentSearch.clear();
		}
	}

}
