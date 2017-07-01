package com.imagesearch.searchresults.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;



/**
 * A container object for list of {@link ImageData} objects.
 *
 * @author Gilad Opher
 */
public class ImagesData{


	@SerializedName("page")
	public int page;



	@SerializedName("photo")
	public List<ImageData> images;



	public ImagesData(int page, List<ImageData> images) {
		this.page = page;
		this.images = images;
	}
}
