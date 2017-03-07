package com.imagesearch.search.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.search.presenter.SearchPresenter;
import com.imagesearch.search.presenter.SearchPresenterViewContract;
import com.imagesearch.searchresults.view.SearchResultsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



public class SearchActivity extends AppCompatActivity
		implements SearchView.OnQueryTextListener, SearchPresenterViewContract.View{


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
	 * Dagger injection.
	 */
	private void inject(){
		FlickerImagesComponent flickerImagesComponent =
				DaggerFlickerImagesComponent.builder()
						.apiComponent(FlickerImageSearchApplication.getFlickerImageSearchApplication(this).getApiComponent())
						.flickerImagesModule(new FlickerImagesModule())
						.build();
		flickerImagesComponent.inject(this);
	}


	@Inject
	SearchPresenter searchPresenter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		ButterKnife.bind(this);
		inject();


		setSupportActionBar(toolbar);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		historyList.setLayoutManager(linearLayoutManager);

		historyAdapter = new SearchHistoryAdapter(this::invokeSearch);

		historyList.setAdapter(historyAdapter);

		searchView.setOnQueryTextListener(this);
		searchView.setIconifiedByDefault(false);

		fab.setOnClickListener(v -> onFabClicked());
	}


	private void invokeSearch(String query){
		searchPresenter.onNewQuery(query);
		startActivity(SearchResultsActivity.newInstance(this, query));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main_images, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_clear_search:
				searchPresenter.clearHistory();
				historyAdapter.clearHistory();
				return false;
			default:
				break;
		}

		return false;
	}


	@Override
	protected void onResume(){
		super.onResume();
		searchPresenter.getHistory();
	}


	@Override
	protected void onStart(){
		super.onStart();
		searchPresenter.bind(this);
	}


	@Override
	protected void onStop(){
		super.onStop();
		searchPresenter.unbind();
	}

	/**
	 * Invoked when submit or {@link FloatingActionButton} clicked.
	 */
	@Override
	public boolean onQueryTextSubmit(String query){
		invokeSearch(query);
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
			invokeSearch(searchView.getQuery().toString());
	}


}
