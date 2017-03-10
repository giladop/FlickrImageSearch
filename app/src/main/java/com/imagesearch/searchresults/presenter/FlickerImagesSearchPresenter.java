package com.imagesearch.searchresults.presenter;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;

import javax.inject.Inject;


/**
 * The flicker images presenter.
 * This mediator component is laying between the view and data repositories.
 *
 * @author Gilad Opher
 */
public class FlickerImagesSearchPresenter implements PresenterImagesRepositoryContract.GetImagesCallback,
		FlickerImagesPresenterViewContract.UserActionsListener{


	/**
	 * The view.
	 */
	private FlickerImagesPresenterViewContract.View view;


	/**
	 * The images repository
	 */
	private PresenterImagesRepositoryContract imagesRepository;


	@Inject
	public FlickerImagesSearchPresenter(PresenterImagesRepositoryContract imagesRepository){
		this.imagesRepository = imagesRepository;
	}


	/**
	 * invoked after images where fetched from network or cache.
	 */
	@Override
	public void onImagesLoaded(List<ImageData> images){
		if (view != null){
			view.hideLoadingImagesProgressIndicator();
			if (images.isEmpty() && imagesRepository.isCached())
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

	//		recentSearchRepository.onNewQuery(query);
		}
	}

	@Override
	public void bind(FlickerImagesPresenterViewContract.View view){
		this.view = view;
	}


	@Override
	public void unbind(boolean strong){
		if (strong)
			imagesRepository.clearCache();

		this.view = null;
	}


}
