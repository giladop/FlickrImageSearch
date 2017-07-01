package com.imagesearch.searchresults.model.api;

import com.imagesearch.searchresults.model.data.ImagesPageResult;

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

	@GET("?method=flickr.photos.search&api_key=a933db0988735825c98458892db8a853&format=json&nojsoncallback=1&safe_search=1")
	Observable<ImagesPageResult> getImages(@Query("text") String query, @Query("page") int page);


}
