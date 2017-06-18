package com.imagesearch.searchresults.model.repository;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.imagesearch.searchresults.model.data.ImageData;

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



	void cache(@NonNull String query, int page, List<ImageData> images){
		this.query = query;
		this.page = page;
		this.cacheTime = System.currentTimeMillis();
		if (imageDataList == null)
			imageDataList = new ArrayList<>(images);
		else
			imageDataList.addAll(new ArrayList<>(images));
	}


	List<ImageData> getCachedData(){
		return imageDataList;
	}


	public void clearCache(){
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
				", imageDataList=" + imageDataList != null ? String.valueOf(imageDataList.size()) : "0" +
				'}';
	}
}
