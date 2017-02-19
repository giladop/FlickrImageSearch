package com.imagesearch.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * A container object for list of {@link ImageData} objects.
 *
 * @author Gilad Opher
 */
public class ImagesData{


	@SerializedName("photo")
	public List<ImageData> images;


	public ImagesData(List<ImageData> images){
		this.images = images;
	}

}
