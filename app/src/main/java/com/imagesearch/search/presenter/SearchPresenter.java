package com.imagesearch.search.presenter;

import java.util.List;

import javax.inject.Inject;



/**
 * @author Gilad Opher
 */
public class SearchPresenter implements SearchPresenterViewContract.UserActionsListener,
		PresenterRecentSearchContract.GetSearchHistoryCallback{


	private SearchPresenterViewContract.View view = null;


	/**
	 * The recent search query repository.
	 */
	private PresenterRecentSearchContract recentSearchRepository;


	@Inject
	public SearchPresenter(PresenterRecentSearchContract recentSearchRepository){
		this.recentSearchRepository = recentSearchRepository;
	}


	@Override
	public void onNewQuery(String query){
		recentSearchRepository.onNewQuery(query);
	}


	/**
	 * Request recent search.
	 */
	@Override
	public void getHistory(){
		recentSearchRepository.getSearchHistory(this);
	}


	/**
	 * Clear recent search.
	 */
	@Override
	public void clearHistory(){
		recentSearchRepository.clearHistory();
	}


	/**
	 * Invoke when view become visible.
	 */
	@Override
	public void bind(SearchPresenterViewContract.View view){
		this.view = view;
		recentSearchRepository.reloadHistory();
	}


	/**
	 * Invoke when view become unavailable.
	 */
	@Override
	public void unbind(){
		view = null;
		recentSearchRepository.saveHistory();
	}


	/**
	 * Invoke when recent searches was loaded.
	 */
	@Override
	public void onHistoryLoaded(List<String> history){
		if (view != null)
			view.onHistoryLoaded(history);
	}
}
