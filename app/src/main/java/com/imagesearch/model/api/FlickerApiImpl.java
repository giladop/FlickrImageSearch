package com.imagesearch.model.api;

import android.support.annotation.NonNull;

import com.imagesearch.model.data.ImagesResultObject;
import com.imagesearch.model.repository.RemoteRepository;

import javax.inject.Inject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



/**
 * The flicker images {@link Retrofit} API implementation.
 *
 * @author Gilad Opher
 */
public class FlickerApiImpl{


	/**
	 * The API interface declaration.
	 */
	private FlickerApi api;


	@Inject
	public FlickerApiImpl(FlickerApi api){
		this.api = api;
	}


	/**
	 * The images request method.
	 */
	public void getImages(@NonNull String query, int page, @NonNull final RemoteRepository.GetImagesCallback callback){

		api.getImages(query, page).enqueue(new Callback<ImagesResultObject>(){

			@Override
			public void onResponse(Response<ImagesResultObject> response){
				ImagesResultObject resultObject = response.body();
				if (resultObject == null)
					callback.onImagesNotAvailable();
				else
					callback.onImagesLoaded(resultObject.imagesData);
			}

			@Override
			public void onFailure(Throwable t){
				callback.onImagesNotAvailable();
			}
		});
	}

}
