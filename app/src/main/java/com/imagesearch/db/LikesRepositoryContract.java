package com.imagesearch.db;

import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;



/**
 * Created by giladopher on 10/05/2017.
 */

public interface LikesRepositoryContract{


	interface LikesCallBack{


		void onLikesLoaded(List<ImageData> images);

	}

	void getUserLike(@NonNull String userId, @NonNull LikesCallBack callBack);


	void getAllLikes(@NonNull LikesCallBack callBack);


	void like(@NonNull String userId, @NonNull ImageData data);


	void unLike(@NonNull String userId, @NonNull String imageDataId);



}
