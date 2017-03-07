package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.model.data.ImagesData;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;

import java.util.*;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;



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
		if ((lastQuery != null && !lastQuery.equals(query)) || page == 1){
			imagesCache.clear();
		}

		//store last query/page
		lastQuery = query;
		lastPage = page;

		//fetch images from remote API.
		remoteRepository.getImages(query, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeWith( new DisposableObserver<List<ImageData>>(){

				@Override
				public void onNext(List<ImageData> imagesData){
					imagesCache.addAll(imagesData);
					callback.onImagesLoaded(imagesData);
				}

				@Override
				public void onError(Throwable e){
					callback.onImagesNotAvailable();
				}

				@Override
				public void onComplete(){
					callback.onImagesLoaded(new ArrayList<>());
				}
			}
		);
		return true;
	}



	@Override
	public boolean isCached(){
		return imagesCache.isEmpty();
	}


	/**
	 * Check if data was already fetched for this query+page request.
	 */
	private boolean isCachedData(@NonNull String newQuery, int newPage){
		if (lastQuery == null || imagesCache.isEmpty()) return false;

		return lastQuery.equals(newQuery) && lastPage == newPage;
	}

}
