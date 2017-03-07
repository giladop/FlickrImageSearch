package com.imagesearch.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.searchresults.model.api.FlickerApi;
import com.imagesearch.searchresults.model.repository.FlickerImagesImagesRepository;
import com.imagesearch.searchresults.model.repository.RemoteImagesRepository;
import com.imagesearch.searchresults.model.repository.RemoteRepository;
import com.imagesearch.searchresults.presenter.FlickerImagesSearchPresenter;
import com.imagesearch.search.model.repository.RecentSearchesRepository;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;
import com.imagesearch.search.presenter.SearchPresenter;

import dagger.Module;
import dagger.Provides;



/**
 * @author Gilad Opher
 */
@Module
public class FlickerImagesModule{


	@Provides
	RemoteRepository provideRemoteRepository(FlickerApi api){
		return new RemoteImagesRepository(api);
	}


	@Provides
	PresenterImagesRepositoryContract provideFlickerImagesRepository(RemoteRepository remoteRepository){
		return new FlickerImagesImagesRepository(remoteRepository);
	}


	@Provides
	PresenterRecentSearchContract providePresenterRecentSearchContract(SharedPreferences prefs, Gson gson){
		return new RecentSearchesRepository(prefs, gson);
	}


	@Provides
	FlickerImagesSearchPresenter provideFlickerImagesSearchPresenter(PresenterImagesRepositoryContract imagesRepositoryContract){
		return new FlickerImagesSearchPresenter(imagesRepositoryContract);
	}


	@Provides
	SearchPresenter provideSearchPresenter(PresenterRecentSearchContract recentSearchContract){
		return new SearchPresenter(recentSearchContract);
	}

}
