package com.imagesearch.di;

import com.imagesearch.db.LikesRepositoryContract;
import com.imagesearch.home.FavoritesViewModel;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.search.presenter.SearchPresenter;
import com.imagesearch.searchresults.model.repository.ImagesRepository;
import com.imagesearch.searchresults.view.SearchResultsViewModel;

import dagger.Module;
import dagger.Provides;



/**
 * @author Gilad Opher
 */
@Module
public class FlickerImagesModule{


	@Provides
	SearchPresenter provideSearchPresenter(PresenterRecentSearchContract recentSearchContract){
		return new SearchPresenter(recentSearchContract);
	}

	@Provides
	FavoritesViewModel provideFavoritesViewModel(LikesRepositoryContract likesRepositoryContract){
		return new FavoritesViewModel(likesRepositoryContract);
	}

	@Provides
	SearchResultsViewModel provideSearchResultsViewModel(ImagesRepository imagesRepository){
		return new SearchResultsViewModel(imagesRepository);
	}
}
