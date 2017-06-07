package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.imagesearch.searchresults.model.data.ImageData;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by giladopher on 05/06/2017.
 */
public class ImageRepositoryCache{


	private final long CACHE_EXPIRATION_TIME = DateUtils.MINUTE_IN_MILLIS * 3;


	private long cacheTime;


	private String query;


	private int page;


	private List<ImageData> imageDataList;


	public ImageRepositoryCache(){
		clearCache();
	}


	public boolean isCached(@NonNull String query, int page){
		if (!query.equals(this.query)){
			clearCache();
			return false;
		}
		if (System.currentTimeMillis() > cacheTime + CACHE_EXPIRATION_TIME)
			return false;

		return this.page == page;
	}


	public void cache(@NonNull String query, int page, List<ImageData> imags){
		this.query = query;
		this.page = page;
		if (imageDataList == null)
			imageDataList = new ArrayList<>(imags);
		else
			imageDataList.addAll(new ArrayList<>(imags));

		cacheTime = System.currentTimeMillis();
	}


	public List<ImageData> getCachedData(){
		return imageDataList;
	}


	public void clearCache(){
		query = null;
		page = 0;
		imageDataList = null;
		cacheTime = 0;
	}



}
