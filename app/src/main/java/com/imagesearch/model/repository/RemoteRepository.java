package com.imagesearch.model.repository;

import android.support.annotation.NonNull;

import com.imagesearch.model.data.ImageData;

import java.util.List;

import io.reactivex.Observable;



/**
 * The contract between {@link FlickerImagesImagesRepository} and remote API layer.
 *
 * @author Gilad Opher
 */
public interface RemoteRepository{


	/**
	 * Get Images by {@link String} query.
	 */
	Observable<List<ImageData>> getImages(@NonNull String query, int page);


}
