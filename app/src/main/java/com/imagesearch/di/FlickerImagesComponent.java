package com.imagesearch.di;

import com.imagesearch.search.view.SearchActivity;
import com.imagesearch.searchresults.presenter.FlickerImagesSearchPresenter;
import com.imagesearch.searchresults.view.FullScreenImageActivity;
import com.imagesearch.searchresults.view.SearchResultsActivity;

import dagger.Component;



/**
 * @author Gilad Opher
 */
@ActivityScoped
@Component(
		dependencies = {AppComponent.class},
		modules = {FlickerImagesModule.class}
)
public interface FlickerImagesComponent{


	void inject(SearchResultsActivity activity);


	void inject(FullScreenImageActivity activity);


	void inject(SearchActivity activity);


}
