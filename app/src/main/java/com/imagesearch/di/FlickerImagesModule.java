package com.imagesearch.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.model.api.FlickerApiImpl;
import com.imagesearch.model.repository.*;
import com.imagesearch.presenter.FlickerImagesSearchPresenter;
import com.imagesearch.presenter.PresenterRecentSearchContract;
import com.imagesearch.presenter.PresenterImagesRepositoryContract;

import dagger.Module;
import dagger.Provides;



/**
 * @author Gilad Opher
 */
@Module
public class FlickerImagesModule{


	@Provides
	RemoteRepository provideRemoteRepository(FlickerApiImpl api){
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
	FlickerImagesSearchPresenter provideFlickerImagesSearchPresenter(PresenterImagesRepositoryContract imagesRepositoryContract, PresenterRecentSearchContract recentSearchContract){
		return new FlickerImagesSearchPresenter(imagesRepositoryContract, recentSearchContract);
	}

}
