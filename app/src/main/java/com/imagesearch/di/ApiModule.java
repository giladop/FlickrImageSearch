package com.imagesearch.di;

import com.imagesearch.model.api.FlickerApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;



/**
 * @author Gilad Opher
 */
@Module
public class ApiModule{


	@Provides
	FlickerApi provideFlickerApi(Retrofit retrofit){
		return retrofit.create(FlickerApi.class);
	}


}
