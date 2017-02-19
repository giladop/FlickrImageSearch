package com.imagesearch.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



/**
 * The raw image data object that received from the flicker API.
 *
 * @author Gilad Opher
 */
public class ImageData implements Parcelable{


	@SerializedName("id")
	public String id;


	@SerializedName("secret")
	public String secret;


	@SerializedName("server")
	public String server;


	@SerializedName("farm")
	public String farm;


	@SerializedName("title")
	public String title;


	/**
	 * Return the image url.
	 */
	public String getImageUrl(){
		return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
	}


	// Parcelable implementation
	protected ImageData(Parcel in){
		id = in.readString();
		secret = in.readString();
		server = in.readString();
		farm = in.readString();
		title = in.readString();
	}


	public static final Creator<ImageData> CREATOR = new Creator<ImageData>(){
		@Override
		public ImageData createFromParcel(Parcel in){
			return new ImageData(in);
		}

		@Override
		public ImageData[] newArray(int size){
			return new ImageData[size];
		}
	};


	@Override
	public int describeContents(){
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeString(id);
		dest.writeString(secret);
		dest.writeString(server);
		dest.writeString(farm);
		dest.writeString(title);
	}

}
