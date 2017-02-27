package com.imagesearch.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.model.api.FlickerApi;
import com.squareup.picasso.Picasso;

import dagger.Component;



/**
 * @author Gilad Opher
 */
@PerApi
@Component(
		dependencies = {AppComponent.class},
		modules = {ApiModule.class}
)
public interface ApiComponent{


	FlickerApi flickerApi();


	SharedPreferences SharedPreferences();


	Gson gson();


	Picasso picasso();

}
