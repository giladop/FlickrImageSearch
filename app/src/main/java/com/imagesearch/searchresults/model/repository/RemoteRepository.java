package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImagesData;

import io.reactivex.Observable;



/**
 * The contract between {@link ImagesRepositoryImpl} and remote API layer.
 *
 * @author Gilad Opher
 */
public interface RemoteRepository{


	/**
	 * Get Images by {@link String} query.
	 */
	Observable<ImagesData> getImages(@NonNull String query, int page);


}
