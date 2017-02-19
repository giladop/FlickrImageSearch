package com.imagesearch.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;



/**
 * @author Gilad Opher
 */
@Module
public class AppModule{


	private static final String APP_BASE_URL = "https://api.flickr.com/services/rest/";


	private static final String APP_NAME = "FLICKER_IMAGES_APP";


	private final Context context;


	public AppModule(Context context){
		this.context = context;
	}


	@Provides
	@Singleton
	Gson provideGson(){
		return new GsonBuilder()
				.create();
	}


	@Provides
	@Singleton
	Retrofit provideRetrofit(Gson gson){
		return new Retrofit.Builder()
				.baseUrl(APP_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
	}


	@Provides
	@Singleton
	Context provideContext(){
		return context;
	}


	@Provides
	@Singleton
	Picasso providePicasso(Context context){
		return Picasso.with(context);
	}


	@Provides
	SharedPreferences provideSharedPreferences(Context context){
		return context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
	}

}
