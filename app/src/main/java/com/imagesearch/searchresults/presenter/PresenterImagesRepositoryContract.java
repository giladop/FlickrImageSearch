package com.imagesearch.searchresults.presenter;

import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;



/**
 * The contract between {@link FlickerImagesSearchPresenter} and flicker images data repository.
 *
 * @author Gilad Opher
 */
public interface PresenterImagesRepositoryContract{


	/**
	 * Call back from API layer
	 */
	interface GetImagesCallback {


		void onImagesLoaded(List<ImageData> images);


		void onImagesNotAvailable();
	}


	/**
	 * Get images method. The results are returned via {@link GetImagesCallback}.
	 * A boolean indication is return indicating a long operation.
	 */
	boolean getImages(@NonNull String query, int page, @NonNull GetImagesCallback callback);


	boolean isCached();

}
