package com.imagesearch.search.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.*;



/**
 * The object that stores and handles the "in-memory" operations of recent search queries.
 *
 * @author Gilad Opher
 */
public class RecentSearch{


	/**
	 * The queries are stored in {@link LinkedHashSet} for uniqueness and ordering.
	 */
	@SerializedName("recent")
	private LinkedHashSet<String> recent = new LinkedHashSet<>();


	/**
	 * Add query to {@link LinkedHashSet}.
	 * if item already exist remove and add again so item will jump to first place.
	 */
	public void add(String query){
		boolean newItem = recent.add(query);
		if (!newItem){
			recent.remove(query);
			recent.add(query);
		}
	}


	/**
	 * Return {@link List} of queries reversed order.
	 */
	public List<String> getRecentAsList(){
		List<String> history = new ArrayList<>();
		for (String query : recent) history.add(query);

		Collections.reverse(history);
		return history;
	}


	/**
	 * Clear all queries.
	 */
	public void clear(){
		recent.clear();
	}

}
