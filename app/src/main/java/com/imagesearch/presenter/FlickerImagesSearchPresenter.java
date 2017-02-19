package com.imagesearch.presenter;

import com.imagesearch.model.data.ImageData;

import java.util.List;

import javax.inject.Inject;


/**
 * The flicker images presenter.
 * This mediator component is laying between the view and data repositories.
 *
 * @author Gilad Opher
 */
public class FlickerImagesSearchPresenter implements PresenterImagesRepositoryContract.GetImagesCallback,
		PresenterRecentSearchContract.GetSearchHistoryCallback, FlickerImagesPresenterViewContract.UserActionsListener{


	/**
	 * The view.
	 */
	private FlickerImagesPresenterViewContract.View view;


	/**
	 * The images repository
	 */
	private PresenterImagesRepositoryContract imagesRepository;


	/**
	 * The recent search query repository.
	 */
	private PresenterRecentSearchContract recentSearchRepository;


	@Inject
	public FlickerImagesSearchPresenter(PresenterImagesRepositoryContract imagesRepository, PresenterRecentSearchContract recentSearchRepository){
		this.imagesRepository = imagesRepository;
		this.recentSearchRepository = recentSearchRepository;
	}


	/**
	 * invoked after images where fetched from network or cache.
	 */
	@Override
	public void onImagesLoaded(List<ImageData> images){
		if (view != null){
			view.hideLoadingImagesProgressIndicator();
			if (images.isEmpty())
				view.onImagesNotFound();
			else
				view.onImagesLoaded(images);
		}
	}


	/**
	 * invoked when images not found for query or when error was occur.
	 */
	@Override
	public void onImagesNotAvailable(){
		if (view == null) return;

		view.onImagesNotFound();
	}


	/**
	 * Request images.
	 */
	@Override
	public void getImages(String query, int page){
		if (imagesRepository.getImages(query, page, this)){
			if (view != null)
				view.showLoadingImagesProgressIndicator();

			recentSearchRepository.onNewQuery(query);
		}
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
	public void bind(FlickerImagesPresenterViewContract.View view){
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
