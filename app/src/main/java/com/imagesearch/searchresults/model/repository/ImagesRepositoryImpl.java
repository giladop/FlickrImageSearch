package com.imagesearch.searchresults.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.imagesearch.searchresults.model.data.ImagesData;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;



/**
 *
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
	public LiveData<ImagesData> loadMore(@NonNull String query, int page, boolean clearCache){

		final MutableLiveData<ImagesData> data = new MutableLiveData<>();

		if (clearCache){
			imageRepositoryCache.clearCache();
		}else{
			if (imageRepositoryCache.isCached(query, page)){
				data.setValue(imageRepositoryCache.getCachedData());
				return data;
			}
		}

		remoteRepository.getImages(query, page)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeWith(new DisposableObserver<ImagesData>(){

			   @Override
			   public void onNext(ImagesData imagesData){
				   imageRepositoryCache.cache(query, imagesData);
				   data.setValue(imagesData);
			   }

			   @Override
			   public void onError(Throwable e){}

			   @Override
			   public void onComplete(){}

		   }
		);
		return data;
	}

}
