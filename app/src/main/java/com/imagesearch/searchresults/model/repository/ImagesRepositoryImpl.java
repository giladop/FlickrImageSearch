package com.imagesearch.searchresults.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;


import java.util.List;
import android.util.Log;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;



/**
 * Created by giladopher on 05/06/2017.
 */
public class ImagesRepositoryImpl implements ImagesRepository{


	private RemoteRepository remoteRepository;


	private ImageRepositoryCache imageRepositoryCache;


	@Inject
	public ImagesRepositoryImpl(RemoteRepository remoteRepository, ImageRepositoryCache imageRepositoryCache){
		this.remoteRepository = remoteRepository;
		this.imageRepositoryCache = imageRepositoryCache;
	}


	@Override
	public LiveData<List<ImageData>> loadMore(@NonNull String query, int page, boolean clearCache){


		Log.d("loadMore","from: repositoryImpl - start");

		final MutableLiveData<List<ImageData>> data = new MutableLiveData<>();

		if (clearCache){
			imageRepositoryCache.clearCache();
			Log.d("loadMore","clearCache");
		}else{
			if (imageRepositoryCache.isCached(query, page)){
				Log.d("loadMore", "get from cache: " + imageRepositoryCache);
				data.setValue(imageRepositoryCache.getCachedData());
				return data;
			}
		}

		remoteRepository.getImages(query, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeWith(new DisposableObserver<List<ImageData>>(){

			   @Override
			   public void onNext(List<ImageData> imagesData){
			//	   Log.d("loadMore","from: repositoryImpl - onNext()");


				   imageRepositoryCache.cache(query, page, imagesData);
				   data.setValue(imagesData);
			   }

			   @Override
			   public void onError(Throwable e){

			   }

			   @Override
			   public void onComplete(){
				   Log.d("loadMore","from: repositoryImpl - onComplete()");
			   }

		   }
		);
		return data;
	}

}
