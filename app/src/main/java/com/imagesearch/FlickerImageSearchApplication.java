package com.imagesearch;

import android.app.Application;

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


	private static FlickerImageSearchApplication instance;


	@Override
	public void onCreate(){
		super.onCreate();
		instance = FlickerImageSearchApplication.this;
		this.appComponent = DaggerAppComponent.builder().
				appModule(new AppModule(getApplicationContext()))
				.repositoryModule(new RepositoryModule())
				.build();
	}


	/**
	 * static access to {@link FlickerImageSearchApplication}.
	 */
	public static FlickerImageSearchApplication getFlickerImageSearchApplication(){
		return (FlickerImageSearchApplication)instance.getApplicationContext();
	}


	/**
	 * Return {@link AppComponent}.
	 */
	public AppComponent getAppComponent(){
		return appComponent;
	}

}
