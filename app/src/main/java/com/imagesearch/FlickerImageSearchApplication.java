package com.imagesearch;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.imagesearch.di.*;



/**
 * The flicker images search {@link Application}.
 *
 * @author Gilad Opher
 */
public class FlickerImageSearchApplication extends Application{


	/**
	 * The Dagger {@link AppComponent}.
	 */
	private AppComponent appComponent;


	@Override
	public void onCreate(){
		super.onCreate();
		this.appComponent = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext())).build();
	}


	/**
	 * static access to {@link FlickerImageSearchApplication}.
	 */
	public static FlickerImageSearchApplication getFlickerImageSearchApplication(@NonNull Context context){
		return (FlickerImageSearchApplication)context.getApplicationContext();
	}


	/**
	 * Return {@link AppComponent}.
	 */
	public AppComponent getAppComponent(){
		return appComponent;
	}


	/**
	 * Create and return {@link ApiComponent}.
	 */
	public ApiComponent getApiComponent(){
		return DaggerApiComponent.builder().appComponent(appComponent).apiModule(new ApiModule()).build();
	}
}
