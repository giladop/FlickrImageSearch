package com.imagesearch.model.repository;

import android.support.annotation.NonNull;
import com.imagesearch.model.data.ImagesData;



/**
 * The contract between {@link FlickerImagesImagesRepository} and remote API layer.
 *
 * @author Gilad Opher
 */
public interface RemoteRepository{


	/**
	 * Call back from API layer
	 */
	interface GetImagesCallback {


		void onImagesLoaded(ImagesData images);


		void onImagesNotAvailable();
	}



	/**
	 * Get Images by {@link String} query.
	 * Actual result, return using {@link GetImagesCallback}.
	 */
	void getImages(@NonNull String query, int page, @NonNull GetImagesCallback callback);


}
