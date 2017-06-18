package com.imagesearch.searchresults.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.model.repository.ImagesRepository;

import java.util.List;

import javax.inject.Inject;



/**
 * Created by giladopher on 05/06/2017.
 */
public class SearchResultsViewModel extends ViewModel{



	private ImagesRepository repository;



	@Inject
	public SearchResultsViewModel(ImagesRepository repository){
		this.repository = repository;
	}


	public LiveData<List<ImageData>> loadMore(String query, int page, boolean clearCache){
		return repository.loadMore(query, page, clearCache);

	}


}
