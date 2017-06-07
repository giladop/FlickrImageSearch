package com.imagesearch.db;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;



/**
 * Created by giladopher on 10/05/2017.
 */
public interface LikesRepositoryContract{


	interface LikesCallBack{


		void onLikesLoaded(LiveData<List<ImageData>> images);


		void onLikesLoadedFailed();

	}

	LiveData<List<ImageData>> getUserLike(@NonNull String userIds);


	void getAllLikes(@NonNull LikesCallBack callBack);


	void like(@NonNull String userId, @NonNull ImageData data);


	void unLike(@NonNull String userId, @NonNull String imageDataId);



}
