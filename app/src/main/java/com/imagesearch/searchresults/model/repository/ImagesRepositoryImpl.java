package com.imagesearch.searchresults.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.ArrayList;
import java.util.List;

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
	public LiveData<List<ImageData>> getImages(@NonNull String query, int page){

		final MutableLiveData<List<ImageData>> data = new MutableLiveData<>();

		if (imageRepositoryCache.isCached(query, page)){
			data.setValue(imageRepositoryCache.getCachedData());
			return data;
		}

		remoteRepository.getImages(query, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeWith(new DisposableObserver<List<ImageData>>(){

			   @Override
			   public void onNext(List<ImageData> imagesData){
				   imageRepositoryCache.cache(query, page, imagesData);
				   data.setValue(imagesData);
			   }

			   @Override
			   public void onError(Throwable e){

			   }

			   @Override
			   public void onComplete(){

			   }

		   }
		);
		return data;
	}

}
