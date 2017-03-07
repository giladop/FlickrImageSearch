package com.imagesearch.searchresults.view;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.imagesearch.FlickerImageSearchApplication;
import com.imagesearch.R;
import com.imagesearch.di.DaggerFlickerImagesComponent;
import com.imagesearch.di.FlickerImagesComponent;
import com.imagesearch.di.FlickerImagesModule;
import com.imagesearch.searchresults.model.data.ImageData;
import com.imagesearch.searchresults.presenter.FlickerImagesPresenterViewContract;
import com.imagesearch.searchresults.presenter.FlickerImagesSearchPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * This is the main {@link Activity} of this app and act as the VIEW in the MVP pattern.
 * For simplify the activity hosts both search and list fragments and delegate actions/data to proper fragment.
 *
 */
public class SearchResultsActivity extends AppCompatActivity
		implements FlickerImagesPresenterViewContract.View{


	private static final String QUERY_EXTRA = "queryExtra";


	@BindView(R.id.root)
	View view;


	@BindView(R.id.swipe_refresh_layout)
	SwipeRefreshLayout swipeRefreshLayout;


	@BindView(R.id.images_list)
	RecyclerView imagesList;


	@BindView(R.id.toolbar)
	Toolbar toolbar;


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


	/**
	 * Images data list.
	 */
	private ArrayList<ImageData> images;


	/**
	 * This is the PRESENTER in MVP pattern. For simplify,
	 * the presenter handles/manages both search and list views.
	 */
	@Inject
	FlickerImagesSearchPresenter flickerImagesSearchPresenter;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_images);
		ButterKnife.bind(this);
		inject();

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FragmentManager fm = getFragmentManager();

		//setup presenter
		setupAndBindPresenter(fm);

		ButterKnife.bind(this, view);

		toolbar.setTitle(getIntent().getStringExtra(QUERY_EXTRA));

		GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
		final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				currentPage = page;
				loadMore();
			}
		};

		swipeRefreshLayout.setOnRefreshListener(() -> {
			scrollListener.resetState();
			imagesAdapter.refresh();
			currentPage = 1;
			loadMore();
		});


		imagesList.setLayoutManager(gridLayoutManager);



		imagesAdapter = new ImagesAdapter(this, this::openFullScreenImageView);

		imagesList.setAdapter(imagesAdapter);
		imagesList.addOnScrollListener(scrollListener);

		if (savedInstanceState != null){
			currentPage = savedInstanceState.getInt("page");
			scrollListener.setCurrentPage(currentPage);
			images = savedInstanceState.getParcelableArrayList("images");
			imagesAdapter.addImages(images);
		}else{
			images = new ArrayList<>();
			currentPage = 1;
			loadMore();
		}

		fab.setOnClickListener(v -> finish());

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
	 * create/recreate {@link FlickerImagesSearchPresenter} with help of {@link PresenterHolder} the presenter
	 * Survive configuration change.
	 */
	private void setupAndBindPresenter(FragmentManager fm){
		PresenterHolder presenterHolder = (PresenterHolder) fm.findFragmentByTag("presenterHolder");
		if (presenterHolder == null) {

			fm.beginTransaction().add(new PresenterHolder(), "presenterHolder").commit();
			fm.executePendingTransactions();
			inject();
		}else
			flickerImagesSearchPresenter = presenterHolder.getFlickerImagesSearchPresenter();

		if (flickerImagesSearchPresenter == null)
			inject();

		flickerImagesSearchPresenter.bind(this);
	}


	/**
	 * Save {@link FlickerImagesSearchPresenter}.
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState){

		PresenterHolder presenterHolder = (PresenterHolder) getFragmentManager().findFragmentByTag("presenterHolder");
		if (presenterHolder != null)
			presenterHolder.setFlickerImagesSearchPresenter(flickerImagesSearchPresenter);

		outState.putInt("page", currentPage);
		outState.putParcelableArrayList("images", images);

		super.onSaveInstanceState(outState);
	}


	/**
	 * Bind view (this) with presenter.
	 */
	@Override
	protected void onStart(){
		super.onStart();
		flickerImagesSearchPresenter.bind(this);
	}


	/**
	 * Un-Bind view (this) from presenter.
	 */
	@Override
	protected void onStop(){
		super.onStop();
		flickerImagesSearchPresenter.unbind();
	}


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


	@Override
	public void showLoadingImagesProgressIndicator(){
		if (!swipeRefreshLayout.isRefreshing())
			swipeRefreshLayout.setRefreshing(true);
	}


	@Override
	public void hideLoadingImagesProgressIndicator(){
		swipeRefreshLayout.setRefreshing(false);
	}


	@Override
	public void onImagesLoaded(List<ImageData> images){
		this.images.addAll(images);
		imagesAdapter.addImages(images);
	}




	@Override
	public void onImagesNotFound(){
		Snackbar.make(view, R.string.no_images_found, Snackbar.LENGTH_LONG).show();
	}



	private void loadMore(){
		String query = getIntent().getStringExtra(QUERY_EXTRA);
		flickerImagesSearchPresenter.getImages(query, currentPage);
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