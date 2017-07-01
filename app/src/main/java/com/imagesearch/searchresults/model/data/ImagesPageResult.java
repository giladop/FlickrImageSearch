package com.imagesearch.searchresults.model.data;

import com.google.gson.annotations.SerializedName;



/**
 * * A container object for {@link ImagesData} objects.
 *
 * @author Gilad Opher
 */
public class ImagesPageResult {



	@SerializedName("photos")
	public ImagesData imagesData;


}
