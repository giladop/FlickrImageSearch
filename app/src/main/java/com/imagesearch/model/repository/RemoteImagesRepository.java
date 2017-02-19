package com.imagesearch.model.repository;

import android.support.annotation.NonNull;
import com.imagesearch.model.api.FlickerApiImpl;
import com.imagesearch.model.data.ImagesData;

import javax.inject.Inject;



/**
 * Remote data source. fetch data from google maps API's
 *
 * @author Gilad Opher
 */
public class RemoteImagesRepository implements RemoteRepository{


	/**
	 * The API retrofit impl.
	 */
	private FlickerApiImpl api;


	@Inject
	public RemoteImagesRepository(FlickerApiImpl api){
		this.api = api;
	}



	@Override
	public void getImages(@NonNull String query, int page, @NonNull final GetImagesCallback callback){

		api.getImages(query, page, new GetImagesCallback(){
			@Override
			public void onImagesLoaded(ImagesData images){
				callback.onImagesLoaded(images);
			}

			@Override
			public void onImagesNotAvailable()	{
				callback.onImagesNotAvailable();
			}
		});
	}


}
