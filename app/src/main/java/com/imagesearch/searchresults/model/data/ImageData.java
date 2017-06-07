package com.imagesearch.searchresults.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;



/**
 * The raw image data object that received from the flicker API.
 *
 * @author Gilad Opher
 */
public class ImageData implements Parcelable{


	@SerializedName("farm")
	private String farm;



	@SerializedName("id")
	private String id;


	@SerializedName("secret")
	private String secret;


	@SerializedName("server")
	private String server;



	@SerializedName("title")
	private String title;


	private int totalLikes;


	private Map<String, Boolean> likes = new HashMap<>();


	public ImageData(){
	}

	protected ImageData(Parcel in){
		farm = in.readString();
		id = in.readString();
		secret = in.readString();
		server = in.readString();
		title = in.readString();
		totalLikes = in.readInt();
	}



	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeString(farm);
		dest.writeString(id);
		dest.writeString(secret);
		dest.writeString(server);
		dest.writeString(title);
		dest.writeInt(totalLikes);
	}

	@Override
	public int describeContents(){
		return 0;
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

	public String getId(){
		return id;
	}


	public String getTitle(){
		return title;
	}


	public void setSecret(String secret){
		this.secret = secret;
	}

	public void setServer(String server){
		this.server = server;
	}

	public void setFarm(String farm){
		this.farm = farm;
	}

	public void like(String id){
		totalLikes++;
		likes.put(id, true);
	}


	public void unLike(String id){
		if (likes.containsKey(id)){
			totalLikes--;
			likes.remove(id);
		}
	}


	public int getTotalLikes(){
		return totalLikes;
	}




	/**
	 * Return the image url.
	 */
	public String getImageUrl(){
		return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
	}


	@Exclude
	public Map<String, Object> toMap() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("farm", farm);
		result.put("id", id);
		result.put("secret", secret);
		result.put("server", server);
		result.put("title", title);
		result.put("totalLikes", totalLikes);
		result.put("likes", likes);
		return result;
	}


	public Map<String, Boolean> getLikes(){
		return likes;
	}
}
