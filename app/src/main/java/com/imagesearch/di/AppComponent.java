package com.imagesearch.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.searchresults.presenter.PresenterImagesRepositoryContract;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;



/**
 * @author Gilad Opher
 */
@Singleton
@Component(
		modules = {AppModule.class, RepositoryModule.class}
)
public interface AppComponent{


	Gson gson();


	Retrofit retrofit();


	Context context();


	Picasso picasso();


	SharedPreferences sharedPreferences();


	PresenterImagesRepositoryContract PresenterImagesRepositoryContract();


	PresenterRecentSearchContract PresenterRecentSearchContract();

}
