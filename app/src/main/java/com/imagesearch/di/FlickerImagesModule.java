package com.imagesearch.di;

import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.search.presenter.SearchPresenter;
import com.imagesearch.searchresults.presenter.FlickerImagesSearchPresenter;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;

import dagger.Module;
import dagger.Provides;



/**
 * @author Gilad Opher
 */
@Module
public class FlickerImagesModule{


	@Provides
	FlickerImagesSearchPresenter provideFlickerImagesSearchPresenter(PresenterImagesRepositoryContract imagesRepositoryContract){
		return new FlickerImagesSearchPresenter(imagesRepositoryContract);
	}


	@Provides
	SearchPresenter provideSearchPresenter(PresenterRecentSearchContract recentSearchContract){
		return new SearchPresenter(recentSearchContract);
	}

}
