package com.imagesearch.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.imagesearch.db.LikesRepositoryContract;
import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;

import javax.inject.Inject;



/**
 *
 *
 * Created by giladopher on 31/05/2017.
 */
public class FavoritesViewModel extends ViewModel{


	private LiveData<List<ImageData>> favoritesImage;


	private LikesRepositoryContract repository;


	@Inject
	public FavoritesViewModel(LikesRepositoryContract repository){
		this.repository = repository;
	}



    public void init(String uid){
		if (favoritesImage != null)
			return;

		favoritesImage = repository.getUserLike(uid);
	}


	public LiveData<List<ImageData>> getFavoritesImage(){


		return favoritesImage;
	}
}


