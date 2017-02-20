package com.imagesearch.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.*;

import com.imagesearch.R;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * This fragment allow enter a search query using {@link SearchView} and submit it to presenter for search images.
 * In addition, the latest search are presented. Clear recent search is available through toolbar overflow menu.
 *
 * @author Gilad Opher
 */
public class SearchImagesFragment extends Fragment implements SearchView.OnQueryTextListener{


	@BindView(R.id.toolbar)
	Toolbar toolbar;


	@BindView(R.id.search_view)
	SearchView searchView;


	@BindView(R.id.history_images_list)
	RecyclerView historyList;


	@BindView(R.id.recent_search_label)
	View recentLabel;


	@BindView(R.id.fab)
	FloatingActionButton fab;


	/**
	 * The {@link RecyclerView.Adapter}.
	 */
	private SearchHistoryAdapter historyAdapter;


	/**
	 * callback to parent {@link Activity}.
	 */
	private SearchImagesCallback listener;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public void onAttach(Context context){
		super.onAttach(context);

		if (context instanceof SearchImagesCallback)
			this.listener = (SearchImagesCallback)context;
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.search_images_layout, container, false);
		ButterKnife.bind(this, view);

		((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		historyList.setLayoutManager(linearLayoutManager);

		historyAdapter = new SearchHistoryAdapter(new SearchHistoryAdapter.SearchHistoryCallback(){
			@Override
			public void onRecentSearchClicked(String query){
				listener.invokeSearch(query);
			}
		});

		historyList.setAdapter(historyAdapter);

		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);

		fab.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				onFabClicked();
			}
		});

		return view;
	}


	/**
	 * Add clear recent search menu item
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main_images, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_clear_search:
				listener.clearRecentSearch();
				historyAdapter.clearHistory();
				return false;
			default:
				break;
		}

		return false;
	}


	@Override
	public void onResume(){
		super.onResume();
		if (listener != null)
			listener.reloadHistory();
	}


	/**
	 * Helper for creating new fragment instance.
	 */
	public static SearchImagesFragment newInstance(){
		return new SearchImagesFragment();
	}


	/**
	 * Invoked when submit or {@link FloatingActionButton} clicked.
	 */
	@Override
	public boolean onQueryTextSubmit(String query){
		listener.invokeSearch(query);
		return false;
	}


	@Override
	public boolean onQueryTextChange(String newText){
		return false;
	}


	public void onHistoryLoaded(List<String> history){
		historyAdapter.onHistoryLoaded(history);
	}


	public void onFabClicked(){
		if (searchView.getQuery().length() > 0)
			listener.invokeSearch(searchView.getQuery().toString());
	}


	/**
	 * Reference to parent {@link Activity}.
	 */
	public interface SearchImagesCallback{


		/**
		 * Invoke when submit query clicked.
		 */
		void invokeSearch(String query);


		/**
		 * Invoke when screen loads for getting recent search from presenter.
		 */
		void reloadHistory();


		/**
		 * Notify presenter on clear history
		 */
		void clearRecentSearch();

	}

}
