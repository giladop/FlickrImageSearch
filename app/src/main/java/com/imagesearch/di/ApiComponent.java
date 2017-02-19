package com.imagesearch.di;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imagesearch.model.api.FlickerApiImpl;
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


	FlickerApiImpl flickerApiImpl();


	SharedPreferences SharedPreferences();


	Gson gson();


	Picasso picasso();

}
