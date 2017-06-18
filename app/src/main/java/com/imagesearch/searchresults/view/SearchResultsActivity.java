package com.imagesearch.searchresults.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.searchresults.model.data.ImageData;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * This is the main {@link Activity} of this app and act as the VIEW in the MVP pattern.
 * For simplify the activity hosts both search and list fragments and delegate actions/data to proper fragment.
 *
 */
public class SearchResultsActivity extends LifecycleActivity {


	private static final String BUNDLE_RECYCLER_LAYOUT = "search.result.activity.recycler.layout";


	private static final String QUERY_EXTRA = "queryExtra";


	@BindView(R.id.root)
	View view;

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.swipe_refresh_layout)
	SwipeRefreshLayout swipeRefreshLayout;


	@BindView(R.id.images_list)
	RecyclerView imagesList;


	@BindView(R.id.fab)
	FloatingActionButton fab;


	/**
	 * The images adapter.
	 */
	private ImagesAdapter imagesAdapter;


	/**
	 * Save the current page.
	 */
	private int currentPage;


	private ViewModelObserver observer = new ViewModelObserver();


	@Inject
	SearchResultsViewModel searchResultsViewModel;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_images);
		ButterKnife.bind(this);
		inject();

		toolbar.setTitle(getIntent().getStringExtra(QUERY_EXTRA));
		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				currentPage = page;
				loadMore(false);
			}
		};

		swipeRefreshLayout.setOnRefreshListener(() -> {
			scrollListener.resetState();
			imagesAdapter.refresh();
			currentPage = 1;
			loadMore(true);
		});

		imagesList.setLayoutManager(gridLayoutManager);
		imagesAdapter = new ImagesAdapter(this, this::openFullScreenImageView);

		imagesList.setAdapter(imagesAdapter);
		imagesList.addOnScrollListener(scrollListener);

		if (savedInstanceState != null){
			currentPage = savedInstanceState.getInt("page");
			scrollListener.setCurrentPage(currentPage);
			Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
			imagesList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
		}else{
			currentPage = 1;
		}

		fab.setOnClickListener(v -> finish());

	//	flickerImagesSearchPresenter.bind(this);
		loadMore(false);

	}



	/**
	 * Start full screen image activity with transition animation.
	 */
	private void openFullScreenImageView(ImageData imageData, ImageView imageView){
		Intent startIntent = FullScreenImageActivity.newIntent(this, imageData);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
					this,
					imageView,
					"transition_photo")
					.toBundle();
			startActivity(startIntent, bundle);
		}else
			startActivity(startIntent);
	}



	/**
	 * Save page and layout manager.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("page", currentPage);
		outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, imagesList.getLayoutManager().onSaveInstanceState());
	}



	/**
	 * Dagger injection.
	 */
	private void inject(){
		FlickerImagesComponent flickerImagesComponent =
			DaggerFlickerImagesComponent.builder()
				.appComponent(FlickerImageSearchApplication.getFlickerImageSearchApplication().getAppComponent())
				.flickerImagesModule(new FlickerImagesModule())
				.build();
		flickerImagesComponent.inject(this);
	}



	private void loadMore(boolean clearCache){
		String query = getIntent().getStringExtra(QUERY_EXTRA);
		//flickerImagesSearchPresenter.getImages(query, currentPage);

		swipeRefreshLayout.setRefreshing(true);
		searchResultsViewModel.loadMore(query, currentPage, clearCache).observe(this, observer);
	}



	private class ViewModelObserver implements Observer<List<ImageData>>{



		@Override
		public void onChanged(@Nullable List<ImageData> imageData){
			swipeRefreshLayout.setRefreshing(false);
			imagesAdapter.addImages(imageData);
		}
	}


	/**
	 * Create a instance.
	 */
	public static Intent newInstance(@NonNull Context context, @NonNull String query){

		Intent startIntent = new Intent(context, SearchResultsActivity.class);
		startIntent.putExtra(QUERY_EXTRA, query);

		return startIntent;
	}

}
