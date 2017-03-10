package com.imagesearch.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.search.model.repository.RecentSearchesRepository;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.searchresults.model.api.FlickerApi;
import com.imagesearch.searchresults.model.repository.FlickerImagesImagesRepository;
import com.imagesearch.searchresults.model.repository.RemoteImagesRepository;
import com.imagesearch.searchresults.model.repository.RemoteRepository;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;



/**
 * Created by giladopher on 06/03/2017.
 */
@Module
public class RepositoryModule{


	@Provides
	FlickerApi provideFlickerApi(Retrofit retrofit){
		return retrofit.create(FlickerApi.class);
	}


	@Provides
	RemoteRepository provideRemoteRepository(FlickerApi api){
		return new RemoteImagesRepository(api);
	}


	@Provides
	PresenterRecentSearchContract providePresenterRecentSearchContract(SharedPreferences prefs, Gson gson){
		return new RecentSearchesRepository(prefs, gson);
	}

	@Provides
	@Singleton
	PresenterImagesRepositoryContract provideFlickerImagesRepository(RemoteRepository remoteRepository){
		return new FlickerImagesImagesRepository(remoteRepository);
	}


}
