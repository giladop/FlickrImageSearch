package com.imagesearch.model.repository;

import android.support.annotation.NonNull;

import com.imagesearch.model.api.FlickerApi;
import com.imagesearch.model.data.ImageData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;



/**
 * Remote data source. fetch data from google maps API's
 *
 * @author Gilad Opher
 */
public class RemoteImagesRepository implements RemoteRepository{


	/**
	 * The API retrofit impl.
	 */
	private FlickerApi api;


	@Inject
	public RemoteImagesRepository(FlickerApi api){
		this.api = api;
	}


	@Override
	public Observable<List<ImageData>> getImages(@NonNull String query, int page){

		return api.getImages(query, page)
				.subscribeOn(Schedulers.io())
				.map(res -> res.imagesData.images)
				.flatMapIterable(images -> images)
				.buffer(3);
	}

}
