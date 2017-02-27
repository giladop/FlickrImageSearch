package com.imagesearch.model.api;

import com.imagesearch.model.data.ImagesResultObject;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;



/**
 * The flicker images {@link Retrofit} API.
 *
 * @author Gilad Opher
 */
public interface FlickerApi{


	@GET("?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1")
	Observable<ImagesResultObject> getImages(@Query("text") String query, @Query("page") int page);


}
