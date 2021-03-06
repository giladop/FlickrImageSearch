package com.imagesearch.search.presenter;

import android.support.annotation.NonNull;


import java.util.List;



/**
 * The contract between {@link } and recent search repository.
 *
 * @author Gilad Opher
 */
public interface PresenterRecentSearchContract{


	/**
	 * Call back from recent search repository.
	 */
	interface GetSearchHistoryCallback{


		void onHistoryLoaded(List<String> queries);
	}


	void onNewQuery(String query);


	void saveHistory();


	void reloadHistory();


	void clearHistory();


	void getSearchHistory(@NonNull GetSearchHistoryCallback searchHistoryCallback);

}
