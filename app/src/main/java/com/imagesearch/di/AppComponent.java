package com.imagesearch.di;

import android.content.Context;

import com.google.gson.Gson;
import com.imagesearch.db.LikesRepositoryContract;
import com.imagesearch.search.presenter.PresenterRecentSearchContract;
import com.imagesearch.searchresults.model.repository.ImagesRepository;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;



/**
 * @author Gilad Opher
 */
@Singleton
@Component(
		modules = {AppModule.class, RepositoryModule.class}
)
public interface AppComponent{


	Gson gson();


	Context context();


	Picasso picasso();


	PresenterRecentSearchContract PresenterRecentSearchContract();


	LikesRepositoryContract LikesRepositoryContract();


	ImagesRepository ImagesRepository();

}
