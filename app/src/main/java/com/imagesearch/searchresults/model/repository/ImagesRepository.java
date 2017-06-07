package com.imagesearch.searchresults.model.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;



/**
 * Created by giladopher on 05/06/2017.
 */
public interface ImagesRepository{


	LiveData<List<ImageData>> getImages(@NonNull String query, int page);
}
