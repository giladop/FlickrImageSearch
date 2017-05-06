package com.imagesearch.searchresults.model.repository;

import android.provider.Settings;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.api.FlickerApi;
import com.imagesearch.searchresults.model.data.ImageData;

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

//		return api.getImages(query, page)
//				.subscribeOn(Schedulers.io())
//				.doOnNext(a -> {
//					Thread.sleep(5000);})
//				.map(res -> res.imagesData.images);

	}

}
