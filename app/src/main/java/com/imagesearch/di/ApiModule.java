package com.imagesearch.di;

import com.imagesearch.model.api.FlickerApi;
import com.imagesearch.model.api.FlickerApiImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;



/**
 * @author Gilad Opher
 */
@Module
public class ApiModule{


	@Provides
	FlickerApi provideGoogleMapsApi(Retrofit retrofit){
		return retrofit.create(FlickerApi.class);
	}


	@Provides
	FlickerApiImpl provideGoogleMapsApiImpl(FlickerApi api){
		return new FlickerApiImpl(api);
	}

}
