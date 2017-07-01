package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.model.data.ImagesData;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple 3 time base cache
 *
 * Created by giladopher on 05/06/2017.
 */
public class ImageRepositoryCache{


	private static final long CACHE_EXPIRATION_TIME = DateUtils.MINUTE_IN_MILLIS * 3;



	private long cacheTime;


	private String query;


	private int page;


	private List<ImageData> imageDataList;



	public ImageRepositoryCache(){
		clearCache();
	}



	boolean isCached(@NonNull String query, int page){
		if (!query.equals(this.query) ||
			System.currentTimeMillis() > cacheTime + CACHE_EXPIRATION_TIME){
				clearCache();
				return false;
		}
		return this.page == page;
	}



	void cache(@NonNull String query, ImagesData imagesData){
		this.query = query;
		this.page = imagesData.page;
		this.cacheTime = System.currentTimeMillis();
		if (imageDataList == null)
			this.imageDataList = new ArrayList<>(imagesData.images);
		else
			this.imageDataList.addAll(imagesData.images);
	}


	ImagesData getCachedData(){
		return new ImagesData(page, imageDataList);
	}


	void clearCache(){
		query = null;
		page = 0;
		imageDataList = null;
		cacheTime = 0;
	}


	@Override
	public String toString(){
		return "ImageRepositoryCache{" +
				"cacheTime=" + cacheTime +
				", query='" + query + '\'' +
				", page=" + page +
				'}';
	}
}
