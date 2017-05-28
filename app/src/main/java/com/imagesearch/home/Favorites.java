package com.imagesearch.home;

import com.google.firebase.database.IgnoreExtraProperties;
import com.imagesearch.searchresults.model.data.ImageData;

import java.util.ArrayList;
import java.util.Map;



/**
 * Created by giladopher on 09/05/2017.
 */
@IgnoreExtraProperties
public class Favorites{


	public Map<String , ArrayList<ImageData>> images;


	public Favorites(){
	}

	public Favorites(Map<String , ArrayList<ImageData>> images){
		this.images = images;
	}
}
