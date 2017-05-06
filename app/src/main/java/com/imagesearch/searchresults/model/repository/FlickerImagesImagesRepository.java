package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.model.data.ImagesData;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;

import java.util.ArrayList;
import java.util.List;

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


	private boolean isQuering;



	private GetImagesCallback callback;


	@Inject
	public FlickerImagesImagesRepository(RemoteRepository remoteRepository){
		this.remoteRepository = remoteRepository;
	}


	/**
	 * Get images method. The results are return via {@link GetImagesCallback}.
	 * A boolean indication is return indicating a long operation.
	 */
	@Override
	public boolean getImages(@NonNull final String query, final int page){

		Log.d("callback", "getImages() query: " + query + " ,page: "  + page  + " callback: " + callback + ", isQuering = " + isQuering + " , repository = " + this);

		// check if in cache
		if (isCachedData(query, page)){
			Log.d("callback", "return cache to callback: " + callback + ", imagesCache = " + imagesCache.size());
			callback.onImagesLoaded(imagesCache);
			return false;
		}



		// clear cache if new query
		if ((lastQuery != null && !lastQuery.equals(query)) || page == 1){
			Log.d("callback", "clear cache");
			imagesCache.clear();
		}



		//fetch images from remote API.
		Log.d("callback", "fetch images");
		isQuering = true;
		remoteRepository.getImages(query, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeWith( new DisposableObserver<List<ImageData>>(){

				@Override
				public void onNext(List<ImageData> imagesData){
					imagesCache.addAll(imagesData);
					//store last query/page
					lastQuery = query;
					lastPage = page;

					if (callback != null)
						callback.onImagesLoaded(imagesData);
				}

				@Override
				public void onError(Throwable e){
					if (callback != null)
						callback.onImagesNotAvailable();
				}

				@Override
				public void onComplete(){
					isQuering = false;
	//				Log.d("callback", "after: " + callback);
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


	@Override
	public void clearCache(){
		imagesCache.clear();
		lastQuery = null;
		lastPage = 0;
	}

	@Override
	public void bindCallback(GetImagesCallback callback){
		this.callback = callback;
	}

	@Override
	public void unBindCallback(){
		this.callback = null;
	}


	/**
	 * Check if data was already fetched for this query+page request.
	 */
	private boolean isCachedData(@NonNull String newQuery, int newPage){
		if (isQuering) return true;

		if (lastQuery == null || imagesCache.isEmpty()) return false;

		//if (lastQuery.equals(newQuery) && isQuering) return true;

		return lastQuery.equals(newQuery) && lastPage == newPage;
	}

}
