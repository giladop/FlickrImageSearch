package com.imagesearch.model.data;

import com.google.gson.annotations.SerializedName;



/**
 * * A container object for {@link ImagesData} objects.
 *
 * @author Gilad Opher
 */
public class ImagesResultObject{


	@SerializedName("photos")
	public ImagesData imagesData;


}
