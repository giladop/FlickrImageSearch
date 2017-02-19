package com.imagesearch.model.repository;

import android.support.annotation.NonNull;

import com.imagesearch.model.data.ImageData;
import com.imagesearch.model.data.ImagesData;
import com.imagesearch.presenter.PresenterImagesRepositoryContract;

import java.util.*;

import javax.inject.Inject;



/**
 * The repository responsible to supply the image data from flicker API.
 * In case same query with same page number is last one is requested (Like in configuration change)
 * return results from in-memory cache.
 *
 * @author Gilad Opher
 */
public class FlickerImagesImagesRepository implements PresenterImagesRepositoryContract{


	/**
	 * The data source that we'll use for getting the remote data.
	 */
	private RemoteRepository remoteRepository;


	/**
	 * Save the last page for track if use cache.
	 */
	private int lastPage;


	/**
	 * Save the last query for track if use cache.
	 */
	private String lastQuery;


	/**
	 * A {@link ImagesData} cache.
	 */
	private List<ImageData> imagesCache = new ArrayList<>();



	@Inject
	public FlickerImagesImagesRepository(RemoteRepository remoteRepository){
		this.remoteRepository = remoteRepository;
	}


	/**
	 * Get images method. The results are return via {@link GetImagesCallback}.
	 * A boolean indication is return indicating a long operation.
	 */
	@Override
	public boolean getImages(@NonNull final String query, final int page, @NonNull final GetImagesCallback callback){

		// check if in cache
		if (isCachedData(query, page)){
			callback.onImagesLoaded(imagesCache);
			return false;
		}

		// clear cache if new query
		if (lastQuery != null && !lastQuery.equals(query)){
			imagesCache.clear();
		}

		//store last query/page
		lastQuery = query;
		lastPage = page;

		//fetch images from remote API.
		remoteRepository.getImages(query, page, new RemoteRepository.GetImagesCallback(){

			@Override
			public void onImagesLoaded(ImagesData imagesData){
				imagesCache.addAll(imagesData.images);
				callback.onImagesLoaded(imagesData.images);
			}

			@Override
			public void onImagesNotAvailable(){
				callback.onImagesNotAvailable();
			}
		});
		return true;
	}


	/**
	 * Check if data was already fetched for this query+page request.
	 */
	private boolean isCachedData(@NonNull String newQuery, int newPage){
		if (lastQuery == null || imagesCache.isEmpty()) return false;

		return lastQuery.equals(newQuery) && lastPage == newPage;
	}

}
