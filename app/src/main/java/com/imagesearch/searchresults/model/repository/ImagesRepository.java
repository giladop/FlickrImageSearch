package com.imagesearch.searchresults.model.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImagesData;



/**
 *
 * Created by giladopher on 05/06/2017.
 */
public interface ImagesRepository{


	LiveData<ImagesData> loadMore(@NonNull String query, int page, boolean clearCache);


}
