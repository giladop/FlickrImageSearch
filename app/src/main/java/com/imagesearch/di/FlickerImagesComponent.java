package com.imagesearch.di;

import com.imagesearch.view.FullScreenImageActivity;
import com.imagesearch.view.MainImagesActivity;

import dagger.Component;



/**
 * @author Gilad Opher
 */
@ActivityScoped
@Component(
		dependencies = {ApiComponent.class},
		modules = {FlickerImagesModule.class}
)
public interface FlickerImagesComponent{


	void inject(MainImagesActivity activity);


	void inject(FullScreenImageActivity activity);

}
